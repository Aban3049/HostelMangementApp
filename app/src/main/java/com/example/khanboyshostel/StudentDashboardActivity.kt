package com.example.khanboyshostel

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khanboyshostel.databinding.ActivityStudentDashboardBinding

class StudentDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityStudentDashboardBinding.inflate(layoutInflater)

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

        binding.messMenuBtn.setOnClickListener {
            startActivity(Intent(this@StudentDashboardActivity, MessMenuActivity::class.java))
        }
        binding.addComplainBtn.setOnClickListener {
            startActivity(Intent(this@StudentDashboardActivity,AddComplaintActivity::class.java))
        }

        binding.checkChargesBtn.setOnClickListener {
            startActivity(Intent(this@StudentDashboardActivity,ChargesActivity::class.java))
        }

        binding.checkRoomBtn.setOnClickListener {
            startActivity(Intent(this@StudentDashboardActivity,CheckRoomsStudentActivity::class.java))
        }
        binding.aboutBtn.setOnClickListener {
            startActivity(Intent(this@StudentDashboardActivity,AboutActivity::class.java))
        }

    }


}