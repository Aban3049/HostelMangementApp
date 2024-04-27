package com.example.khanboyshostel

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.khanboyshostel.databinding.ActivityAddMessMenuBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class AddMessMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMessMenuBinding

    private lateinit var progressDialog: ProgressDialog


    private lateinit var firebaseAuth: FirebaseAuth

    private var imageUri: Uri? = null

    private companion object {
        private const val TAG = "ADD_MESS_TAG"
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityAddMessMenuBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.addImageBtn.setOnClickListener {
            showImagePickOptions()
        }

        binding.closeBtn.setOnClickListener {
            try {

                Log.d(TAG, "onBindViewHolder: imageUri: $imageUri")

                Glide.with(this)
                    .clear(binding.imageIv)
            } catch (e: Exception) {
                Log.e(TAG, "onBindViewHolder:", e)
            }

        }

        binding.postBtn.setOnClickListener {
            validateData()
        }

    }

    private var uploadImageUrl = ""

    private fun loadImages() {
        Log.d(TAG, "loadImages: ")
        try {

            Log.d(TAG, "onBindViewHolder: imageUri: $imageUri")

            Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.ic_image_gray)
                .into(binding.imageIv)
        } catch (e: Exception) {
            Log.e(TAG, "onBindViewHolder:", e)
        }

    }

    private fun showImagePickOptions() {
        Log.d(TAG, "showImagePickOptions: ")

        //s how popUp on Button
        val popupMenu = PopupMenu(this, binding.addImageBtn)
        popupMenu.menu.add(Menu.NONE, 1, 1, "Camera")
        popupMenu.menu.add(Menu.NONE, 2, 2, "Gallery")
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { item ->
// get the id of the item clicked in popup menu
            val itemId = item.itemId
            // check which item is clicked from popUp menu, 1=Camera. 2=Gallery as we defined
            if (itemId == 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                    val cameraPermission = arrayOf(android.Manifest.permission.CAMERA)
                    requestCameraPermission.launch(cameraPermission)
                } else {
                    // Device version is TIRAMISU,We need Storage permission to launch Camera
                    val cameraPermissions = arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    requestCameraPermission.launch(cameraPermissions)
                }
            } else if (itemId == 2) {
                // Gallery is clicked we need to check if we have permission of Storage before launching Gallery to Pick image
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    pickImageGallery()
                } else {
                    // Device version is TIRAMISU,We need Storage permission to launch Gallery
                    val storagePermission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    requestStoragePermission.launch(storagePermission)
                }
            }

            true
        }
    }

    private val requestStoragePermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d(TAG, "requestStoragePermission: isGranted: $isGranted")

        if (isGranted) {
            // Let's check if permission is granted or not
            pickImageGallery()
        } else {
            //Storage Permission denied, we can't launch gallery to pick images
            Toast.makeText(this, "Storage Permission denied....", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        Log.d(TAG, "requestCameraPermission: result: $result")
        //let's check if permissions are granted or not
        var areAllGranted = true
        for (isGranted in result.values) {
            areAllGranted = areAllGranted && isGranted
        }
        if (areAllGranted) {
            //All Permission Camera,Storage are granted,we can now launch camera to capture image
            pickImageCamera()
        } else {
            //Camera or Storage or Both permission are denied, Can't launch camera to capture image
            Toast.makeText(
                this,
                "Camera or Storage or both permission denied...",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun pickImageGallery() {
        Log.d(TAG, "pickImageGallery:")
        val intent = Intent(Intent.ACTION_PICK)
        // we only want to pick Images
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private fun pickImageCamera() {
        Log.d(TAG, "pickImageCamera:")

        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, "TEMP_IMAGE_TITLE")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "TEMP_IMAGE_DESCRIPTION")
        // Uri of image to be captured from camera
        imageUri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        //Intent to launch Camera
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(intent)
    }

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d(TAG, "galleryActivityResultLauncher :")
        // Check if image is picked or not
        if (result.resultCode == Activity.RESULT_OK) {

            //get data from result param
            val data = result.data
            //get uri of image picked
            imageUri = data!!.data
            Log.d(TAG, "galleryActivityResultLauncher: imageUri: $imageUri")

            //timestamp will be used as id of image picked

            //setup model for image,Param 1 is id,Param 2 is imageUri, Param 3 is imageUrl , from internet
            loadImages()
        } else {
            // Cancelled
            Toast.makeText(this, "Cancelled...!", Toast.LENGTH_SHORT).show()
        }
// Check if image is picked or not
    }

    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d(TAG, "cameraActivityResultLauncher:")
        //Check if image is picked or not
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "cameraActivityResultLauncher: imageUri $imageUri")


            loadImages()
        } else {
            Toast.makeText(this, "Cancelled...!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun validateData() {

        Log.d(TAG, "validateData: ")

        if (imageUri == null) {
            Toast.makeText(this@AddMessMenuActivity, "Warning Pick Image!", Toast.LENGTH_SHORT)
                .show()
        } else {
            //data validate begin upload
            uploadBookImageToStorage()
        }

    }

    private fun uploadBookImageToStorage() {
        Log.d(TAG, "uploadToStorage: uploading to storage....")

        //show progress dialog
        progressDialog.setMessage("Uploading Image...")
        progressDialog.show()


        //path f pdf in firebase storage

        val filePathAndName = "MessImage/menuImage"


        val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)
        // compressing image
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream)
        val reducedImage: ByteArray = byteArrayOutputStream.toByteArray()

        storageReference.putBytes(reducedImage)
            .addOnSuccessListener { taskSnapshot ->

                Log.d(TAG, "uploadToStorage: Image uploaded now getting url ...")

                //get url of uploaded Image
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);

                uploadImageUrl = "${uriTask.result}"
                uploadNoticeInfoToDb()
            }
            .addOnFailureListener { e ->

                Log.d(TAG, "uploadToStorage: failed to upload due to ${e.message}")
                progressDialog.dismiss()

                Toast.makeText(
                    this@AddMessMenuActivity,
                    "Pick Again Failed to Get Image due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()


                Log.i(TAG, "Failed uploading image to server")

            }
            .addOnProgressListener {

                val dataTransferred = (100 * it.bytesTransferred / it.totalByteCount)
                progressDialog.setMessage("Uploading $dataTransferred%")
            }

    }

    private fun uploadNoticeInfoToDb() {
// upload pdf to firebase db
        Log.d(TAG, "uploadInfoToDb: uploading to db")
        progressDialog.setMessage("Uploading pdf info....")


        val hashMap: HashMap<String, Any> = HashMap()


        hashMap["timestamp"] = System.currentTimeMillis()
        hashMap["imageUrl"] = "$uploadImageUrl"


        val ref = FirebaseDatabase.getInstance().getReference("MessMenu")
        ref.child("Image")
            .setValue(hashMap)

            .addOnSuccessListener {
                Log.d(TAG, "uploadPdfInfoToDb: uploaded to db")
                progressDialog.dismiss()

                Toast.makeText(
                    this@AddMessMenuActivity,
                    "Posted Menu Successfully",
                    Toast.LENGTH_SHORT
                ).show()

                imageUri = null

            }

            .addOnFailureListener { e ->
                Log.d(TAG, "uploadToStorage: failed to upload due to ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(
                    this@AddMessMenuActivity,
                    "Failed to Upload due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

}