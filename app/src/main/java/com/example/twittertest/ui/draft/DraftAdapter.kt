package com.example.twittertest.ui.draft

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.twittertest.R
import com.example.twittertest.database.TweetSchedule
import com.example.twittertest.databinding.TextDraftItemBinding
import org.jetbrains.annotations.NotNull

class DraftAdapter(
    val deleteClickListener: DeleteDraftListener,
    val editClickListener: EditDraftListener): RecyclerView.Adapter<DraftAdapter.ViewHolder>() {

    var data =  listOf<TweetSchedule>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            TextDraftItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.binding.tweetSchedule = item
        holder.binding.editClickListener = editClickListener
        holder.binding.deleteClickListener = deleteClickListener
    }

    class ViewHolder(val binding: TextDraftItemBinding) : RecyclerView.ViewHolder(binding.root){
        val content: TextView = binding.textDraftContent
    }
}

class DeleteDraftListener(val clickListener: (tweetId: Long) -> Unit) {
    fun onClick(tweetSchedule: TweetSchedule) = clickListener(tweetSchedule.id)
}

class EditDraftListener(val clickListener: (tweetId: Long) -> Unit) {
   fun onClick(tweetSchedule: TweetSchedule) = clickListener(tweetSchedule.id)
}