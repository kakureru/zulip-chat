package com.example.courcework.app.presentation.screens.chat.adapter

import com.example.courcework.app.presentation.adapter.DelegateItem
import com.example.courcework.app.presentation.screens.chat.model.OwnMessageItem

class OwnMessageDelegateItem(val id: Int, private val value: OwnMessageItem) : DelegateItem {

    override fun content(): Any = value

    override fun id(): Int = id

    override fun compareToOther(other: DelegateItem): Boolean =
        (other as OwnMessageDelegateItem).content() == value
}