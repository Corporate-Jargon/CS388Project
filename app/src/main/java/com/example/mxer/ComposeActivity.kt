package com.example.mxer

import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.Image
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
import org.apache.commons.io.FilenameUtils.getPath
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
            onLaunchCamera()
        }
        findViewById<TextView>(R.id.skip_link).setOnClickListener {
            goToMainActivity()
        }
    }

    fun goToMainActivity() {
        val intent = Intent(this@ComposeActivity, MainActivity::class.java)
        startActivity(intent)
        // End app after using back button by closing this activity
        finish()
    }
    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }
//    fun getPath(uri: Uri?): String? {
//        if (uri == null) {
//            return null
//        }
//        val projection = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor: Cursor? = this.getContentResolver().query(
//            uri, projection, null,
//            null, null
//        )
//        if (cursor != null) {
//            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//            cursor.moveToFirst()
//            return cursor.getString(column_index)
//        }
//        assert(false)
//        if (cursor != null) {
//            cursor.close()
//        }
//        return uri.path
//    }
    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(this, "com.codepath.mxer.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // We have photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // Resize bitmap
                // Load the taken image into a preview
                val ivPreview: ImageView = findViewById(R.id.imageView)
                ivPreview.setImageBitmap(takenImage)
            } else {
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (data != null && requestCode == PICK_PHOTO_CODE) {
//            val photoUri = data.data
//
//            // Load the image located at photoUri into selectedImage
//            val selectedImage = loadFromUri(photoUri)
//val imagePath = getPath(photoUri)
//            photoFile = File(imagePath)
////            photoFile = getPhotoFileUri(photoUri.toString())
//            // Load the selected image into a preview
//            val ivPreview: ImageView = findViewById(R.id.imageView)
//            ivPreview.setImageBitmap(selectedImage)
//        }
//    }

    // Trigger gallery selection for a photo
    fun onPickPhoto() {
        // Create intent for picking a photo from the gallery
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
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