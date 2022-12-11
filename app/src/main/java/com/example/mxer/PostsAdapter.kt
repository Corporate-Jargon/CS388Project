package com.example.mxer

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.sql.Date

class PostsAdapter(private val context: Context, val posts: ArrayList<Post>) : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {
    private lateinit var communicator: Communicator
    private val TAG = "PostsAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun clear(){
        posts.clear()
        notifyDataSetChanged()
    }
    fun addAll(postList: List<Post>){
        posts.addAll(postList)
        notifyDataSetChanged()
    }

    fun getRelativeTimeAgo(string: String): String{
        val createTime = java.util.Date(string)
        val currentTime = java.util.Date()
        val diff = currentTime.time - createTime.time
        currentTime.toString()

        val d = (1000 * 24 * 60 * 60).toLong()
        val h = (1000 * 60 * 60).toLong()
        val m = (1000 * 60).toLong()
        val s = 1000.toLong()

        val day = diff / d
        val hour = diff % d / h
        val min = diff % d % h / m
//        val sec = diff % d % h % m / s

        var time = ""
        if (day > 0) time += "$day" + "d "
        if (hour > 0) time += "$hour" + "h "
        if (min > 0) time += "$min" + "m "
        return time
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val ivProfile = itemView.findViewById<ImageView>(R.id.ivProfile)
        val tvAuthor = itemView.findViewById<TextView>(R.id.tvAuthor)
        val tvTimestamp = itemView.findViewById<TextView>(R.id.tvTimestamp)
        val tvBody = itemView.findViewById<TextView>(R.id.tvBody)
        init {
            itemView.setOnClickListener(this)
        }

        fun bind(post: Post){
            tvAuthor.text = post.getAuthor()?.username
            tvTimestamp.text = getRelativeTimeAgo((post.createdAt).toString())
            tvBody.text = post.getDesc()
            var options: RequestOptions = RequestOptions()
//            options.centerCrop()
//            options.circleCrop()
            Glide.with(itemView)
                .load(post.getAuthor()?.getParseFile("profile_picture")?.url)
//                .override(300, 300)
//                .apply(options)
                .into(ivProfile)
        }

        override fun onClick(v: View?) {
            val post = posts[adapterPosition]
            val test = post.objectId
            if (test != null) {
                Log.i(TAG, test)
            }
            val activity = context as Activity
            communicator = activity as Communicator
            communicator.passPost(post)
        }
    }
}