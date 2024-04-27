package com.example.khanboyshostel

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khanboyshostel.databinding.ActivityAddRoom2Binding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddRoomActivity : AppCompatActivity() {

private lateinit var binding:ActivityAddRoom2Binding

private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityAddRoom2Binding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mDatabase = FirebaseDatabase.getInstance().reference

       binding.addRoomBtn.setOnClickListener {
           validateData()
       }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()

        }

    }

    private var roomNumber = ""
    private var roomBeds = ""
    private var roomCapacity = ""

    private fun validateData(){

        roomNumber = binding.roomNumberEt.text.toString()
        roomBeds = binding.bedsEt.text.toString()
        roomCapacity = binding.capacityEt.text.toString()

        if (roomNumber.isEmpty()){
            binding.roomNumberEt.requestFocus()
            binding.roomNumberEt.error = "Field Required"
        }else if (roomBeds.isEmpty()){
            binding.roomNumberEt.requestFocus()
            binding.roomNumberEt.error = "Field Required"
        }else if (roomCapacity.isEmpty()){
            binding.capacityEt.requestFocus()
            binding.capacityEt.error = "Field Required"
        }else{
            addRoomToDatabase(roomNumber,roomBeds.toInt(),roomCapacity)
        }

    }

    private fun addRoomToDatabase(roomNumber: String, roomBeds: Int, roomCapacity: String) {
        // Create a unique key for the new room
        val roomId = mDatabase.child("rooms").push().key

        val numberOfBeds = roomBeds

// Create an empty list to hold the beds
        val bedsList: MutableList<Bed> = mutableListOf()

// Generate beds and add them to the list
        for (i in 0..<numberOfBeds) {
            val bedId = "$i"
            val bedNumber = "B$i"
            val isOccupied = false // Set the initial occupancy status as needed

            val bed = Bed(bedId, bedNumber, isOccupied)
            bedsList.add(bed)
        }

        // Create a Room object
        val room = Room(roomId!!, roomNumber, roomBeds, roomCapacity, false,bedsList) // Assuming 'Room' is a model class

        // Add the room to the database under "rooms" node with the generated key
        mDatabase.child("rooms").child(roomId).setValue(room)
            .addOnSuccessListener(OnSuccessListener {
                // Room added successfully
                Toast.makeText(this@AddRoomActivity, "Room added to database", Toast.LENGTH_SHORT).show()

            })
            .addOnFailureListener(OnFailureListener { e ->
                // Failed to add room
                Toast.makeText(this@AddRoomActivity, "Failed to add room to database", Toast.LENGTH_SHORT).show()
            })
    }

//    fun updateBedOccupiedStatus(roomId: String, bedId: String, isOccupied: Boolean) {
//        val roomRef = mDatabase.child("rooms").child(roomId)
//        val bedRef = roomRef.child("beds").child(bedId)
//
//        bedRef.child("isOccupied").setValue(isOccupied)
//            .addOnSuccessListener {
//                // Occupied status updated successfully
//                Toast.makeText(this@AddRoomActivity, "Occupied Status Updated Successfully", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener { exception ->
//                // Failed to update occupied status
//            }
//    }


    }
