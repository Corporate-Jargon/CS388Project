package com.example.mxer.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mxer.*
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

open class EventsFragment : Fragment() {
    private lateinit var communicator: Communicator
    lateinit var adapter: EventsAdapter
    var allEvents: ArrayList<Community> = ArrayList<Community>()
    var allEventsImg1: ArrayList<Community> = ArrayList<Community>()
    var allEventsImg2: ArrayList<Community> = ArrayList<Community>()
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
        rvEvents.layoutManager = LinearLayoutManager(requireContext())
        adapter = EventsAdapter(requireContext(), allEvents, allEventsImg1, allEventsImg2)
        rvEvents.adapter = adapter

        getEvents("")

        val svEvents = view.findViewById<SearchView>(R.id.svEvents)
        svEvents.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                allEvents.clear()
                allEventsImg1.clear()
                allEventsImg2.clear()
                if (p0 != null) {
                    getEvents(p0)
                } else {
                    getEvents("")
                }
                return true
            }

        })
    }
    fun getEvents(name: String) {
        val query: ParseQuery<Community> = ParseQuery.getQuery(Community::class.java)
        query.whereEqualTo("isEvent", 1)
        query.whereMatches("name", "("+name+")", "i")
        query.findInBackground(object : FindCallback<Community> {
            override fun done(events: MutableList<Community>?, e: ParseException?) {
                if(e != null) {
                    Log.e(TAG, "Error fetching events: ${e}")
                } else {
                    if(events != null) {
                        allEvents.addAll(events)
                        for (i in events.indices) {
                            allEventsImg1.add(events[i].getMxe1() as Community)
                            allEventsImg2.add(events[i].getMxe2() as Community)
                        }
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