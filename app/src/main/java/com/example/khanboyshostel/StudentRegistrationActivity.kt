package com.example.khanboyshostel

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khanboyshostel.databinding.ActivityStudentRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class StudentRegistrationActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog

    private lateinit var binding: ActivityStudentRegistrationBinding

    private companion object {
        private const val TAG = "SDNT_REG_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityStudentRegistrationBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.registerButton.setOnClickListener {
            validateData()
        }

        binding.loginBtn.setOnClickListener {
            startActivity(
                Intent(
                    this@StudentRegistrationActivity,
                    StudentLogInActivity::class.java
                )
            )
        }

    }

    private var name = ""
    private var email = ""
    private var password = ""


    private fun validateData() {

        name = binding.nameEt.text.toString()
        email = binding.emailEt.text.toString()
        password = binding.passwordEt.text.toString()



        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEt.error = "Invalid Email Pattern"
            binding.emailEt.requestFocus()
        } else if (password.isEmpty()) {
            binding.passwordEt.error = "Enter Password"
            binding.passwordEt.requestFocus()
        } else if (name.isEmpty()) {
            binding.nameEt.requestFocus()
            binding.nameEt.error = "Enter Name"
        } else {
            registerUser()
        }
    }

    private fun registerUser() {

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog.setMessage("Creating Account")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.setMessage("Registered Successfully")
                Toast.makeText(
                    this@StudentRegistrationActivity,
                    "Registered Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                updateUserInfo()
                progressDialog.dismiss()
                Log.d(TAG, "register user: Register Success")

            }.addOnFailureListener { e ->
                Log.e(TAG, "register user $e")
                progressDialog.dismiss()
                Toast.makeText(
                    this@StudentRegistrationActivity,
                    "Failed to create Account due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

    private fun updateUserInfo() {
        firebaseAuth = FirebaseAuth.getInstance()

        Log.d(TAG, "updateUserInfo")
        progressDialog.setMessage("Saving User Info")

        val timeStamp = System.currentTimeMillis()
        val registerUserEmail = firebaseAuth.currentUser!!.email
        val registeredUserUid = firebaseAuth.uid

        val hashMap = HashMap<String, Any>()
        hashMap["name"] = "$name"
        hashMap["userType"] = "Email"
        hashMap["timestamp"] = timeStamp
        hashMap["email"] = "$registerUserEmail"
        hashMap["uid"] = "$registeredUserUid"
        hashMap["userMode"] = "User"

        val reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.child(registeredUserUid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "updateUserInfo: User registered...")
                progressDialog.dismiss()
                // Start MainActivity
                startActivity(
                    Intent(
                        this@StudentRegistrationActivity,
                        StudentLogInActivity::class.java
                    )
                )
                finishAffinity() // finish current and back activities from back stack
            }.addOnFailureListener { e ->
                Log.e(TAG, "updateUserInfo", e)
                Toast.makeText(
                    this@StudentRegistrationActivity,
                    "Failed to save use info due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

}