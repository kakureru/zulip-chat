package com.example.courcework.app.presentation.screens.channels.stream.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.courcework.R
import com.example.courcework.app.presentation.adapter.AdapterDelegate
import com.example.courcework.app.presentation.adapter.DelegateItem
import com.example.courcework.app.presentation.screens.channels.stream.model.StreamItem
import com.example.courcework.databinding.StreamItemBinding

class StreamDelegate(private val onClick: (streamId: Int) -> Unit) : AdapterDelegate {

    inner class StreamViewHolder(private val binding: StreamItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StreamItem) {
            binding.apply {
                name.text = item.name
                if (item.expanded)
                    imgArrow.setImageDrawable(R.drawable.ic_keyboard_arrow_up)
                else
                    imgArrow.setImageDrawable(R.drawable.ic_keyboard_arrow_down)
            }
        }
    }

    private fun ImageView.setImageDrawable(id: Int) =
        this.setImageDrawable(ResourcesCompat.getDrawable(context.resources, id,null))

    override fun onCreateViewHolder(parent: ViewGroup) = StreamViewHolder(
        StreamItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DelegateItem, position: Int) {
        val stream = item.content() as StreamItem
        (holder as StreamViewHolder).bind(stream)
        holder.itemView.setOnClickListener { onClick(stream.id) }
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is StreamDelegateItem
}