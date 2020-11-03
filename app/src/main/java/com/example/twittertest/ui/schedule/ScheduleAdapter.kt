package com.example.twittertest.ui.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.twittertest.database.TweetSchedule
import com.example.twittertest.databinding.TextScheduleItemBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ScheduleAdapter(val deleteClickListener: DeleteScheduleListener,
    val editClickListener: EditScheduleListener): RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

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

        val binding = TextScheduleItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(val binding: TextScheduleItemBinding) : RecyclerView.ViewHolder(binding.root){
        val content: TextView = binding.textScheduleContent
        val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
        fun bind(item: TweetSchedule){
            binding.tweetSchedule = item
            binding.textScheduleScheduledDateTime.text = formatScheduleDateTime(item.schedule)
            binding.textScheduleContent.text = item.tweetContent
        }

        fun formatScheduleDateTime(schedule: LocalDateTime?): String{
            return dtf.format(schedule)
        }

    }
}

class DeleteScheduleListener(val clickListener: (tweetId: Long) -> Unit) {
    fun onClick(tweetSchedule: TweetSchedule) = clickListener(tweetSchedule.id)
}

class EditScheduleListener(val clickListener: (tweetId: Long) -> Unit) {
    fun onClick(tweetSchedule: TweetSchedule) = clickListener(tweetSchedule.id)
}
