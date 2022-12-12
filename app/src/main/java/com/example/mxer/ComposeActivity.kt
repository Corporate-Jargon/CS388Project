package com.example.mxer

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.example.mxer.fragments.ProfileFragment
import com.parse.FindCallback
import com.parse.ParseFile
import com.parse.ParseQuery
import com.parse.ParseUser
import java.io.File
import java.io.IOException

class ComposeActivity : AppCompatActivity() {
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)
        // Set description of post
        // Button to launch camera for picture
        // Image to show the picture
        // A button to save and send the post
        findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            // Send post to server
            // Get description
            val description = findViewById<EditText>(R.id.description).text.toString()
            val user = ParseUser.getCurrentUser()
            if (photoFile != null) {
                // Double exclamation points mean file is guaranteed not to be null
                submitCommunity(description, user, photoFile!!)
            } else {
                Log.e(TAG, "Error getting picture")
                Toast.makeText(this, "Take a picture!", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<Button>(R.id.btnTakePicture).setOnClickListener {
            // Launch camera to let user take picture
            onPickPhoto()
        }
    }

    fun goToMainActivity() {
        val intent = Intent(this@ComposeActivity, MainActivity::class.java)
        startActivity(intent)
        // End app after using back button by closing this activity
        finish()
    }

    // Send a post object to our Parse server
    private fun submitCommunity(description: String, user: ParseUser, file: File) {
        // Create the Post object
        // on some click or some loading we need to wait for...
        // on some click or some loading we need to wait for...
        val pb = findViewById<View>(R.id.pbLoading) as ProgressBar
        pb.visibility = ProgressBar.VISIBLE
        val community = Community()
        community.setOwner(user)
        community.setIsEvent(0)
        community.setName(description)
        community.setIcon(ParseFile(file))
        community.saveInBackground { exception ->
            if (exception != null) {
                Log.e(TAG, "Error while saving community")
                exception.printStackTrace()
                Toast.makeText(this, "Error saving community", Toast.LENGTH_SHORT).show()

            } else {
                Log.i(TAG, "Successfully saved community")
                Toast.makeText(this, "New community created", Toast.LENGTH_SHORT).show()
                // Reset views
                findViewById<EditText>(R.id.description).setText("")
                findViewById<ImageView>(R.id.imageView).setImageBitmap(null)
                goToMainActivity()

            }
            pb.visibility = ProgressBar.INVISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == PICK_PHOTO_CODE) {
            val photoUri = data.data

            // Load the image located at photoUri into selectedImage
            val selectedImage = loadFromUri(photoUri)
            // Load the selected image into a preview
            val ivPreview: ImageView = findViewById(R.id.imageView)
            ivPreview.setImageBitmap(selectedImage)
        }
    }

    // Trigger gallery selection for a photo
    fun onPickPhoto() {
        // Create intent for picking a photo from the gallery
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE)
        }
    }

    fun loadFromUri(photoUri: Uri?): Bitmap? {
        var image: Bitmap? = null
        try {
            // check version of Android on device
            image = if (Build.VERSION.SDK_INT > 27) {
                // on newer versions of Android, use the new decodeBitmap method
                val source = ImageDecoder.createSource(this.getContentResolver(), photoUri!!)
                ImageDecoder.decodeBitmap(source)
            } else {
                // support older versions of Android by using getBitmap
                MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }

    companion object {
        const val TAG = "ComposeActivity"
        const val PICK_PHOTO_CODE = 1046

    }
}