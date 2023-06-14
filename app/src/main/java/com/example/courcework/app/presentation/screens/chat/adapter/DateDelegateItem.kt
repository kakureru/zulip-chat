package com.example.courcework.app.presentation.screens.chat.adapter

import com.example.courcework.app.presentation.adapter.DelegateItem
import com.example.courcework.app.presentation.screens.chat.model.DateItem

class DateDelegateItem(private val value: DateItem) : DelegateItem {

    override fun content(): Any = value

    override fun id(): Int = value.hashCode()

    override fun compareToOther(other: DelegateItem): Boolean =
        (other as DateDelegateItem).content() == value
}