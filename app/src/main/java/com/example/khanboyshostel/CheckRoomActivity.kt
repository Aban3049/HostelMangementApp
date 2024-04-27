package com.example.khanboyshostel

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.khanboyshostel.databinding.ActivityCheckRoomBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CheckRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckRoomBinding

    private lateinit var mDatabase: DatabaseReference

    private lateinit var roomAdapter: RoomAdapter

    private lateinit var roomList: List<Room>


    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityCheckRoomBinding.inflate(layoutInflater)

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

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().reference


        // Load rooms from Firebase
        loadRooms()

    }


    private fun loadRooms() {
        // Initialize empty room list
        val roomList = mutableListOf<Room>()

        mDatabase.child("rooms").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomList.clear() // Clear the existing roomList

                for (roomSnapshot in snapshot.children) {
                    val room = Room.fromMap(roomSnapshot.value as Map<String, Any>)
                    roomList.add(room)
                }

                roomAdapter = RoomAdapter(this@CheckRoomActivity, roomList)
                // Create adapter after data is available
                binding.recyclerViewRooms.layoutManager =
                    LinearLayoutManager(this@CheckRoomActivity)
                binding.recyclerViewRooms.adapter = roomAdapter

                android.os.Handler().postDelayed({
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerViewRooms.visibility = View.VISIBLE
                }, 2000)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@CheckRoomActivity,
                    "Failed to fetch rooms due to $error",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }


}







