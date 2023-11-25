package com.sheridancollege.cowanjos.advandtermproj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sheridancollege.cowanjos.advandtermproj.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var editTextEmail : TextView
        var editTextPassword : TextView
        var signUpButton : Button
        var progressbar : ProgressBar
        var linkToLogin : TextView

        binding.apply {
            editTextEmail = binding.email
            editTextPassword = binding.password
            signUpButton  = binding.signUpButton
            progressbar = binding.progressBar
            linkToLogin = binding.toLogin
        }




        linkToLogin.setOnClickListener{
            var intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }


        signUpButton.setOnClickListener{

            progressbar.visibility = View.VISIBLE

            var email = ""
            var password = ""

            if(editTextEmail.text.isNullOrBlank()){
                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
            }else if(editTextPassword.text.isNullOrBlank()){
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
            }else{
                email = editTextEmail.text.toString()
                password = editTextPassword.text.toString()
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        progressbar.visibility = View.GONE

                        Toast.makeText(
                            baseContext,
                            "Account created.",
                            Toast.LENGTH_SHORT,
                        ).show()


                    } else {

                        progressbar.visibility = View.GONE

                        Toast.makeText(
                            baseContext,
                            "Creation failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }


        }

    }
}