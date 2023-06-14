package com.example.courcework.app.presentation.screens.chat.adapter

import android.text.Html
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.courcework.app.presentation.adapter.AdapterDelegate
import com.example.courcework.app.presentation.adapter.DelegateItem
import com.example.courcework.app.presentation.screens.chat.custom.CustomMessageCallback
import com.example.courcework.app.presentation.screens.chat.custom.CustomOwnMessage
import com.example.courcework.app.presentation.screens.chat.model.OwnMessageItem
import com.example.courcework.app.presentation.setClickableText

class OwnMessageDelegate(
    private val imageGetterProvider: (textView: TextView) -> Html.ImageGetter,
    private val customMessageCallback: CustomMessageCallback
) : AdapterDelegate {

    inner class ViewHolder(private val msgView: CustomOwnMessage) : RecyclerView.ViewHolder(msgView) {

        private val imageGetter = imageGetterProvider(msgView.message)

        fun bind(messageItem: OwnMessageItem) {
            msgView.apply {
                message.setClickableText(messageItem.text, imageGetter) { url -> customMessageCallback.onLinkClick(url) }
                sendStatus.isVisible = messageItem.isSending
                setReactions(messageItem.reactions)
                onClick = { reaction -> customMessageCallback.onReactionClick(reaction.emoji, messageItem.id) }
                onLongClick = { customMessageCallback.onLongClick(messageItem.id) }
                onAddReactionClick = { customMessageCallback.onNewReactionClick(messageItem.id) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(CustomOwnMessage(parent.context))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DelegateItem, position: Int) {
        (holder as ViewHolder).bind(item.content() as OwnMessageItem)
    }

    override fun isOfViewType(item: DelegateItem) = item is OwnMessageDelegateItem
}