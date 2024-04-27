package com.example.khanboyshostel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterBedUser(private var bedList: List<Bed>, private val recyclerView: RecyclerView) :
    RecyclerView.Adapter<AdapterBedUser.BedViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BedViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_bed_user, parent, false)
        return BedViewHolder(view)

    }

    override fun getItemCount(): Int {
        return bedList.size
    }


    override fun onBindViewHolder(holder: BedViewHolder, position: Int) {

        if (bedList.size > position) {
            val bed = bedList[position]
            val bedIndex = position + 1  // Adjust for 1-based indexing

            // Extract information from the Bed object
            val bedStatusText = if (bed.occupied == false) {
                "Available" // Customize based on your status values
            } else {
                "Occupied" // Customize based on your status values
            }
            holder.textViewBedInfo.text = "Bed $bedIndex: ${bed.bedNumber} ($bedStatusText)"


        } else {
            // Handle case where index is out of bounds or data is unexpected
            Log.w("BedAdapter", "Invalid bed data at position $position")
        }

    }


    fun updateBeds(beds: List<Bed>) {
        // Update the internal bed list
        this.bedList = beds
        notifyDataSetChanged() // Notify adapter about data change
    }

    inner class BedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textViewBedInfo: TextView = itemView.findViewById(R.id.textViewBedInfo)

    }


}