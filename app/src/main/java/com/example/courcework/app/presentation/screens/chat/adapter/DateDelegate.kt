package com.example.courcework.app.presentation.screens.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.courcework.app.presentation.adapter.AdapterDelegate
import com.example.courcework.app.presentation.adapter.DelegateItem
import com.example.courcework.app.presentation.screens.chat.model.DateItem
import com.example.courcework.databinding.DateItemBinding

class DateDelegate : AdapterDelegate {

    inner class ViewHolder(private val binding: DateItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: DateItem) {
            with(binding) {
                date.text = model.date
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            DateItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DelegateItem, position: Int) {
        (holder as ViewHolder).bind(item.content() as DateItem)
    }

    override fun isOfViewType(item: DelegateItem) = item is DateDelegateItem
}