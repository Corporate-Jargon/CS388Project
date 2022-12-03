package com.example.mxer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mxer.fragments.BrowseFragment
import com.example.mxer.fragments.ProfileFragment
import com.example.mxer.fragments.CommunityFragment
import com.example.mxer.fragments.ComposeFragment
import com.example.mxer.fragments.PostFragment
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.ParseUser

class MainActivity : AppCompatActivity(), Communicator {
    var listOfNums = mutableListOf<String>("true","true","true","true")
    var filterSetting: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (getDataFile().exists()){
            loadItems()
        } else {
            saveItems()
        }
        filterSetting = listOfNums[SettingsActivity.Setting.FILTER.pos].toBoolean()

        val fragmentManager: FragmentManager = supportFragmentManager
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            // Alias
                item ->
            var fragmentToShow: Fragment? = null
            when (item.itemId) {
                R.id.action_home -> {
                    fragmentToShow = BrowseFragment()
                }
//                R.id.action_compose -> {
//                    // Set it to the fragment we want to show
//                    fragmentToShow = ComposeFragment()
//                }
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
        val username = ParseUser.getCurrentUser().username
        return File(filesDir, "settings${username}.txt")
    }
//     Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfNums = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()

        }
    }
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfNums)
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
        //TODO read from list of nums instead of hard-coded value
        bundle.putString("ProfanityFilter", listOfNums[SettingsActivity.Setting.FILTER.pos])
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
    companion object {
        const val TAG = "MainActivity"
    }
}