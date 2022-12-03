package com.example.mxer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mxer.fragments.*
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class MainActivity : AppCompatActivity(), Communicator {
    var listOfNums = mutableListOf<String>("true","true","true","true")
    var filterSetting: Boolean = true
    var allCommunities: ArrayList<Community> = ArrayList<Community>()
    var userCommunities: ArrayList<Community> = ArrayList<Community>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getOtherCommunities()
        getUserCommunities()
        if (File(filesDir, "settings.txt").exists()){
            loadItems()
            filterSetting = listOfNums[SettingsActivity.Setting.FILTER.pos].toBoolean()
        } else {
            filterSetting = true
        }
        val fragmentManager: FragmentManager = supportFragmentManager
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            // Alias
                item ->
            var fragmentToShow: Fragment? = null
            when (item.itemId) {
                R.id.action_home -> {
                    fragmentToShow = BrowseFragment()
                }
                R.id.action_event -> {
                    // Set it to the fragment we want to show
                    fragmentToShow = AllEventsFragment()
                }
                R.id.action_profile -> {
                    fragmentToShow = ProfileFragment()
                }
            }
            if (fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
            }
            // Return true when we handled the interaction
            true
        }
        // Set default selection
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_home
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings){
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    fun getDataFile(): File {
        // Every line is going to represent a specific task in our list of tasks
        return File(filesDir, "settings.txt")
    }
    // Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfNums = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()

        }
    }

    override fun passCommunity(community: Community) {
        //passes community id from browse to community view screen
        val bundle = Bundle()
        bundle.putString("CommunityId", community.getId())
        bundle.putString("Name", community.getName())
        val transaction = this.supportFragmentManager.beginTransaction()
        val fragment = CommunityFragment()
        fragment.arguments = bundle
        transaction.replace(R.id.flContainer, fragment).commit()
    }

    override fun passCompose(community: Community) {
        //passes community id from community view to compose screen
        val bundle = Bundle()
        bundle.putString("CommunityId", community.getId())
        bundle.putString("Name", community.getName())
        val transaction = this.supportFragmentManager.beginTransaction()
        val fragment = ComposeFragment()
        fragment.arguments = bundle
        transaction.replace(R.id.flContainer, fragment).commit()
    }

    override fun passPost(post: Post) {
        val bundle = Bundle()
        bundle.putString("PostId", post.objectId)
        val transaction = this.supportFragmentManager.beginTransaction()
        val fragment = PostFragment()
        fragment.arguments = bundle
        transaction.replace(R.id.flContainer, fragment).commit()
    }

    fun deleteEvent() {
        // TODO Make a query to set the event's isstatus to 2
        //  Deleting may require an event from a bundle to the
        //  profile page, and add a create/delete button on profile

    }
    fun getOtherCommunities() {
    // TODO When pr from recyclerviews is done, uncomment the whereequalto for isevent
        val query: ParseQuery<Community> = ParseQuery.getQuery(Community::class.java)
        //query.whereEqualTo("isEvent", 0)
        query.include(Community.KEY_OWNER)
        query.whereNotEqualTo(Community.KEY_OWNER, ParseUser.getCurrentUser())
        query.findInBackground(object : FindCallback<Community> {
            override fun done(communities: MutableList<Community>?, e: ParseException?) {
                if (e != null) {
                    // Something went wrong
                    Log.e(TAG, "Error fetching communities")
                } else {
                    if (communities != null) {
                        allCommunities.addAll(communities)
                        Log.i(TAG, "Communities: $allCommunities")
                    }
                }
            }})
    }
    fun getUserCommunities() {
        // TODO When pr from recyclerviews is done, uncomment the whereequalto for isevent
        val query: ParseQuery<Community> = ParseQuery.getQuery(Community::class.java)
        //query.whereEqualTo("isEvent", 0)
        query.include(Community.KEY_OWNER)
        query.whereEqualTo(Community.KEY_OWNER, ParseUser.getCurrentUser())
        query.findInBackground(object : FindCallback<Community> {
            override fun done(communities: MutableList<Community>?, e: ParseException?) {
                if (e != null) {
                    // Something went wrong
                    Log.e(TAG, "Error fetching communities")
                } else {
                    if (communities != null) {
                        userCommunities.addAll(communities)
                        Log.i(TAG, "Communities: $userCommunities")
                    }
                }
            }})
    }
    fun createEvent() {
        // TODO When pr from recyclerviews is done, uncomment the whereequalto for isevent
        val query: ParseQuery<Community> = ParseQuery.getQuery(Community::class.java)
        //query.whereEqualTo("isEvent", 1)
        query.include(Community.KEY_OWNER)
        query.whereEqualTo(Community.KEY_OWNER, ParseUser.getCurrentUser())
        query.findInBackground(object : FindCallback<Community> {
            override fun done(events: MutableList<Community>?, e: ParseException?) {
                if (e != null) {
                    // New event
                    makeEvent()
                    Log.i(TAG, "New event")
                } else {
                    if (events != null) {
                        makeEvent()

                        Log.e(TAG, "You can only have one event at a time")
                    }
                }
            }
        })
    }
    fun makeEvent() {
        val event = Community()
        event.setName("Unknown")
        event.setOwner(ParseUser.getCurrentUser())
        event.setIsEvent(1)
        userCommunities[0].getId()?.let { event.setMxe1(it) }
        allCommunities[0].getId()?.let { event.setMxe2(it) }
        event.saveInBackground { exception ->
            if (exception != null) {
                Log.e(TAG, "Error while saving event")
                exception.printStackTrace()
                Toast.makeText(this, "Created event", Toast.LENGTH_SHORT).show()
            } else {
                Log.i(TAG, "Successfully saved event")
                Log.i(TAG, "Event: $event")
            }
        }
    }
    companion object {
        const val TAG = "MainActivity"
    }
}