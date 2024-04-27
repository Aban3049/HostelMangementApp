package com.example.khanboyshostel

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khanboyshostel.databinding.ActivityStudentLogInBinding
import com.google.firebase.auth.FirebaseAuth

class StudentLogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentLogInBinding

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityStudentLogInBinding.inflate(layoutInflater)

        firebaseAuth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.registerBtn.setOnClickListener {
            startActivity(
                Intent(
                    this@StudentLogInActivity,
                    StudentRegistrationActivity::class.java
                )
            )
        }

        binding.loginButton.setOnClickListener {
            validateData()
        }

    }

    private var email = ""
    private var password = ""

    private fun validateData() {

        email = binding.emailEt.text.toString()
        password = binding.passwordEt.text.toString()

        if (email.isEmpty()) {
            binding.emailEt.requestFocus()
            binding.emailEt.error = "Filed Required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEt.requestFocus()
            binding.emailEt.error = "Invalid Email Patteren"
        } else if (password.isEmpty()) {
            binding.passwordEt.requestFocus()
            binding.passwordEt.error = "Field Required "
        } else {
            logInUser()
        }

    }

    private fun logInUser() {
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
            Toast.makeText(this@StudentLogInActivity, "Logged In Successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@StudentLogInActivity,StudentDashboardActivity::class.java))
        }.addOnFailureListener {
            Toast.makeText(this@StudentLogInActivity, "Failed to login due to ${it.message}", Toast.LENGTH_SHORT).show()
        }

    }


}