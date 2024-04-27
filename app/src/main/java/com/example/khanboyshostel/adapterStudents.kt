package com.example.khanboyshostel

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.khanboyshostel.databinding.RowStudentBinding
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.Locale

class adapterStudents : RecyclerView.Adapter<adapterStudents.studentsViewHolder> {

    private var context: Context

    private var studentsArrayList: ArrayList<modelStudetns>

    private lateinit var binding: RowStudentBinding

    constructor(context: Context, studentsArrayList: ArrayList<modelStudetns>) : super() {
        this.context = context
        this.studentsArrayList = studentsArrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): studentsViewHolder {

        binding = RowStudentBinding.inflate(LayoutInflater.from(context), parent, false)

        return studentsViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return studentsArrayList.size
    }

    override fun onBindViewHolder(holder: studentsViewHolder, position: Int) {

        var model = studentsArrayList[position]

        holder.studentNameTv.text = model.studentName
        holder.fatherNameTv.text = "Father/Guardian Name: ${model.fatherName}"
        holder.studentCellNoTv.text = "Student Cell No: ${model.studentCellNo}"
        holder.fatherCellNoTv.text = "Father/Guardian Cell No: ${model.fatherCellNo}"
        holder.addressTv.text ="Address: ${model.Address}"
        holder.roomNoTv.text = "Room No: ${model.roomNumber}"


        val timestamp = model.timestamp

        val formattedDate = formatTimestampDate(timestamp)

        holder.dateTv.text = formattedDate

        holder.deleteBtn.setOnClickListener {
            deleteComplain(model.id)
        }


    }


    inner class studentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var studentNameTv = binding.studentNameTv
        var fatherNameTv = binding.fatherNameTV
        var studentCellNoTv = binding.studentCellNoTv
        var fatherCellNoTv = binding.fatherCellNoTv
        var addressTv = binding.addressTv
        var roomNoTv = binding.roomNoTv
        var deleteBtn = binding.deleteBtn
        var dateTv = binding.dateTv

    }

    private fun formatTimestampDate(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp

        return DateFormat.format("dd/MM/yyyy", calendar).toString()

    }

    private fun deleteComplain(complainId: String) {

        val ref = FirebaseDatabase.getInstance().getReference("registerStudents")
        ref.child(complainId)
            .removeValue().addOnSuccessListener {
                Toast.makeText(context, "Student Deleted Successfully", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(
                    context,
                    "Failed to delete Student due to ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }
}