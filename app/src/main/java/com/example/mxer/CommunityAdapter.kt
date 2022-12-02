package com.example.mxer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CommunityAdapter (val context: Context, val communities: ArrayList<Community>): RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val tvName: TextView
        val ivIcon: ImageView
        init {
            tvName = itemView.findViewById<TextView>(R.id.tvEvName)
            ivIcon = itemView.findViewById<ImageView>(R.id.ivIcon1)
        }
        fun bind(community: Community) {
            tvName.setText(community.getName())
            Glide.with(itemView.context).load(community.getIcon()?.url).into(ivIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_browse, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val community = communities.get(position)
        holder.bind(community)
    }

    override fun getItemCount(): Int {
        return communities.size
    }
}