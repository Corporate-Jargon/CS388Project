package com.example.mxer.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mxer.*
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

open class EventsFragment : Fragment() {
    private lateinit var communicator: Communicator
    lateinit var adapter: EventsAdapter
    var allEvents: ArrayList<Community> = ArrayList<Community>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentManager: FragmentManager = parentFragmentManager
        communicator = activity as Communicator
        val rvEvents = view.findViewById<RecyclerView>(R.id.rvEvents)

        adapter = EventsAdapter(requireContext(), allEvents)
        rvEvents.adapter = adapter

        getEvents()
    }
    fun getEvents() {
        val query: ParseQuery<Community> = ParseQuery.getQuery(Community::class.java)
        query.whereEqualTo("isEvent", 1)
        query.findInBackground(object : FindCallback<Community> {
            override fun done(events: MutableList<Community>?, e: ParseException?) {
                if(e != null) {
                    Log.e(TAG, "Error fetching events: ${e}")
                } else {
                    if(events != null) {
                        allEvents.addAll(events)
                        Log.i(TAG, "Events: ${allEvents}")
                        adapter.notifyDataSetChanged()

                    }
                }
            }
        })
    }

    companion object {
        const val TAG = "EventsFragment"
    }
}