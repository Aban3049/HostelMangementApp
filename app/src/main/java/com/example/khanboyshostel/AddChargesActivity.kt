package com.example.khanboyshostel

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khanboyshostel.databinding.ActivityAddChargesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddChargesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddChargesBinding

    private lateinit var progressDialog: ProgressDialog


    private companion object {
        private const val TAG = "ADD_CHARGES_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityAddChargesBinding.inflate(layoutInflater)

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

        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Please Wait...")

        loadCharges()

        binding.postCharges.setOnClickListener {
            validateData()
        }

    }

    private var hostelCharges = ""
    private var hostelChargesPerDay = ""
    private var messCharges = ""
    private var messChargesPerDay = ""

    private fun validateData() {

        hostelCharges = binding.hostelChargesEt.text.toString()
        hostelChargesPerDay = binding.hostelChargesPerDayEt.text.toString()
        messCharges = binding.messChargesEt.text.toString()
        messChargesPerDay = binding.messChargesPerDayEt.text.toString()


        addCharges()


    }

    private fun addCharges() {
// upload pdf to firebase db
        Log.d(TAG, "uploading to db")
        progressDialog.show()
        progressDialog.setMessage("Uploading Charges info....")


        val hashMap: HashMap<String, Any> = HashMap()

        val timeStamp = System.currentTimeMillis()

        hashMap["timestamp"] = System.currentTimeMillis()
        hashMap["hostelCharges"] = hostelCharges
        hashMap["hostelChargesPerDay"] = hostelChargesPerDay
        hashMap["messCharges"] = messCharges
        hashMap["messChargesPerDay"] = messChargesPerDay


        val ref = FirebaseDatabase.getInstance().getReference("HostelCharges")
        ref.child("charges")
            .setValue(hashMap)

            .addOnSuccessListener {
                Log.d(TAG, "uploadInfoToDb: uploaded to db")
                progressDialog.dismiss()

                Toast.makeText(
                    this@AddChargesActivity,
                    "Charges Added Successfully",
                    Toast.LENGTH_SHORT
                ).show()


            }

            .addOnFailureListener { e ->
                Log.d(
                    TAG,
                    "uploadToStorage: failed to upload due to ${e.message}"
                )
                progressDialog.dismiss()
                Toast.makeText(
                    this@AddChargesActivity,
                    "Failed to Add Charges to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

    private fun loadCharges() {

        val ref = FirebaseDatabase.getInstance().getReference("HostelCharges")
        ref.child("charges")
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val hostelCharges = snapshot.child("hostelCharges").value as String
                    val hostelChargesPerDay = snapshot.child("hostelChargesPerDay").value as String
                    val messCharges = snapshot.child("messCharges").value as String
                    val messChargesPerDay = snapshot.child("messChargesPerDay").value as String
                    val timestamp = snapshot.child("timestamp").value

                    binding.hostelChargesEt.setText(hostelCharges)
                    binding.hostelChargesPerDayEt.setText(hostelChargesPerDay)
                    binding.messChargesEt.setText(messCharges)
                    binding.messChargesPerDayEt.setText(messChargesPerDay)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

}