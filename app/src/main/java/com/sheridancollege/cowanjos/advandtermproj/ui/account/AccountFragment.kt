package com.sheridancollege.cowanjos.advandtermproj.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sheridancollege.cowanjos.advandtermproj.*
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentAccountBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize Firebase Auth and get current user
        auth = FirebaseAuth.getInstance()
        val firebaseUser = auth.currentUser

        // Get DAO and create Repository
        val accountDao = AppDatabase.getInstance(requireContext()).accountDao()
        val accountRepository = AccountRepository(accountDao)

        // Use custom ViewModelFactory
        val viewModelFactory = AccountViewModelFactory(accountRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AccountViewModel::class.java)

        binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (firebaseUser != null) {
            // Sync Firebase user data with Room database
            syncUserDataWithRoom(firebaseUser)
        } else {
            // User not logged in, redirect to login page
            redirectToLogin()
        }

        binding.toLogin.setOnClickListener {
            auth.signOut()
            Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
            redirectToLogin()
        }

        binding.accountEmail.text = auth.currentUser?.email

        binding.editUser.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_navigation_account_to_editAccount)

        }

        return root
    }

    private fun syncUserDataWithRoom(firebaseUser: FirebaseUser) {
        val username = firebaseUser.email ?: firebaseUser.uid // Use email or UID as username
        val password = "firebase" // Dummy password as Firebase handles the real authentication

        lifecycleScope.launch(Dispatchers.IO) {
            val existingUserId = viewModel.getUserIdByUsername(username)
            if (existingUserId == null) {
                // User not in Room database, insert new account
                val newAccount = Account(userId = 0, username = username, password = password)
                viewModel.insertAccount(newAccount)
            } else {
                // User already exists, update existing account if needed
                viewModel.updateAccount(existingUserId, username, password)
            }
        }
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
