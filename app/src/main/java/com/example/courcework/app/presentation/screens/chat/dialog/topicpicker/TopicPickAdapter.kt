package com.example.courcework.app.presentation.screens.chat.dialog.topicpicker

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.courcework.app.presentation.screens.channels.stream.model.TopicId
import com.example.courcework.app.presentation.screens.channels.stream.model.TopicItem

class TopicPickAdapter(
    val onItemClick: (topicId: TopicId) -> Unit
) : ListAdapter<TopicItem, TopicPickAdapter.TopicViewHolder>(DiffCallback) {

    inner class TopicViewHolder(private val textView: TextView) : RecyclerView.ViewHolder(textView) {
        init {
            textView.setOnClickListener{ onItemClick(getItem(adapterPosition).id) }
        }
        fun bind(item: TopicItem) {
            textView.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TopicViewHolder(TextView(parent.context))

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<TopicItem>() {
            override fun areItemsTheSame(oldItem: TopicItem, newItem: TopicItem) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: TopicItem, newItem: TopicItem) = oldItem == newItem
        }
    }
}