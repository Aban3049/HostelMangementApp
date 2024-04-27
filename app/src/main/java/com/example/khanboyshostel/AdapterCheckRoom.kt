package com.example.khanboyshostel

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AdapterCheckRoom(private val context: Context, private val roomList: List<Room>) :
    RecyclerView.Adapter<AdapterCheckRoom.RoomViewHolder>() {


    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomNumberTv: TextView = itemView.findViewById(R.id.roomNumberTv)
        val roomCapacityTv: TextView = itemView.findViewById(R.id.roomCapacityTV)
        val bedsTv: TextView = itemView.findViewById(R.id.roomBedsTv)
        val roomStatusTv: TextView = itemView.findViewById(R.id.roomStatusTv)
        val viewBedBtn: Button = itemView.findViewById(R.id.checkBedBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.row_rooms, parent, false)
        return RoomViewHolder(view)

    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {

        val room = roomList[position]
        holder.roomNumberTv.text = context.getString(R.string.room_number, room.roomNumber)
        holder.roomCapacityTv.text = context.getString(R.string.capacity, room.roomCapacity)
        holder.bedsTv.text = context.getString(R.string.beds, room.roomBeds.toString())

        holder.viewBedBtn.setOnClickListener {
            val intent = Intent(context, DisplayBedsUserActivity::class.java)
            intent.putExtra("roomId", room.roomId)
            intent.putExtra("roomNumber", room.roomNumber)
            context.startActivity(intent)
        }



        checkRoomStatus(room.occupied, holder)


    }

    private fun checkRoomStatus(roomStatus: Boolean, holder: AdapterCheckRoom.RoomViewHolder) {

        if (roomStatus == true) {
            holder.roomStatusTv.visibility = View.VISIBLE
            holder.roomStatusTv.text = "Status: OCCUPIED"
        }

    }


}