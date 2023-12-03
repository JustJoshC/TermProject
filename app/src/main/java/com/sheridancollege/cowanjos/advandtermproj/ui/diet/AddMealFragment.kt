package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.sheridancollege.cowanjos.advandtermproj.AppDatabase
import com.sheridancollege.cowanjos.advandtermproj.Meal
import com.sheridancollege.cowanjos.advandtermproj.MealDao
import com.sheridancollege.cowanjos.advandtermproj.R
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentAddMealBinding
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentDietGoalsBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class AddMealFragment: Fragment() {

    private var _binding: FragmentAddMealBinding? = null

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var viewModel: AddMealViewModel
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddMealBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Get Room Database instance
        val database = AppDatabase.getInstance(requireContext())

        // Get Dao
        val addMealDao = database.mealDao()

        // repo instance
        val addMealRepository = AddMealRepository(addMealDao)

        // factory init
        val factory = AddMealViewModelFactory(addMealRepository)

        viewModel = ViewModelProvider(this, factory).get(AddMealViewModel::class.java)

        auth = FirebaseAuth.getInstance()

        var contentResolver = requireContext().contentResolver

        binding.btnSaveMeal.setOnClickListener {
            saveMeal()
            findNavController().navigate(R.id.action_navigation_meal_to_navigation_diet3)
        }

        binding.btnDeleteMeal.setOnClickListener {
            lifecycleScope.launch {
                viewModel.deleteLastMeal()
                findNavController().navigate(R.id.action_navigation_meal_to_navigation_diet3)
            }
        }

        binding.btnTakePhoto.setOnClickListener {
            takePhoto(contentResolver)
        }

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }


        cameraExecutor = Executors.newSingleThreadExecutor()

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveMeal(){
        val mealName = binding.editMealName.text.toString()
        val mealCalories = binding.editMealCalories.text.toString()
        val mealCaloriesInt = mealCalories.toInt()
        val mealDateDay = binding.MealDatePicker.dayOfMonth.toString()
        val mealDateMonth = binding.MealDatePicker.month.toString()
        val mealDateYear = binding.MealDatePicker.year.toString()

        val mealDate = "$mealDateDay/$mealDateMonth/$mealDateYear"

        if(mealName.isBlank() || mealCalories.isBlank()){
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val accountId = auth.currentUser!!.email

            if(accountId != null){

                val newMeal = Meal(
                    dietId = 0,
                    mealId = 0,
                    accountId = accountId,
                    mealTime = mealDate,
                    mealDescription = mealName,
                    mealCalories = mealCaloriesInt
                )
                // add meal
                viewModel.addMeal(newMeal)
            }
        }

    }

    private fun takePhoto(contentResolver: ContentResolver) {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/TermProject-Image")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this.requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: return
                    val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(savedUri))
                    Log.d(TAG, "Photo capture succeeded: $savedUri")
                }
            }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(_binding?.viewFinder?.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this.requireContext()))

    }

    private fun requestPermissions() {
        val activityResultLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions())
            { permissions ->
                // Handle Permission granted/rejected
                var permissionGranted = true
                permissions.entries.forEach {
                    if (it.key in REQUIRED_PERMISSIONS && it.value == false)
                        permissionGranted = false
                }
                if (!permissionGranted) {

                } else {
                    startCamera()
                }
            }
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this.requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }

    companion object {
        private const val TAG = "TermProject"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}