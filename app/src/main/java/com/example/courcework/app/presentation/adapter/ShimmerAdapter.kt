package com.example.courcework.app.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.courcework.R

class ShimmerAdapter(
    private val context: Context,
    private val layoutId: Int,
    private val count: Int = 5
) : BaseAdapter() {
    override fun getCount() = count
    override fun getItem(position: Int): Any = Any()
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View =
        LayoutInflater.from(context).inflate(layoutId, parent, false)
}