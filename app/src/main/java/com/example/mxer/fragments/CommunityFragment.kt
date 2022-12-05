package com.example.mxer.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mxer.*
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

open class CommunityFragment: Fragment() {
    lateinit var rvPosts: RecyclerView
    lateinit var adapter: PostsAdapter
    lateinit var communicator: Communicator
    val allPosts = ArrayList<Post>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentManager: FragmentManager = parentFragmentManager
        communicator = activity as Communicator
        rvPosts = view.findViewById(R.id.rvPosts)
        adapter = PostsAdapter(requireContext(), allPosts)
        rvPosts.adapter = adapter
        rvPosts.layoutManager = LinearLayoutManager(requireContext())

        val bundle: Bundle = this.requireArguments()
        val communityName: String = bundle.getString("Name","")
        view.findViewById<TextView>(R.id.tvCommTitle).text = communityName
        val communityId: String = bundle.getString("CommunityId","")
        view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.btnCompose).setOnClickListener{
            val community = Community()
            community.setId(communityId)
            community.setName(communityName)
            communicator.passCompose(community)
        }
        queryPosts(communityId)
    }
    open fun queryPosts(commId: String) {
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_AUTHOR)
        query.addDescendingOrder("createdAt")
        query.limit = 20
        Log.i(TAG, "commid: ${commId}")
        query.whereEqualTo(Post.KEY_COMM, commId)
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if(e != null) {
                    Log.e(TAG, "Error fetching posts")
                } else {
                    if(posts != null) {
                        Log.i(TAG, "Posts: ${posts}")
                        allPosts.clear()
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
    companion object {
        const val TAG = "CommunityFragment"
    }
}