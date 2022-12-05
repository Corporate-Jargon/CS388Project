package com.example.mxer.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mxer.*
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import org.w3c.dom.Text


class PostFragment : Fragment() {
    lateinit var rvComments: RecyclerView
    lateinit var adapter: CommentsAdapter
    lateinit var communicator: Communicator
    val allComments = ArrayList<Comment>()
    var originalPost = Post()
    lateinit var ivProfile: ImageView
    lateinit var tvAuthor: TextView
    lateinit var tvTimestamp: TextView
    lateinit var tvBody: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.requireArguments()
        val postId: String = bundle.getString("PostId","")
        Log.i(TAG, postId)
        //using a 2nd query for functionality for now
        queryOriginal(postId)
        ivProfile = view.findViewById(R.id.ivProfile)
        tvAuthor = view.findViewById(R.id.tvAuthor)
        tvTimestamp = view.findViewById(R.id.tvTimestamp)
        tvBody = view.findViewById(R.id.tvBody)
        communicator = activity as Communicator
        rvComments = view.findViewById(R.id.rvComments)
        adapter = CommentsAdapter(requireContext(), allComments)
        rvComments.adapter = adapter
        rvComments.layoutManager = LinearLayoutManager(requireContext())

    }
    open fun queryOriginal(postId: String){
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_AUTHOR)
        query.limit = 1
        query.whereEqualTo(Post.KEY_ID,postId)
        Log.i(TAG, postId)
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if(e != null){
                    Log.e(TAG, "Error finding original post")
                } else {
                    if(posts != null) {
                        originalPost = posts[0]
                        tvAuthor.text = originalPost.getAuthor()?.username
                        //TODO make timestamp relative
                        tvTimestamp.text = ""
                        tvBody.text = originalPost.getDesc()
                        var options: RequestOptions = RequestOptions()
                        options.centerCrop()
                        options.circleCrop()
                        Glide.with(requireContext())
                            .load(originalPost.getAuthor()?.getParseFile("profile_picture")?.url)
                            .override(300, 300)
                            .apply(options)
                            .into(ivProfile)
                        Log.i(TAG, posts.toString())
                    }
                }
            }
        })
    }
    companion object {
        const val TAG = "PostFragment"
    }
}