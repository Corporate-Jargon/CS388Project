package com.example.mxer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mxer.fragments.BrowseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager
//        findViewById<BottomNavigationView>(com.parse.R.id.bottom_navigation).setOnItemSelectedListener {
//            // Alias
//                item ->
//            var fragmentToShow: Fragment? = null
//            when (item.itemId) {
////                com.parse.R.id.action_home -> {
////                    fragmentToShow = BrowseFragment()
////                }
////                com.parse.R.id.action_event -> {
////                    fragmentToShow = ComposeFragment()
////
////                }
////                com.parse.R.id.action_profile -> {
////                    fragmentToShow = ProfileFragment()
////                }
//            }
//            if (fragmentToShow != null) {
//                fragmentManager.beginTransaction().replace(com.parse.R.id.flContainer, fragmentToShow).commit()
//            }
//            // Return true when we handled the interaction
//            true
//        }
//        // Set default selection
//        findViewById<BottomNavigationView>(com.parse.R.id.bottom_navigation).selectedItemId = com.parse.R.id.action_home
    }
}