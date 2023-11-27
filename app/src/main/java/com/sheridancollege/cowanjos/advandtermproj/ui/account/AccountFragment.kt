package com.sheridancollege.cowanjos.advandtermproj.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.sheridancollege.cowanjos.advandtermproj.*
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        view?.findNavController()?.saveState()

        val accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        // Initialize Firebase Auth and get current user
        auth = FirebaseAuth.getInstance()
        val firebaseUser = auth.currentUser

        binding = FragmentAccountBinding.inflate(inflater, container, false)

        if (firebaseUser == null) {

            redirectToLogin()

        } else {
            binding.accountEmail.text = auth.currentUser?.email
        }


        binding.toLogin.setOnClickListener {
            auth.signOut()
            Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
            redirectToLogin()
        }

        binding.editUser.setOnClickListener {
            Toast.makeText(context, "Edit Account", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_navigation_account_to_editAccount)

        }

        binding.deleteAccount.setOnClickListener {
            Toast.makeText(context, "Delete Account", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_navigation_account_to_deleteAccount2)
        }

        binding.toReminders.setOnClickListener {
            Toast.makeText(context, "Reminders", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_navigation_account_to_reminderFragment)

        }

        return binding.root
    }

    private fun redirectToLogin() {
        val intent = Intent(activity, Login::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
