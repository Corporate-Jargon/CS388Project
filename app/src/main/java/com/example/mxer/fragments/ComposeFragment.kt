package com.example.mxer.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import com.example.mxer.MainActivity
import com.example.mxer.Post
import com.example.mxer.R
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.File


open class ComposeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentManager: FragmentManager = parentFragmentManager


        // Send a post object to our Parse server
        fun submitPost(description: String, user: ParseUser, file: File) {
            // Create the Post object
            // on some click or some loading we need to wait for...
            // on some click or some loading we need to wait for...
            val pb = view.findViewById<View>(R.id.pbLoading) as ProgressBar
            pb.visibility = ProgressBar.VISIBLE
            val post = Post()
            post.setDescription(description)
            post.setUser(user)
            post.setImage(ParseFile(file))
            post.saveInBackground { exception ->
                if (exception != null) {
                    Log.e(MainActivity.TAG, "Error while saving post")
                    exception.printStackTrace()
                    Toast.makeText(requireContext(), "Error saving post", Toast.LENGTH_SHORT).show()

                } else {
                    Log.i(MainActivity.TAG, "Successfully saved post")
                    // Reset views
                    view.findViewById<EditText>(R.id.description).setText("")
                    view.findViewById<ImageView>(R.id.imageView).setImageBitmap(null)

                }
                pb.visibility = ProgressBar.INVISIBLE
            }
        }
        // Set description of post
        // Button to launch camera for picture
        // Image to show the picture
        // A button to save and send the post
        view.findViewById<Button>(R.id.btnSubmit).setOnClickListener {

        }

        view.findViewById<Button>(R.id.btnPost).setOnClickListener {
            // Get description
            val description = view.findViewById<EditText>(R.id.etPost).text.toString()
            val user = ParseUser.getCurrentUser()
            if (photoFile != null) {
                // Double exclamation points mean file is guaranteed not to be null
                submitPost(description, user, photoFile!!)
            } else {
                Log.e(MainActivity.TAG, "Error getting picture")
                Toast.makeText(requireContext(), "Take a picture!", Toast.LENGTH_SHORT).show()
            }
            //TODO make sure user is returned to the proper feed
            val fragmentToShow = CommunityFragment()
            fragmentManager.beginTransaction().replace(R.id.flContainer,fragmentToShow).commit()
        }
    }
}