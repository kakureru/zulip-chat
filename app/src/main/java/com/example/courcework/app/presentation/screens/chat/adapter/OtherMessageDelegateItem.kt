package com.example.courcework.app.presentation.screens.chat.adapter

import com.example.courcework.app.presentation.adapter.DelegateItem
import com.example.courcework.app.presentation.screens.chat.model.OtherMessageItem

class OtherMessageDelegateItem(val id: Int, private val value: OtherMessageItem) : DelegateItem {

    override fun content(): Any = value

    override fun id(): Int = id

    override fun compareToOther(other: DelegateItem): Boolean =
        (other as OtherMessageDelegateItem).content() == value
}