package com.example.mxer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.parse.ParseObject

class EventsAdapter(
    val context: Context,
    private val events: ArrayList<Community>,
    private val allEventsImg1: ArrayList<Community>,
    private val allEventsImg2: ArrayList<Community>
): RecyclerView.Adapter<EventsAdapter.ViewHolder>() {
    private lateinit var communicator: Communicator
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        private val tvName: TextView
        private val ivIcon1: ImageView
        private val ivIcon2: ImageView
        init {
            tvName = itemView.findViewById(R.id.tvCommName)
            ivIcon1 = itemView.findViewById(R.id.ivIcon1)
            ivIcon2 = itemView.findViewById(R.id.ivIcon2)
        }
        fun bind(community: Community, images1: Community, images2: Community) {
            tvName.text = community.getName()
            Glide.with(itemView.context)
                .asBitmap()
                .load(images1.getIcon()?.url)
                .fitCenter()
                .apply(RequestOptions().override(500, 500))
                .placeholder(R.drawable.ic_launcher_background)
                .transform(RoundedCorners(50))
                .into(ivIcon1)
            Glide.with(itemView.context)
                .asBitmap()
                .load(images2.getIcon()?.url)
                .fitCenter()
                .apply(RequestOptions().override(500, 500))
                .placeholder(R.drawable.ic_launcher_background)
                .transform(RoundedCorners(50))
                .into(ivIcon2)

            ivIcon1.setOnClickListener {
                val comm = Community()
                community.objectId?.let { it1 -> comm.setId(it1) }
                community.getName()?.let { it1 -> comm.setName(it1) }
                communicator.passCommunity(comm)
            }

            ivIcon2.setOnClickListener {
                val comm = Community()
                community.objectId?.let { it1 -> comm.setId(it1) }
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
        val community = events[position]
        ParseObject.fetchAllIfNeeded(allEventsImg1)
        ParseObject.fetchAllIfNeeded(allEventsImg2)
        val images1 = allEventsImg1[position]
        val images2 = allEventsImg2[position]
        holder.bind(community, images1, images2)
    }

    override fun getItemCount(): Int {
        return events.size
    }
}