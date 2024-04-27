package com.example.khanboyshostel

import android.os.Bundle
import android.text.format.DateFormat
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khanboyshostel.databinding.ActivityChargesBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar
import java.util.Locale

class ChargesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChargesBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityChargesBinding.inflate(layoutInflater)

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

        loadCharges()

    }

    private fun loadCharges() {

        val ref = FirebaseDatabase.getInstance().getReference("HostelCharges")
        ref.child("charges")
            .addListenerForSingleValueEvent(object:ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {

                    binding.hostelChargeMTv.text = snapshot.child("hostelCharges").value.toString()
                    binding.hostelChargesPdTv.text = snapshot.child("hostelChargesPerDay").value.toString()
                    binding.messChargesmTv.text = snapshot.child("messCharges").value.toString()
                    binding.messChargesPdTv.text = snapshot.child("messChargesPerDay").value.toString()
                    val timestamp = snapshot.child("timestamp").value

                 val formattedTimeStamp =   formatTimestampDate(timestamp as Long)
                    binding.dateTv.text = formattedTimeStamp

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun formatTimestampDate(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp

        return DateFormat.format("dd/MM/yyyy", calendar).toString()

    }

}