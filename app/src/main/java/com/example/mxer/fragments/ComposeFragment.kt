package com.example.mxer.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import com.parse.ParseFile
import com.parse.ParseUser
import okhttp3.Headers
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.mxer.*
import okhttp3.MediaType.Companion.toMediaType

open class ComposeFragment : Fragment() {
    var score: Double = -1.0
    private lateinit var communicator: Communicator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentManager: FragmentManager = parentFragmentManager

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
                        Log.d("DEBUG", json.toString())
                        toxiccode = json.jsonObject.getJSONObject("attributeScores")
                            .getJSONObject("TOXICITY")
                            .getJSONObject("summaryScore")["value"] as Double
                        score = toxiccode
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
            Log.i("DEBUG", toxiccode.toString())
            return toxiccode
        }

        // Send a post object to our Parse server
        fun submitPost(description: String, user: ParseUser) {
            val pb = view.findViewById<View>(R.id.pbLoading) as ProgressBar
            pb.visibility = ProgressBar.VISIBLE
            val post = Post()
            post.setDesc(description)
            post.setAuthor(user)
            post.saveInBackground { exception ->
                if (exception != null) {
                    Log.e(MainActivity.TAG, "Error while saving post")
                    exception.printStackTrace()
                    Toast.makeText(requireContext(), "Error saving post", Toast.LENGTH_SHORT).show()

                } else {
                    Log.i(MainActivity.TAG, "Successfully saved post")
                    // Reset views
                    view.findViewById<EditText>(R.id.etPost).setText("")
                }
                pb.visibility = ProgressBar.INVISIBLE
            }
        }
        communicator = activity as Communicator
        view.findViewById<Button>(R.id.btnPost).setOnClickListener {
            // Get description
            val description = view.findViewById<EditText>(R.id.etPost).text.toString()
            val user = ParseUser.getCurrentUser()
            //TODO make sure to prevent race condition
            postAPI(description)

            val threshold = 0.75
            Log.i("DEBUG", score.toString())
            if (score < threshold) {
                // Double exclamation points mean file is guaranteed not to be null
                submitPost(description, user)
            } else {
                Log.e(MainActivity.TAG, "Error submiting post from toxicity")
                Toast.makeText(requireContext(), "Post is too toxic. Lighten up :)", Toast.LENGTH_SHORT).show()
            }
            val bundle: Bundle = this.requireArguments()
            val communityName: String = bundle.getString("Name","")
            val communityId: String = bundle.getString("CommunityId","")
            val community = Community()
            community.setId(communityId)
            community.setName(communityName)
            communicator.passCommunity(community)
        }
    }
}