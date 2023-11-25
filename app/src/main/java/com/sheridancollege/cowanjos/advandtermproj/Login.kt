package com.sheridancollege.cowanjos.advandtermproj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.sheridancollege.cowanjos.advandtermproj.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {

            private lateinit var auth : FirebaseAuth
            private lateinit var binding : ActivityLoginBinding

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
                setContentView(R.layout.activity_login)

                auth = FirebaseAuth.getInstance()

                binding = ActivityLoginBinding.inflate(layoutInflater)
                val view = binding.root
                setContentView(view)


                var editTextEmail : TextInputEditText
                var editTextPassword : TextInputEditText
                var loginButton : Button
                var progressbar : ProgressBar

                binding.apply {
                    loginButton = binding.loginButton
                    progressbar = binding.progressBar
                    editTextEmail = binding.email
                    editTextPassword = binding.password
                }

                //var linkToSignup = binding.toSignUp as TextView

                val linkToSignup = findViewById<TextView>(R.id.toSignUp)

                linkToSignup.setOnClickListener {
                    Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Register::class.java)
                    startActivity(intent)
                    finish()
                }


                loginButton.setOnClickListener {
                    Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
                    progressbar.visibility = View.VISIBLE

                    var email = ""
                    var password = ""

                    if (editTextEmail.text.isNullOrBlank()) {
                        Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
                    } else if (editTextPassword.text.isNullOrBlank()) {
                        Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
                    } else {
                        email = editTextEmail.text.toString()
                        password = editTextPassword.text.toString()
                    }

                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->

                            progressbar.visibility = View.GONE

                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(
                                    baseContext,
                                    "Logging In...",
                                    Toast.LENGTH_SHORT,
                                ).show()

                                intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()

                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(
                                    baseContext,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }

                }
            }
        }