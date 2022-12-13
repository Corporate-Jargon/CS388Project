package com.example.mxer.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mxer.*
import com.parse.ParseQuery


class PostFragment : Fragment() {
    private lateinit var rvComments: RecyclerView
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
    fun queryOriginal(postId: String){
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_AUTHOR)
        query.limit = 1
        query.whereEqualTo(Post.KEY_ID,postId)
        Log.i(TAG, postId)
        query.findInBackground { posts, e ->
            if (e != null) {
                Log.e(TAG, "Error finding original post")
            } else {
                if (posts != null) {
                    originalPost = posts[0]
                    tvAuthor.text = originalPost.getAuthor()?.username
                    tvTimestamp.text = ""
                    tvBody.text = originalPost.getDesc()
                    val options = RequestOptions().centerCrop().circleCrop()
                    Glide.with(requireContext())
                        .load(originalPost.getAuthor()?.getParseFile("profile_picture")?.url)
                        .override(300, 300)
                        .apply(options)
                        .into(ivProfile)
                    Log.i(TAG, posts.toString())
                    queryComments(originalPost)
                    view?.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(
                        R.id.btnCompose
                    )?.setOnClickListener {
                        communicator.passComment(originalPost)
                    }
                }
            }
        }
    }
    fun queryComments(post: Post){
        val query: ParseQuery<Comment> = ParseQuery.getQuery(Comment::class.java)
        query.include(Comment.KEY_AUTHOR)
        query.addDescendingOrder("createdBy")
        query.limit = 20
        query.whereEqualTo(Comment.KEY_REPLY, post)
        query.findInBackground { comments, e ->
            if (e != null) {
                Log.e(TAG, "Error fetching comments")
            } else {
                if (comments != null) {
                    allComments.clear()
                    allComments.addAll(comments)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
    companion object {
        const val TAG = "PostFragment"
    }
}