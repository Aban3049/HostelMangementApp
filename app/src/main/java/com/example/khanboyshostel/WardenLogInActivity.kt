package com.example.khanboyshostel

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khanboyshostel.databinding.ActivityWardenLogInBinding

class WardenLogInActivity : AppCompatActivity() {

    private lateinit var binding:ActivityWardenLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityWardenLogInBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)


        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.loginButtonWarden.setOnClickListener {
            validateData()
        }

    }

  private  var userName = ""
  private  var password = ""

    private fun validateData(){
        userName = binding.usernameEt.text.toString()
        password = binding.passwordEt.text.toString()

        if (userName.isEmpty()){
            binding.usernameEt.requestFocus()
            binding.usernameEt.error = "Field Required"
        }
        else if (password.isEmpty()){
            binding.passwordEt.requestFocus()
            binding.passwordEt.error = "Field Required"
        }
        else if(userName == "admin" && password == "admin"){
            startActivity(Intent(this@WardenLogInActivity,WardenScreanActivity::class.java))
        }
        else{
            Toast.makeText(this@WardenLogInActivity, "Invalid UserName and Password", Toast.LENGTH_SHORT).show()
        }

    }

}