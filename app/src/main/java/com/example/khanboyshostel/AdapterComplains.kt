package com.example.khanboyshostel

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.khanboyshostel.databinding.RowComplainsBinding
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.Locale

class AdapterComplains : RecyclerView.Adapter<AdapterComplains.complainViewHolder> {

    private var context: Context

    private var complainsArrayList: ArrayList<Complains>

    private lateinit var binding: RowComplainsBinding

    constructor(context: Context, adapterComplains: ArrayList<Complains>) : super() {
        this.context = context
        this.complainsArrayList = adapterComplains
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): complainViewHolder {

        binding = RowComplainsBinding.inflate(LayoutInflater.from(context), parent, false)

        return complainViewHolder(binding.root)

    }

    override fun getItemCount(): Int {
        return complainsArrayList.size
    }

    override fun onBindViewHolder(holder: complainViewHolder, position: Int) {

        val model = complainsArrayList[position]

        holder.complainTv.text = model.complain
        holder.userName.text = model.name

        val timestamp = model.timestamp

      val formattedDate =  formatTimestampDate(timestamp)

        holder.dateTv.text = formattedDate

        holder.deleteBtn.setOnClickListener {
            deleteComplain(model.timestamp.toString())
        }

    }


    inner class complainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var userName = binding.nameTv
        var complainTv = binding.complainTv
        var dateTv = binding.dateTv
        var deleteBtn = binding.deleteBtn

    }

   private fun formatTimestampDate(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp

        return DateFormat.format("dd/MM/yyyy", calendar).toString()

    }

   private fun deleteComplain(complainId:String){

        val ref = FirebaseDatabase.getInstance().getReference("Complains")
        ref.child(complainId)
            .removeValue().addOnSuccessListener {
                Toast.makeText(context, "Complain Deleted Successfully", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to delete Complain due to ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }

}