package com.example.courcework.app.presentation.screens.channels.stream.adapter

import com.example.courcework.app.presentation.adapter.DelegateItem
import com.example.courcework.app.presentation.screens.channels.stream.model.TopicItem

class TopicDelegateItem(private val value: TopicItem) : DelegateItem {

    override fun content(): Any = value

    override fun id(): Int = value.id.hashCode()

    override fun compareToOther(other: DelegateItem): Boolean =
        (other as TopicDelegateItem).content() == value
}