package com.example.mxer.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mxer.Communicator
import com.example.mxer.Community
import com.example.mxer.CommunityAdapter
import com.example.mxer.R
import com.parse.ParseQuery

open class BrowseFragment : Fragment() {
    private lateinit var communicator: Communicator
    lateinit var adapter: CommunityAdapter
    var allCommunities: ArrayList<Community> = ArrayList()
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

        getCommunities("")

        val svCommunities = view.findViewById<SearchView>(R.id.svCommunities)
        svCommunities.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                allCommunities.clear()
                if (p0 != null) {
                    getCommunities(p0)
                } else {
                    getCommunities("")
                }
                return true
            }
        })
    }
    private fun getCommunities(name: String) {
        val query: ParseQuery<Community> = ParseQuery.getQuery(Community::class.java)
        query.whereEqualTo("isEvent", 0)
        query.whereMatches("name", "("+name+")", "i")
        query.findInBackground { communities, e ->
            if (e != null) {
                Log.e(TAG, "Error fetching posts: $e")
            } else {
                if (communities != null) {
                    allCommunities.addAll(communities)
                    Log.i(TAG, "Communities: $allCommunities")
                    adapter.notifyDataSetChanged()

                }
            }
        }
    }


    companion object {
        const val TAG = "BrowseFragment"
    }
}