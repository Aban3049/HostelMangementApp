package com.example.khanboyshostel

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.khanboyshostel.databinding.ActivityCheckComplainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CheckComplainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckComplainBinding

    private companion object {
        private const val TAG = "CHK_COMPLN_TAG"
    }

    private lateinit var complainArrayList: ArrayList<Complains>
    private lateinit var adapterArrayList: AdapterComplains

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityCheckComplainBinding.inflate(layoutInflater)

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

        loadComplains()

    }

    private fun loadComplains() {

        //init arrayList

        Log.d(TAG, "loadComplainList: ")

        complainArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Complains")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    //clear list before setting data to it
                    complainArrayList.clear()

                    for (ds in snapshot.children) {
                        //get data
                        val model = ds.getValue(Complains::class.java)
                        //add to list

                        complainArrayList.add(model!!)

                    }

                    //setup adapter
                    adapterArrayList =
                        AdapterComplains(this@CheckComplainActivity, complainArrayList)


                    // at last set adapter to recycler view.

                    // at last set adapter to recycler view.

                    binding.complainRv.adapter = adapterArrayList

                    android.os.Handler().postDelayed({
                        binding.progressBar.visibility = View.GONE
                        binding.complainRv.visibility = View.VISIBLE
                    }, 2000)



                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: DatabaseError: $error")
                }

            })


    }


}