package com.example.mxer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.mxer.fragments.BrowseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
//                R.id.action_profile -> {
//                    fragmentToShow = ProfileFragment()
//                }
            }
            if (fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
            }
            // Return true when we handled the interaction
            true
        }
        // Set default selection
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_home

        fun postAPI(string: String): Double {
            val requestHeaders = RequestHeaders()
            val params = RequestParams()
            val api_key = getString(R.string.perspective_key)

            params["key"] = api_key
            val client = AsyncHttpClient()
            val jsonObject = JSONObject("{\"comment\": {\"text\": \"$string\"},\"languages\": [\"en\"],\"requestedAttributes\": {\"TOXICITY\": {}}}")
            val body = jsonObject.toString()
            val mediaType = "application/json".toMediaType()
            val requestBody = RequestBody.create(mediaType, body)

            var toxiccode = -1.0
            client.post(
                "https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze",
                requestHeaders,
                params,
                requestBody,
                object : JsonHttpResponseHandler() {
                    override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                        toxiccode = json.jsonObject.getJSONObject("attributeScores")
                            .getJSONObject("TOXICITY")
                            .getJSONObject("summaryScore")["value"].toString().toDouble()
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers,
                        response: String,
                        throwable: Throwable
                    ) {
                        Log.d("DEBUG", response)
                    }
                })
            return toxiccode
        }
    }
}