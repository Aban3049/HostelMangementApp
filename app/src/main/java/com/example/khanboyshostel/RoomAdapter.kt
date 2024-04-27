package com.example.khanboyshostel

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase


class RoomAdapter(private val context: Context, private val roomList: List<Room>) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roomNumberTv: TextView = itemView.findViewById(R.id.roomNumberTv)
        val roomCapacityTv: TextView = itemView.findViewById(R.id.roomCapacityTV)
        val bedsTv: TextView = itemView.findViewById(R.id.roomBedsTv)
        val roomStatusTv:TextView = itemView.findViewById(R.id.roomStatusTv)
        val viewBedBtn: Button = itemView.findViewById(R.id.checkBedBtn)
        val deleteBtn:ImageButton = itemView.findViewById(R.id.deleteBtn)
        val markOccupiedBtn:Button = itemView.findViewById(R.id.OccupiedBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = roomList[position]
        holder.roomNumberTv.text = context.getString(R.string.room_number, room.roomNumber)
        holder.roomCapacityTv.text = context.getString(R.string.capacity, room.roomCapacity)
        holder.bedsTv.text = context.getString(R.string.beds, room.roomBeds.toString())

        holder.viewBedBtn.setOnClickListener {
            val intent = Intent(context,DisplayBedsActivity::class.java)
            intent.putExtra("roomId",room.roomId)
            intent.putExtra("roomNumber",room.roomNumber)
            context.startActivity(intent)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context,DisplayBedsActivity::class.java)
            intent.putExtra("roomId",room.roomId)
            intent.putExtra("roomNumber",room.roomNumber)
            context.startActivity(intent)
        }

        holder.markOccupiedBtn.setOnClickListener {

            val hashMap = HashMap<String,Any>()
            hashMap["occupied"] = true

            val ref = FirebaseDatabase.getInstance().getReference("rooms")
            ref.child(room.roomId).updateChildren(hashMap).addOnSuccessListener {
                Toast.makeText(context, "Marked As Occupied", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Failed To Mark as Occupied due to ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        holder.deleteBtn.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("rooms")
            ref.child(room.roomId)
                .removeValue().addOnSuccessListener {
                    Toast.makeText(context, "Room Deleted Successfully", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to delete due to ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }



        checkRoomStatus(room.occupied,holder,room.roomId)
        
    }

    private fun checkRoomStatus(roomStatus:Boolean,holder: RoomViewHolder,roomId:String){

        if (roomStatus == true){
            holder.roomStatusTv.visibility = View.VISIBLE
            holder.roomStatusTv.text = "Status: OCCUPIED"
            holder.markOccupiedBtn.text = "Mark As UnOccupied"
            holder.markOccupiedBtn.setOnClickListener {
                val hashMap = HashMap<String,Any>()
                hashMap["occupied"] = false

                val ref = FirebaseDatabase.getInstance().getReference("rooms")
                ref.child(roomId).updateChildren(hashMap).addOnSuccessListener {
                    Toast.makeText(context, "Marked As UnOccupied", Toast.LENGTH_SHORT).show()
                    holder.markOccupiedBtn.text = "Mark As Occupied"
                    holder.roomStatusTv.visibility = View.GONE
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed To Mark as UNOccupied due to ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return roomList.size
    }

}
