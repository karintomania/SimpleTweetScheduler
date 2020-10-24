package com.example.twittertest.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.twittertest.R
import com.example.twittertest.database.TweetSchedule
import com.example.twittertest.databinding.TextHistoryItemBinding
import org.jetbrains.annotations.NotNull
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HistoryAdapter(val deleteClickListener: DeleteHistoryListener,
    val editClickListener: EditHistoryListener): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    var data =  listOf<TweetSchedule>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
        holder.binding.editClickListener = editClickListener
        holder.binding.deleteClickListener = deleteClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = TextHistoryItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(val binding: TextHistoryItemBinding) : RecyclerView.ViewHolder(binding.root){
        val content: TextView = binding.textHistoryContent
        val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
        fun bind(item: TweetSchedule){
            binding.tweetSchedule = item
            binding.textHistoryScheduledDateTime.text = formatScheduleDateTime(item.schedule)
            binding.textHistoryContent.text = item.tweetContent
        }

        fun formatScheduleDateTime(schedule: LocalDateTime?): String{
            return dtf.format(schedule)
        }

    }
}

class DeleteHistoryListener(val clickListener: (tweetId: Long) -> Unit) {
    fun onClick(tweetSchedule: TweetSchedule) = clickListener(tweetSchedule.id)
}

class EditHistoryListener(val clickListener: (tweetId: Long) -> Unit) {
    fun onClick(tweetSchedule: TweetSchedule) = clickListener(tweetSchedule.id)
}
