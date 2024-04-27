package com.example.khanboyshostel

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khanboyshostel.databinding.ActivityCheckStudentsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CheckStudentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckStudentsBinding

    private companion object {
        private const val TAG = "CHK_STUDENTS_TAG"
    }

    private lateinit var studentArrayList: ArrayList<modelStudetns>
    private lateinit var adapter: adapterStudents

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityCheckStudentsBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadStudents()


    }

    private fun loadStudents() {

        //init arrayList

        Log.d(TAG, "loadComplainList: ")

        studentArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("registerStudents")
            ref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    //clear list before setting data to it
                   studentArrayList.clear()

                    for (ds in snapshot.children) {
                        //get data
                        val model = ds.getValue(modelStudetns::class.java)
                        //add to list

                        studentArrayList.add(model!!)

                    }

                    //setup adapter
                 adapter = adapterStudents(this@CheckStudentsActivity, studentArrayList)

                    // at last set adapter to recycler view.

                    // at last set adapter to recycler view.

                    binding.studentsRv.adapter = adapter

                    android.os.Handler().postDelayed({
                        binding.progressBar.visibility = View.GONE
                        binding.studentsRv.visibility = View.VISIBLE
                    }, 2000)



                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: DatabaseError: $error")
                }

            })


    }

}