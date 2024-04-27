package com.example.khanboyshostel

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khanboyshostel.databinding.ActivityDisplayBedsUserBinding
import com.google.firebase.database.FirebaseDatabase

class DisplayBedsUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDisplayBedsUserBinding

    private var roomId: String = ""

    private var roomNumber: String = ""

    private lateinit var adapterBedUser: AdapterBedUser

    private companion object {
        private const val TAG = "Display_Beds_User"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityDisplayBedsUserBinding.inflate(layoutInflater)

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

        adapterBedUser = AdapterBedUser(emptyList(), binding.bedRecyclerView)

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


    }

    private fun convertMapToBed(map: Map<String, Any>): Bed? {
        // Extract properties from the map and create a Bed object
        val bedId = map["bedId"] as? String
        val bedNumber = map["bedNumber"] as? String
        val occupied = map["occupied"] as? Boolean

        Log.d(TAG,
            "convertMapToBed: bedId:$bedId : bedNumber:$bedNumber: isOccupied:$occupied"
        )


        if (bedId != null && bedNumber != null && occupied != null) {
            return Bed(bedId, bedNumber, occupied)
        } else {
            Log.w(TAG, "Error converting map to Bed: missing required properties"
            )
            return null
        }
    }

    private fun updateBeds(beds: List<Bed>) {
        // Update bed adapter with the filtered and cast bed list
        adapterBedUser.updateBeds(beds)
        binding.bedRecyclerView.adapter = adapterBedUser

        if (binding.bedRecyclerView.adapter != null) {
//            Toast.makeText(this@DisplayBedsActivity, "Adapter is not Null", Toast.LENGTH_SHORT)
            Log.e(TAG, "updateBeds: Adapter Checking: Adapter not Null")
        }

    }

}