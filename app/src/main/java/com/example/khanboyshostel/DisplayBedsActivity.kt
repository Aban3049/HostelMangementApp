package com.example.khanboyshostel

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khanboyshostel.databinding.ActivityDisplayBedsBinding
import com.google.firebase.database.FirebaseDatabase

class DisplayBedsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDisplayBedsBinding

    private var roomId: String = ""

    private var roomNumber:String = ""

    private lateinit var bedAdapter: BedAdapter

    private companion object {
        private const val TAG = "Display_Beds"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityDisplayBedsBinding.inflate(layoutInflater)

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

        roomId = intent.getStringExtra("roomId")!!

        roomNumber = intent.getStringExtra("roomNumber")!!

        binding.roomNumberTv.text = getString(R.string.room_no, roomNumber)

        bedAdapter = BedAdapter(emptyList(), binding.bedRecyclerView)

        Log.d(TAG, "onCreate: $roomId")


        val roomRef = FirebaseDatabase.getInstance().getReference("rooms").child(roomId)
        roomRef.get().addOnSuccessListener { roomSnapshot ->
            val beds = roomSnapshot.child("beds").value as List<*> // Cast to List<*> initially

            val processedBeds = mutableListOf<Bed>() // Create an empty mutable list of Bed

            // Iterate through retrieved elements and attempt casting
            for (bedObject in beds) {
                if (bedObject is Map<*, *>) { // Check if it's a Map (potential HashMap)
                    val bed = convertMapToBed(bedObject as Map<String, Any>) // Cast and convert
                    if (bed != null) {
                        processedBeds.add(bed)
                    }
                }
            }

            // Update adapter with the processed bed list
            updateBeds(processedBeds)
        }


        //Fetch bed information


        // Handle button clicks for bed operations
        binding.assignBedButton.setOnClickListener {
            val selectedBed = bedAdapter.getSelectedBed()
            if (selectedBed != null) {
                // Assign the selected bed using Firebase Database
                assignBed(selectedBed)
            } else {
                Toast.makeText(this, "Please select a bed to assign!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.deallocateBedButton.setOnClickListener {
            // Deallocate a bed using FieldValue.arrayRemove()
            val selectedBed = bedAdapter.getSelectedBed()
            if (selectedBed != null) {
                // Deallocate the selected bed using Firebase Database
                deallocateBed(selectedBed)
            } else {
                Toast.makeText(this, "Please select a bed to deallocate!", Toast.LENGTH_SHORT)
                    .show()
            }

        }


    }

    private fun convertMapToBed(map: Map<String, Any>): Bed? {
        // Extract properties from the map and create a Bed object
        val bedId = map["bedId"] as? String
        val bedNumber = map["bedNumber"] as? String
        val occupied = map["occupied"] as? Boolean

        Log.d(TAG, "convertMapToBed: bedId:$bedId : bedNumber:$bedNumber: isOccupied:$occupied")


        if (bedId != null && bedNumber != null && occupied != null) {
            return Bed(bedId, bedNumber, occupied)
        } else {
            Log.w(TAG, "Error converting map to Bed: missing required properties")
            return null
        }
    }

    private fun updateBeds(beds: List<Bed>) {
        // Update bed adapter with the filtered and cast bed list
        bedAdapter.updateBeds(beds)
        binding.bedRecyclerView.adapter = bedAdapter

        if (binding.bedRecyclerView.adapter != null) {
//            Toast.makeText(this@DisplayBedsActivity, "Adapter is not Null", Toast.LENGTH_SHORT)
            Log.e(TAG, "updateBeds: Adapter Checking: Adapter not Null", )
        }

    }

    private fun assignBed(bed: Bed) {
        // Implement logic to update the bed's "occupied" status to true in Firebase Database
        // Here's an example using Firebase Database references:

        val roomRef = FirebaseDatabase.getInstance().getReference("rooms").child(roomId)
        roomRef.child("beds").child(bed.bedId).child("occupied").setValue(true)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Bed assigned successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Failed to assign bed: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun deallocateBed(bed: Bed) {
        // Implement logic to update the bed's "occupied" status to false in Firebase Database
        // Here's an example using FieldValue.arrayRemove():

        val roomRef = FirebaseDatabase.getInstance().getReference("rooms").child(roomId)
        roomRef.child("beds").child(bed.bedId).child("occupied").setValue(false)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Bed deallocated successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Failed to deallocate bed: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


}