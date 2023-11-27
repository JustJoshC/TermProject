package com.sheridancollege.cowanjos.advandtermproj.ui.account

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sheridancollege.cowanjos.advandtermproj.R
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentEditAccountBinding


class EditAccount : Fragment() {

    private lateinit var binding: FragmentEditAccountBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        layoutInflater.inflate(R.layout.fragment_edit_account, container, false)
        binding = FragmentEditAccountBinding.inflate(layoutInflater)

        var cancelButton = binding.cancelButton
        var saveButton = binding.saveAccount

        binding.userEmail.setText(auth.currentUser!!.email)


        cancelButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_editAccount_to_navigation_account)
        }

        saveButton.setOnClickListener {
            user = auth.currentUser!!

            //val newCredentials = EmailAuthProvider.getCredential(binding.userEmail.text.toString(), binding.userPassword.text.toString())

            //user.reauthenticate(newCredentials).onSuccessTask {
                //user.updateEmail(binding.userEmail.text.toString())
                //user.updatePassword(binding.userPassword.text.toString())
            //}


            user.updateEmail(binding.userEmail.text.toString())
            user.updatePassword(binding.userPassword.text.toString())

            Toast.makeText(view?.context, "Updated Account Info", Toast.LENGTH_SHORT).show()

            view?.findNavController()?.navigate(R.id.action_editAccount_to_navigation_account)
        }



        // Inflate the layout for this fragment
        return binding.root
    }


}