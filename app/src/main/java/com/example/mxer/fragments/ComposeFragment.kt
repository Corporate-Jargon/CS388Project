package com.example.mxer.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.mxer.*
import com.parse.ParseQuery
import com.parse.ParseUser
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

open class ComposeFragment : Fragment() {
    var score: Double = -1.0
    private lateinit var communicator: Communicator
    private lateinit var originalCommunity: Community
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
        communicator = activity as Communicator
        val bundle: Bundle = requireArguments()
        val commId = bundle.getString("CommunityId","")

        queryCommunity(commId)
    }

    // Send a post object to our Parse server
    fun submitPost(
        description: String,
        user: ParseUser,
        score: Double,
        community: Community,
        commId: String
    ) {
        val pb = view?.findViewById<View>(R.id.pbLoading) as ProgressBar
        pb.visibility = ProgressBar.VISIBLE
        val post = Post()
        post.setDesc(description)
        post.setAuthor(user)
        post.setProfane(score)
        post.setComm(community)
        post.saveInBackground { exception ->
            if (exception != null) {
                Log.e(TAG, "Error while saving post")
                exception.printStackTrace()
                Toast.makeText(requireContext(), "Error saving post", Toast.LENGTH_SHORT).show()

            } else {
                if (commId == "tie1n4SSCr") {
                    originalCommunity.setCount(description.length)
                    originalCommunity.saveInBackground { exception ->
                        if (exception == null) {
                            Log.i(TAG, "Successfully saved post")
                            // Reset views
                            view?.findViewById<EditText>(R.id.etPost)?.setText("")
                        }
                        else {
                            Log.i(TAG, "Could not save count")
                            // Reset views
                            view?.findViewById<EditText>(R.id.etPost)?.setText("")
                        }
                    }
                }
                else {
                    Log.i(TAG, "Successfully saved post")
                    // Reset views
                    view?.findViewById<EditText>(R.id.etPost)?.setText("")
                }
            }
            pb.visibility = ProgressBar.INVISIBLE
        }
    }

    private fun queryCommunity(commId: String) {
        val query : ParseQuery<Community> = ParseQuery.getQuery(Community::class.java)
        query.limit = 1
        query.whereEqualTo(Community.KEY_ID, commId)
        query.findInBackground { comms, e ->
            if(e != null) {
                Log.e(TAG, "Error fetching community")
            } else {
                if(comms != null){
                    originalCommunity = comms[0]
                    if (commId == "tie1n4SSCr") {
                        val count = originalCommunity.getCount()
                        val realCount = count?.toInt()
                        Toast.makeText(
                            requireContext(),
                            "The letter count right now is $realCount, ascend past that!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    view?.findViewById<Button>(R.id.btnPost)?.setOnClickListener {
                        // Get description
                        val description = view?.findViewById<EditText>(R.id.etPost)?.text.toString()
                        val user = ParseUser.getCurrentUser()
                        // Check the count to continue
                        if (commId == "tie1n4SSCr") {
                            val count = originalCommunity.getCount()
                            val realCount = count?.toInt()
                            Log.i(TAG, realCount.toString())
                            if (description.length <= realCount!!) {
                                Toast.makeText(
                                    requireContext(),
                                    "The count is $realCount now, try to make it longer",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@setOnClickListener

                            }
                        }
                        postAPI(description, user, commId)
                    }
                }
            }
        }
    }
    fun postAPI(description: String, user: ParseUser, commId: String) {
        val requestHeaders = RequestHeaders()
        val params = RequestParams()
        val api_key = getString(R.string.perspective_key)

        params["key"] = api_key
        val client = AsyncHttpClient()
        val jsonObject =
            JSONObject("{\"comment\": {\"text\": \"$description\"},\"languages\": [\"en\"],\"requestedAttributes\": {\"TOXICITY\": {}}}")
        val body = jsonObject.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = body.toRequestBody(mediaType)

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

                    val bundle: Bundle = requireArguments()
                    val communityName: String = bundle.getString("Name", "")
                    val communityId: String = bundle.getString("CommunityId", "")
                    val filterSetting: Boolean =
                        bundle.getString("ProfanityFilter", "true").toBoolean()
                    val community = Community()
                    community.setId(communityId)
                    community.setName(communityName)

                    val threshold = 0.75
                    if (filterSetting) {
                        if (score < threshold) {
                            submitPost(description, user, score, originalCommunity, commId)
                        } else {
                            Log.e(TAG, "Error submitting post from toxicity")
                            Toast.makeText(
                                requireContext(),
                                "Post is too toxic. Lighten up :)",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        submitPost(description, user, score, originalCommunity, commId)
                    }

                    communicator.passCommunity(community)
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
    }
    companion object {
        const val TAG = "ComposeFragment"
    }
}