package com.example.courcework.app.presentation.screens.chat.adapter

import android.text.Html
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.courcework.R
import com.example.courcework.app.presentation.adapter.AdapterDelegate
import com.example.courcework.app.presentation.adapter.DelegateItem
import com.example.courcework.app.presentation.screens.chat.custom.CustomMessageCallback
import com.example.courcework.app.presentation.screens.chat.custom.CustomOtherMessage
import com.example.courcework.app.presentation.screens.chat.model.OtherMessageItem
import com.example.courcework.app.presentation.setClickableText

class OtherMessageDelegate(
    private val imageGetterProvider: (textView: TextView) -> Html.ImageGetter,
    private val customMessageCallback: CustomMessageCallback
) : AdapterDelegate {

    inner class ViewHolder(private val msgView: CustomOtherMessage) : RecyclerView.ViewHolder(msgView) {

        private val imageGetter = imageGetterProvider(msgView.message)

        fun bind(messageItem: OtherMessageItem) {
            msgView.apply {
                name.text = messageItem.author
                message.setClickableText(messageItem.text, imageGetter) { url -> customMessageCallback.onLinkClick(url) }
                avatar.load(messageItem.avatarUrl) { placeholder(R.drawable.ic_face) }
                setReactions(messageItem.reactions)
                onClick = { reaction -> customMessageCallback.onReactionClick(reaction.emoji, messageItem.id) }
                onLongClick = { customMessageCallback.onLongClick(messageItem.id) }
                onAddReactionClick = { customMessageCallback.onNewReactionClick(messageItem.id) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(CustomOtherMessage(parent.context))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DelegateItem, position: Int) {
        (holder as ViewHolder).bind(item.content() as OtherMessageItem)
    }

    override fun isOfViewType(item: DelegateItem) = item is OtherMessageDelegateItem
}