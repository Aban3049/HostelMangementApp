package com.example.khanboyshostel

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.khanboyshostel.databinding.ActivityMainBinding
import com.example.khanboyshostel.databinding.ActivityMessMenuBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessMenuBinding

    private var ImageUrl = ""

    private companion object {
        private const val TAG = "MESS_MENU_TAG"
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMessMenuBinding.inflate(layoutInflater)

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

        loadMessMenu()

    }

    private fun loadMessMenu() {

        Log.d(TAG, "loadMessMenu: ")

        //Books >bookId >Details
        val ref = FirebaseDatabase.getInstance().getReference("MessMenu")
        ref.child("Image")
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    //get data

                    ImageUrl = "${snapshot.child("imageUrl").value}"


                    loadMenuImage(
                        ImageUrl,
                        binding.progressBar,
                        baseContext,
                        binding.noticeIv
                    )

                    Log.d(TAG, "onDataChange: Successfully")

                }

                override fun onCancelled(error: DatabaseError) {

                    Log.e(TAG, "onCancelled: failed due to $error")

                }

            })

    }

    private fun loadMenuImage(
        imageUrl: String,
        progressBar: ProgressBar,
        context: Context,
        imageView: ImageView
    ) {

        val TAG = "MENU_IMAGE_TAG"

        //using url we can get image
        try {

            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_image_gray)
                .into(imageView)
            imageView.visibility = View.GONE

            android.os.Handler().postDelayed({
                progressBar.visibility = View.GONE
                imageView.visibility = View.VISIBLE
            }, 2000)


        } catch (e: Exception) {
            Log.e(TAG, "onDataChanged", e)
        }
    }

}