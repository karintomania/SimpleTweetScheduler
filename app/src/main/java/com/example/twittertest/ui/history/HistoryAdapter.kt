package com.example.twittertest.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.twittertest.R
import com.example.twittertest.database.TweetSchedule
import com.example.twittertest.ui.TextItemViewHolder

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    var data =  listOf<TweetSchedule>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.content.text = item.tweetContent.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.text_history_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val content: TextView = itemView.findViewById(R.id.text_historyContent)
    }
}

