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


        val homeViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        // Initialize Firebase Auth and get current user
        auth = FirebaseAuth.getInstance()
        val firebaseUser = auth.currentUser

        binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (firebaseUser != null) {

            binding.toLogin.setOnClickListener {
                auth.signOut()
                Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                redirectToLogin()
            }

            binding.accountEmail.text = auth.currentUser?.email

            binding.editUser.setOnClickListener {
                view?.findNavController()?.navigate(R.id.action_navigation_account_to_editAccount)

            }

        } else {
            // User not logged in, redirect to login page
            redirectToLogin()
        }

        return root
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
