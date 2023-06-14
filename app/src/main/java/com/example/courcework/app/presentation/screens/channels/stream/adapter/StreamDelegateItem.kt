package com.example.courcework.app.presentation.screens.channels.stream.adapter

import com.example.courcework.app.presentation.adapter.DelegateItem
import com.example.courcework.app.presentation.screens.channels.stream.model.StreamItem

class StreamDelegateItem(val id: Int, private val value: StreamItem) : DelegateItem {

    override fun content(): Any = value

    override fun id(): Int = id

    override fun compareToOther(other: DelegateItem): Boolean =
        (other as StreamDelegateItem).content() == value
}