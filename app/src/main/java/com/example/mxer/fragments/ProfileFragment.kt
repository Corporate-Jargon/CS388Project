package com.example.mxer.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mxer.R
import com.parse.ParseUser
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {
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
        val tvPrimeCommunity = view.findViewById<TextView>(R.id.primaryCommunity)
        val tvBio = view.findViewById<TextView>(R.id.bio)
        val ivPfp = view.findViewById<ImageView>(R.id.profilePicture)

        val user = ParseUser.getCurrentUser()
        tvUserName.text = user.username
        tvBio.text = user.getString("description")
        Glide.with(requireContext()).load(user.getParseFile("profile_picture")?.url).into(ivPfp)


    }
}