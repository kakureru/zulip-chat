package com.example.courcework.app.presentation.screens.channels

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.courcework.app.presentation.screens.channels.stream.StreamsFragment

class StreamsPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> StreamsFragment.newSubscribedInstance()
            else -> StreamsFragment.newAllInstance()
        }
}