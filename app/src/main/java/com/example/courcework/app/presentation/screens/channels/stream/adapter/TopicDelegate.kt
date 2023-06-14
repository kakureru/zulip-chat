package com.example.courcework.app.presentation.screens.channels.stream.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.courcework.R
import com.example.courcework.app.presentation.adapter.AdapterDelegate
import com.example.courcework.app.presentation.adapter.DelegateItem
import com.example.courcework.app.presentation.screens.channels.stream.model.TopicId
import com.example.courcework.app.presentation.screens.channels.stream.model.TopicItem
import com.example.courcework.databinding.TopicItemBinding
import com.google.android.material.R.attr

class TopicDelegate(private val onClick: (topicId: TopicId) -> Unit) : AdapterDelegate {

    inner class TopicViewHolder(
        context: Context,
        private val binding: TopicItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val primaryColor = context.getColorFromAttr(attr.colorPrimaryVariant, TypedValue())
        private val secondaryColor = context.getColorFromAttr(attr.colorSecondary, TypedValue())

        fun bind(item: TopicItem) {
            binding.apply {
                name.text = item.name
                messageCount.apply {
                    text = context.resources.getString(R.string.message_count, "${item.unreadMessageCount}")
                    isInvisible = !item.isUnreadCountVisible
                }
                root.setBackgroundColor(
                    if (adapterPosition % 2 != 0) primaryColor
                    else secondaryColor
                )
            }
        }
    }

    @ColorInt
    fun Context.getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = TopicViewHolder(
        parent.context,
        TopicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DelegateItem, position: Int) {
        val topic = item.content() as TopicItem
        (holder as TopicViewHolder).bind(topic)
        holder.itemView.setOnClickListener { onClick(topic.id) }
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is TopicDelegateItem
}