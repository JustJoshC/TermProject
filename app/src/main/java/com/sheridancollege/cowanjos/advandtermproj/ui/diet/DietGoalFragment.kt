package com.sheridancollege.cowanjos.advandtermproj.ui.diet

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.sheridancollege.cowanjos.advandtermproj.AppDatabase
import com.sheridancollege.cowanjos.advandtermproj.Diet
import com.sheridancollege.cowanjos.advandtermproj.R
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentDietBinding
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentDietGoalsBinding
import kotlinx.coroutines.launch

class DietGoalFragment: Fragment() {

    private var _binding: FragmentDietGoalsBinding? = null

    private lateinit var viewModel: DietGoalViewModel
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

        _binding = FragmentDietGoalsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val database = AppDatabase.getInstance(requireContext())

        val dietDao = database.dietDao()

        val dietRepository = DietGoalRepository(dietDao)

        val factory = DietGoalViewModelFactory(dietRepository)

        viewModel = ViewModelProvider(this, factory).get(DietGoalViewModel::class.java)

        auth = FirebaseAuth.getInstance()

        binding.btnSaveDietGoal.setOnClickListener {
            saveDietGoal()
            findNavController().navigate(R.id.action_navigation_diet_goal_to_navigation_diet3)
        }

        binding.btnRemoveDietGoals.setOnClickListener {
            // Call function to delete the last diet goal
            lifecycleScope.launch {
                viewModel.deleteLastDietGoal()
                findNavController().navigate(R.id.action_navigation_diet_goal_to_navigation_diet3)
            }
        }

        return root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveDietGoal(){
        val dietGoal = binding.editDietGoals.text.toString().toInt()

        if(dietGoal == null){
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch{
            val accountId = auth.currentUser!!.email

            if(accountId != null){

                val newDietGoal = Diet(
                    dietId = 0,
                    accountId = accountId,
                    targetCalories = dietGoal
                )

                // add goal
                viewModel.addDietGoal(newDietGoal)

            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}