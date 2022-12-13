package com.example.mxer

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

class CommentsAdapter(private val context: Context, val comments: ArrayList<Comment>) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    private lateinit var communicator: Communicator
    private val TAG = "CommentsAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment)
    }
    fun clear(){
        comments.clear()
        notifyDataSetChanged()
    }
    fun addAll(commentList: List<Comment>){
        comments.addAll(commentList)
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
    override fun getItemCount(): Int {
        return comments.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val ivProfile: ImageView = itemView.findViewById<ImageView>(R.id.ivProfile)
        private val tvAuthor = itemView.findViewById<TextView>(R.id.tvAuthor)
        private val tvTimestamp = itemView.findViewById<TextView>(R.id.tvTimestamp)
        private val tvBody = itemView.findViewById<TextView>(R.id.tvBody)
        init {
            itemView.setOnClickListener(this)
        }
        fun bind(comment: Comment){
            tvAuthor.text = comment.getAuthor()?.username
            tvTimestamp.text = getRelativeTimeAgo((comment.createdAt).toString())
            tvBody.text = comment.getDescription()
            val options = RequestOptions().centerCrop().circleCrop()
            Glide.with(itemView)
                .load(comment.getAuthor()?.getParseFile("profile_picture")?.url)
                .override(300, 300)
                .apply(options)
                .into(ivProfile)
        }

        override fun onClick(v: View?) {
            val comment = comments[adapterPosition]
            Log.i(TAG, comment.getAuthor().toString())
        }
    }
}