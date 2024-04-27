package com.example.khanboyshostel

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khanboyshostel.databinding.ActivityRegisterStudentBinding
import com.google.firebase.database.FirebaseDatabase

class RegisterStudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterStudentBinding

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityRegisterStudentBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Saving Student Info")

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.registerButton.setOnClickListener {
            registerStudent()
        }


    }

    private var studentName = ""
    private var fatherName = ""
    private var studentCellNo = ""
    private var fatherCellNo = ""
    private var Address = ""
    private var roomNo = ""


    private fun registerStudent() {

        studentName = binding.nameEt.text.toString()
        fatherName = binding.fahterNameEt.text.toString()
        studentCellNo = binding.studentCellEt.text.toString()
        fatherCellNo = binding.fatherNumberEt.text.toString()
        Address = binding.addressEt.text.toString()
        roomNo = binding.roomNumberEt.text.toString()

        if (studentName.isEmpty()) {
            binding.nameEt.requestFocus()
            binding.nameEt.error = "Field Required"
        } else if (fatherName.isEmpty()) {
            binding.fahterNameEt.requestFocus()
            binding.fahterNameEt.error = "Field Required"
        } else if (studentCellNo.isEmpty()) {
            binding.studentCellEt.requestFocus()
            binding.studentCellEt.error = "Field Required"
        } else if (fatherCellNo.isEmpty()) {
            binding.fahterNameEt.requestFocus()
            binding.fatherNumberEt.error = "Field Required"
        } else if (Address.isEmpty()) {
            binding.addressEt.requestFocus()
            binding.addressEt.error = "Field Required"
        } else if (roomNo.isEmpty()) {
            binding.roomNumberEt.requestFocus()
            binding.roomNumberEt.error = "Field Required"
        } else {
            saveStudentInfo()
        }

    }


    private fun saveStudentInfo() {

        progressDialog.show()

        val timeStamp = System.currentTimeMillis()

        val hashMap = HashMap<String, Any>()
        hashMap["studentName"] = "$studentName"
        hashMap["fatherName"] = "$fatherName"
        hashMap["studentCellNo"] = "$studentCellNo"
        hashMap["fatherCellNo"] = "$fatherCellNo"
        hashMap["Address"] = "$Address"
        hashMap["roomNumber"] = "$roomNo"
        hashMap["id"] = "$timeStamp"
        hashMap["timestamp"] = timeStamp

        val ref = FirebaseDatabase.getInstance().getReference("registerStudents")
        ref.child("$timeStamp")
            .setValue(hashMap).addOnSuccessListener {
            Toast.makeText(
                this@RegisterStudentActivity,
                "Student Added Successfully",
                Toast.LENGTH_SHORT
            ).show()
            progressDialog.dismiss()
        }.addOnFailureListener {
            Toast.makeText(
                this@RegisterStudentActivity,
                "Failed to Add Students due to ${it.message}",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

}