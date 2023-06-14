package com.example.courcework.app.presentation.screens.channels

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.courcework.R
import com.example.courcework.app.di.channels.DaggerChannelsComponent
import com.example.courcework.app.getAppComponent
import com.example.courcework.app.presentation.ViewModelFactory
import com.example.courcework.databinding.FragmentChannelsBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChannelsFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val vm: ChannelsViewModel by viewModels { viewModelFactory }
    private lateinit var binding: FragmentChannelsBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerChannelsComponent.factory().create(context.getAppComponent()).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChannelsBinding.inflate(layoutInflater)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = StreamsPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.subscribed)
                else -> tab.text = resources.getString(R.string.all_streams)
            }
        }.attach()
        vm.state.render()
    }

    private fun SharedFlow<ChannelsState>.render() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@render.collect { state ->
                with(binding) {
                    title.text =
                        if (state.isConnectionAvailable) requireContext().resources.getString(R.string.channels)
                        else requireContext().resources.getString(R.string.waiting_for_connection)
                }
            }
        }
    }
}