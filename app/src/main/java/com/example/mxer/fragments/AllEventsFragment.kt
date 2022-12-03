package com.example.mxer.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mxer.Communicator
import com.example.mxer.Community
import com.example.mxer.R

open class AllEventsFragment : Fragment() {
    private lateinit var communicator: Communicator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // TODO Change to fragment events when the recyclerviews branch is done
        return inflater.inflate(R.layout.fragment_browse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentManager: FragmentManager = parentFragmentManager
        communicator = activity as Communicator
        // TODO Create buttons for creating and closing events with this stuff
//        view.findViewById<ImageView>(R.id.commimage1).setOnClickListener {
//            val community = Community()
//            community.setId("9cNAL0Ynrh")
//            community.setName("Art")
//            communicator.passCommunity(community)
//        }
    }
    companion object {
        const val TAG = "BrowseFragment"
    }
}