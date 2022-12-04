package com.example.mxer.fragments
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mxer.R
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.File

class ProfileFragment : Fragment() {

    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    lateinit var ivPfp: ImageView
    lateinit var etBio: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvUserName = view.findViewById<TextView>(R.id.username)
//        val tvPrimeCommunity = view.findViewById<TextView>(R.id.primaryCommunity)
        val tvBio = view.findViewById<TextView>(R.id.bio)
        val ivPfp = view.findViewById<ImageView>(R.id.profilePicture)

        val dialog = AlertDialog.Builder(requireContext()).create()
        etBio = EditText(requireContext())
        dialog.setTitle(" Edit Bio ")
        dialog.setView(etBio)

        val user = ParseUser.getCurrentUser()
        tvUserName.text = " " + user.username
        tvBio.text = user.getString("description")
        Glide.with(requireContext()).load(user.getParseFile("profile_picture")?.url).into(ivPfp)

        ivPfp.setOnClickListener {
            onLaunchCamera()
        }

        tvBio.setOnClickListener {
            etBio.setText(tvBio.text)
            dialog.show()
//            Toast.makeText(requireContext(), "Mixing up bio", Toast.LENGTH_LONG).show()
        }

        dialog.setButton(DialogInterface.BUTTON_POSITIVE,"Save Bio", DialogInterface.OnClickListener {
                dialog, id -> setBio()
            tvBio.text = user.getString("description")
        })


    }
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
                FileProvider.getUriForFile(requireContext(), "com.codepath.mxer.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // by this point we have the camera photo on disk
                setPfp(photoFile!!)
            } else { // Result was a failure
                Toast.makeText(requireContext(), "Error taking picture", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun setPfp(image: File){
      val user = ParseUser.getCurrentUser()
      val pFile = ParseFile(image)
      user.put("profile_picture", pFile)
      user.saveInBackground { e ->
          if (e == null) {
              Log.i(Companion.TAG, "Successfully saved profile picture")
              Toast.makeText(requireContext(), "Successfully updated profile picture!", Toast.LENGTH_SHORT).show()
              val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
              // RESIZE BITMAP, see section below
              // Load the taken image  into a preview
              ivPfp = requireView().findViewById<ImageView>(R.id.profilePicture)
              ivPfp.setImageBitmap(takenImage)
          } else {
              Log.e(Companion.TAG, e.printStackTrace().toString())
              Toast.makeText(requireContext(), "Unable to update profile picture.", Toast.LENGTH_SHORT).show()
          }
      }
  }

    fun setBio() {
        val user = ParseUser.getCurrentUser()
        user.put("description", (etBio.text).toString())
        user.saveInBackground { e ->
            if (e == null) {
                Log.i(Companion.TAG, "Successfully saved bio")
                Toast.makeText(requireContext(), "Successfully updated bio!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e(Companion.TAG, e.printStackTrace().toString())
                Toast.makeText(requireContext(), "Unable to update bio.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object {
        const val TAG = "ProfileFragment"
    }
}

