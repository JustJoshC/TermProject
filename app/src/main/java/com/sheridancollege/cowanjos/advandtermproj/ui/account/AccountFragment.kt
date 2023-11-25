package com.sheridancollege.cowanjos.advandtermproj.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sheridancollege.cowanjos.advandtermproj.Login
import com.sheridancollege.cowanjos.advandtermproj.MainActivity
import com.sheridancollege.cowanjos.advandtermproj.R
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentAccountBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!

        val scope = CoroutineScope(Dispatchers.Unconfined)

        if(user == null) {

            scope.launch {
                changePage()
            }
        }else {

        }

        binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val textView: TextView = binding.accountEmail
        accountViewModel.text.observe(viewLifecycleOwner) {
            textView.text = user.email
        }

        var logoutButton = binding.toLogin

        logoutButton.setOnClickListener{
            Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
            FirebaseAuth.getInstance().signOut()
            scope.launch {
                changePage()
            }
        }


        return root
        }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    suspend fun changePage(): Unit = withContext(Dispatchers.Unconfined){
        val intent = Intent(view?.context, Login::class.java)
        startActivity(intent)
    }
}
