package com.example.courcework.app.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class DelegateListAdapter : ListAdapter<DelegateItem, RecyclerView.ViewHolder>(DiffCallback) {

    private val delegates: MutableList<AdapterDelegate> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        delegates[viewType].onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        delegates[getItemViewType(position)].onBindViewHolder(holder, getItem(position), position)

    fun addDelegate(delegate: AdapterDelegate) {
        delegates.add(delegate)
    }

    override fun getItemViewType(position: Int): Int =
        delegates.indexOfFirst { it.isOfViewType(currentList[position]) }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<DelegateItem>() {
            override fun areItemsTheSame(oldItem: DelegateItem, newItem: DelegateItem) =
                oldItem::class == newItem::class && oldItem.id() == newItem.id()

            override fun areContentsTheSame(oldItem: DelegateItem, newItem: DelegateItem) =
                oldItem.compareToOther(newItem)
        }
    }
}