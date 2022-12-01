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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.mxer.*
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery

open class BrowseFragment : Fragment() {
    private lateinit var communicator: Communicator
    lateinit var adapter: CommunityAdapter
    var allCommunities: ArrayList<Community> = ArrayList<Community>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentManager: FragmentManager = parentFragmentManager
        communicator = activity as Communicator

        val rvCommunities = view.findViewById<RecyclerView>(R.id.rvCommunities)
        rvCommunities.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        adapter = CommunityAdapter(requireContext(), allCommunities)
        rvCommunities.adapter = adapter

        getCommunities()
    }

    fun getCommunities() {
        val query: ParseQuery<Community> = ParseQuery.getQuery(Community::class.java)
        query.findInBackground(object : FindCallback<Community> {
            override fun done(communities: MutableList<Community>?, e: ParseException?) {
                if(e != null) {
                    Log.e(TAG, "Error fetching posts: ${e}")
                } else {
                    if(communities != null) {
                        allCommunities.addAll(communities)
                        Log.i(TAG, "Communities: ${allCommunities}")
                        adapter.notifyDataSetChanged()

                    }
                }
            }
        })
    }


    companion object {
        const val TAG = "BrowseFragment"
    }
}