package com.example.khanboyshostel

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.khanboyshostel.databinding.ActivityAboutBinding
import com.google.firebase.auth.FirebaseAuth

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    private lateinit var imagePager: ViewPager2


    private lateinit var imageList: List<Int>

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityAboutBinding.inflate(layoutInflater)

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

        // view Pager in card view
        imagePager = binding.imagePager

        imageList = listOf(
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3,
            R.drawable.banner4
        )

        val adapter = ImagePagerAdapter(imageList)
        imagePager.adapter = adapter

        startImageSliderTimer()

    }





private fun startImageSliderTimer() {
    val handler = Handler()
    val updateImageSliderTask = object : Runnable {
        override fun run() {
            val currentPage = imagePager.currentItem
            var nextPage = currentPage + 1
            if (nextPage >= imageList.size) {
                nextPage = 0
            }
            imagePager.currentItem = nextPage
            handler.postDelayed(this, 4000)
        }
    }
    handler.postDelayed(updateImageSliderTask, 4000)
}


class ImagePagerAdapter(private val imageList: List<Int>) :
    RecyclerView.Adapter<ImagePagerAdapter.ImagePagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePagerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_pager, parent, false)
        return ImagePagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagePagerViewHolder, position: Int) {
        val imageRes = imageList[position]
        holder.bind(imageRes)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class ImagePagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)


        fun bind(imageRes: Int) {

            try {
                Glide.with(itemView.context)
                    .load(imageRes)
                    .into(imageView)
            } catch (e: Exception) {

            }


        }
    }
}

}
