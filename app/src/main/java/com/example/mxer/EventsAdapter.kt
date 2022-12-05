package com.example.mxer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parse.ParseObject

class EventsAdapter(
    val context: Context,
    val events: ArrayList<Community>,
   val allEventsImg1: ArrayList<Community>,
   val allEventsImg2: ArrayList<Community>
): RecyclerView.Adapter<EventsAdapter.ViewHolder>() {
    private lateinit var communicator: Communicator
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val tvName: TextView
        val ivIcon1: ImageView
        val ivIcon2: ImageView
        init {
            tvName = itemView.findViewById<TextView>(R.id.tvCommName)
            ivIcon1 = itemView.findViewById<ImageView>(R.id.ivIcon1)
            ivIcon2 = itemView.findViewById<ImageView>(R.id.ivIcon2)
        }
        fun bind(community: Community, images1: Community, images2: Community) {
            tvName.setText(community.getName())
            Glide.with(itemView.context).load(images1.getIcon()?.url).into(ivIcon1)
            Glide.with(itemView.context).load(images2.getIcon()?.url).into(ivIcon2)

            ivIcon1.setOnClickListener {
                val comm = Community()
                community.getId()?.let { it1 -> comm.setId(it1) }
                community.getName()?.let { it1 -> comm.setName(it1) }
                communicator.passCommunity(comm)
            }

            ivIcon2.setOnClickListener {
                val comm = Community()
                community.getId()?.let { it1 -> comm.setId(it1) }
                community.getName()?.let { it1 -> comm.setName(it1) }
                communicator.passCommunity(comm)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false)
        communicator = parent.context as Communicator
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val community = events.get(position)
        ParseObject.fetchAllIfNeeded(allEventsImg1)
        ParseObject.fetchAllIfNeeded(allEventsImg2)
        val images1 = allEventsImg1.get(position)
        val images2 = allEventsImg2.get(position)
        holder.bind(community, images1, images2)
    }

    override fun getItemCount(): Int {
        return events.size
    }
}