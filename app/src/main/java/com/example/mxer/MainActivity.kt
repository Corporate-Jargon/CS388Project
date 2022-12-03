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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

    fun createEvent() {
//        val query: ParseQuery<Community> = ParseQuery.getQuery(Community::class.java)
//        query.whereEqualTo("isEvent", 0)
//        query.findInBackground(object : FindCallback<Community> {
//            override fun done(communities: MutableList<Community>?, e: ParseException?) {
//                if(e != null) {
//                    Log.e(BrowseFragment.TAG, "Error fetching posts: ${e}")
//                } else {
//                    if(communities != null) {
//                        allCommunities.addAll(communities)
//                        Log.i(BrowseFragment.TAG, "Communities: ${allCommunities}")
//                        adapter.notifyDataSetChanged()
//
//                    }
//                }
//            }
//        })


//        // Specify which class to query
//        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
//        query.include(Post.KEY_USER)
//        // Get all the posts only from the signed in user
//        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
//        // Newer posts appear first since it's descending
//        query.addDescendingOrder("createdAt")
//        query.findInBackground(object : FindCallback<Post> {
//            override fun done(posts: MutableList<Post>?, e: ParseException?) {
//                if (e != null) {
//                    // Something went wrong
//                    Log.e(TAG, "Error fetching posts")
//                } else {
//                    if (posts != null) {
//                        // If we got something
//                        for (post in posts) {
//                            Log.i(
//                                TAG,
//                                "Post: " + post.getDescription() + " , username: " + post.getUser()?.username
//                            )
//                        }
//                        allPosts.addAll(posts)
//                        adapter.notifyDataSetChanged()
//                    }
//                }
//            }
//        })


//        val event = Community()
//        event.setName("Unknown")
//        event.se
//        post.setDesc(description)
//        post.setAuthor(user)
//        post.saveInBackground { exception ->
//            if (exception != null) {
//                Log.e(MainActivity.TAG, "Error while saving post")
//                exception.printStackTrace()
//                Toast.makeText(requireContext(), "Error saving post", Toast.LENGTH_SHORT).show()
//
//            } else {
//                Log.i(MainActivity.TAG, "Successfully saved post")
//                // Reset views
//                view.findViewById<EditText>(R.id.etPost).setText("")
//            }
//            pb.visibility = ProgressBar.INVISIBLE
//        }
    }
    companion object {
        const val TAG = "MainActivity"
    }
}