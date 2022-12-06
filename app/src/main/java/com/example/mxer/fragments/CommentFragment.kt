package com.example.mxer.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.mxer.*
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class CommentFragment : Fragment() {
    var score: Double = -1.0
    private val TAG = "CommentFragment"
    private lateinit var communicator: Communicator
    private lateinit var originalPost: Post
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
        view.findViewById<TextView>(R.id.ComposeTitle).text="Compose Comment"
        view.findViewById<TextView>(R.id.btnPost).text="Comment"
        val bundle: Bundle = requireArguments()
        val postId = bundle.getString(Post.KEY_ID,"")
        queryPost(postId)
    }
    fun queryPost(postId: String) {
        val query : ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_AUTHOR)
        query.limit = 1
        query.whereEqualTo(Post.KEY_ID, postId)
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if(e != null) {
                    Log.e(TAG, "Error fetching post")
                } else {
                    if(posts != null) {
                        originalPost = posts[0]
                        view?.findViewById<Button>(R.id.btnPost)?.setOnClickListener {
                            // Get description
                            val description = view?.findViewById<EditText>(R.id.etPost)?.text.toString()
                            val user = ParseUser.getCurrentUser()

                            // Double exclamation points mean file is guaranteed not to be null
                            postAPI(description, user)
                        }
                    }
                }
            }
        })
    }
    fun submitComment(description: String, user: ParseUser, score: Double, post: Post) {
        val pb = view?.findViewById<View>(R.id.pbLoading) as ProgressBar
        pb.visibility = ProgressBar.VISIBLE
        val comment = Comment()
        comment.setDescription(description)
        comment.setAuthor(user)
        comment.setProfane(score)
        comment.setPost(post)
        comment.saveInBackground { exception ->
            if (exception != null) {
                Log.e(MainActivity.TAG,"Error saving comment")
                exception.printStackTrace()
                Toast.makeText(requireContext(), "Error saving comment", Toast.LENGTH_SHORT).show()
            } else {
                Log.i(MainActivity.TAG, "Successfully saved comment")
                view?.findViewById<EditText>(R.id.etPost)?.setText("")
            }
            pb.visibility = ProgressBar.INVISIBLE
        }
    }
    fun postAPI(description: String, user: ParseUser) {
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
                    val post = Post()
                    val postId = bundle.getString(Post.KEY_ID)
                    post.objectId = postId
                    val filterSetting: Boolean =
                        bundle.getString("ProfanityFilter", "true").toBoolean()
                    val threshold = 0.75
                    if (filterSetting) {
                        if (score < threshold) {
                            submitComment(description, user, score, originalPost)
                        } else {
                            Log.e(MainActivity.TAG, "Error submitting post from toxicity")
                            Toast.makeText(
                                requireContext(),
                                "Post is too toxic. Lighten up :)",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        submitComment(description, user, score, originalPost)
                    }
                    communicator.passPost(post)
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
}