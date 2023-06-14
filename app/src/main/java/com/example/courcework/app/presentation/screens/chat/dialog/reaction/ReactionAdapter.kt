package com.example.courcework.app.presentation.screens.chat.dialog.reaction

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.courcework.app.presentation.screens.chat.custom.sp
import com.example.courcework.app.presentation.screens.chat.model.EmojiItem

class ReactionAdapter(
    val onItemClick: (reaction: EmojiItem) -> Unit
) : ListAdapter<EmojiItem, ReactionAdapter.ReactionViewHolder>(DiffCallback) {

    inner class ReactionViewHolder(private val textView: TextView) : RecyclerView.ViewHolder(textView) {
        init {
            textView.setOnClickListener{ onItemClick(getItem(adapterPosition)) }
        }
        fun bind(item: EmojiItem) {
            textView.text = item.getCodeString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ReactionViewHolder(TextView(parent.context).apply { textSize = 10f.sp(parent.context) })

    override fun onBindViewHolder(holder: ReactionViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<EmojiItem>() {
            override fun areItemsTheSame(oldItem: EmojiItem, newItem: EmojiItem) = oldItem.name == newItem.name
            override fun areContentsTheSame(oldItem: EmojiItem, newItem: EmojiItem) = oldItem == newItem
        }
    }
}