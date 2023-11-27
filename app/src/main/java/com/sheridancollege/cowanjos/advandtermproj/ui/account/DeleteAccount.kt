package com.sheridancollege.cowanjos.advandtermproj.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sheridancollege.cowanjos.advandtermproj.MainActivity
import com.sheridancollege.cowanjos.advandtermproj.R
import com.sheridancollege.cowanjos.advandtermproj.Register
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentDeleteAccountBinding

class DeleteAccount : Fragment() {

    lateinit var binding: FragmentDeleteAccountBinding
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        binding = FragmentDeleteAccountBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.deleteButton.setOnClickListener {
            var user = firebaseAuth.currentUser
            user?.reauthenticate(EmailAuthProvider.getCredential("Example@Email", "Password"))

            user?.delete()

            Toast.makeText(context, "Deleted Account", Toast.LENGTH_SHORT).show()

            firebaseAuth.signOut()

            findNavController().navigate(R.id.action_deleteAccount2_to_navigation_account)
            var intent = Intent(context, Register::class.java)
            startActivity(intent)
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}