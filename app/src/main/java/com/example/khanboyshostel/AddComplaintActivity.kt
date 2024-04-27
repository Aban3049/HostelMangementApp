package com.example.khanboyshostel

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khanboyshostel.databinding.ActivityAddComplaintBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddComplaintActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddComplaintBinding

    private lateinit var progressDialog: ProgressDialog

    private lateinit var firebaseAuth: FirebaseAuth

    private companion object {
        private const val TAG = "ADD_COMP_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        firebaseAuth = FirebaseAuth.getInstance()

        binding = ActivityAddComplaintBinding.inflate(layoutInflater)

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

        binding.publishComplain.setOnClickListener {
            validateData()
        }

    }

    private var name = ""
    private var complaint = ""

    private fun validateData() {
        name = binding.nameEt.text.toString()
        complaint = binding.complainEt.text.toString()

        if (name.isEmpty()) {
            binding.nameEt.requestFocus()
            binding.nameEt.error = "Field Required"
        } else if (complaint.isEmpty()) {
            binding.complainEt.requestFocus()
            binding.complainEt.error = "Field Required"
        } else {
            submitComplain()
        }

    }

    private fun submitComplain() {
// upload pdf to firebase db
        Log.d(TAG, "uploading to db")
        progressDialog.show()
        progressDialog.setMessage("Uploading Complain info....")


        val hashMap: HashMap<String, Any> = HashMap()

        val timeStamp = System.currentTimeMillis()

        hashMap["timestamp"] = System.currentTimeMillis()
        hashMap["name"] = name
        hashMap["complain"] = complaint
        hashMap["uid"] = firebaseAuth.currentUser!!.uid


        val ref = FirebaseDatabase.getInstance().getReference("Complains")
        ref.child("$timeStamp")
            .setValue(hashMap)

            .addOnSuccessListener {
                Log.d(TAG, "uploadInfoToDb: uploaded to db")
                progressDialog.dismiss()

                Toast.makeText(
                    this@AddComplaintActivity,
                    "Complain Added Successfully",
                    Toast.LENGTH_SHORT
                ).show()


            }

            .addOnFailureListener { e ->
                Log.d(TAG, "uploadToStorage: failed to upload due to ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(
                    this@AddComplaintActivity,
                    "Failed to Submit Complain to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }


}