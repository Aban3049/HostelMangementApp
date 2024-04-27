package com.example.khanboyshostel

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khanboyshostel.databinding.ActivityWardenScreanBinding

class WardenScreanActivity : AppCompatActivity() {

    private lateinit var binding:ActivityWardenScreanBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityWardenScreanBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.addRoom.setOnClickListener {
            startActivity(Intent(this@WardenScreanActivity,AddRoomActivity::class.java))
        }
        binding.checkRoom.setOnClickListener {
            startActivity(Intent(this@WardenScreanActivity,CheckRoomActivity::class.java))
        }
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.addMessMenu.setOnClickListener {
            startActivity(Intent(this@WardenScreanActivity,AddMessMenuActivity::class.java))
        }
        binding.checkComplains.setOnClickListener {
            startActivity(Intent(this@WardenScreanActivity,CheckComplainActivity::class.java))
        }
        binding.addCharges.setOnClickListener {
            startActivity(Intent(this@WardenScreanActivity,AddChargesActivity::class.java))
        }

        binding.checkChargesBtn.setOnClickListener {
            startActivity(Intent(this@WardenScreanActivity,ChargesActivity::class.java))
        }

        binding.addStudent.setOnClickListener {
            startActivity(Intent(this@WardenScreanActivity,RegisterStudentActivity::class.java))
        }

        binding.checkStudents.setOnClickListener {
            startActivity(Intent(this@WardenScreanActivity,CheckStudentsActivity::class.java))
        }

    }


}