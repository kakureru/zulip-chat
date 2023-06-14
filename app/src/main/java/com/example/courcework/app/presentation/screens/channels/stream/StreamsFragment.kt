package com.example.courcework.app.presentation.screens.channels.stream

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.courcework.R
import com.example.courcework.app.di.stream.DaggerStreamComponent
import com.example.courcework.app.getAppComponent
import com.example.courcework.app.presentation.adapter.DelegateListAdapter
import com.example.courcework.app.presentation.adapter.ShimmerAdapter
import com.example.courcework.app.presentation.screens.channels.stream.adapter.StreamDelegate
import com.example.courcework.app.presentation.screens.channels.stream.adapter.TopicDelegate
import com.example.courcework.app.presentation.screens.channels.stream.newstreamdialog.NewStreamDialogFragment
import com.example.courcework.databinding.FragmentStreamsBinding
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import javax.inject.Inject

class StreamsFragment : ElmFragment<StreamEvent, StreamEffect, StreamState>() {

    private val subscribed by lazy { arguments?.getBoolean(ARG_SUBSCRIBED) ?: throw IllegalArgumentException() }

    @Inject lateinit var factory: StreamStoreFactory.Factory
    override val storeHolder: StoreHolder<StreamEvent, StreamEffect, StreamState> = LifecycleAwareStoreHolder(lifecycle) { factory.create(subscribed).create() }
    override val initEvent: StreamEvent = StreamEvent.UI.Initialize

    private lateinit var binding: FragmentStreamsBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerStreamComponent.factory().create(context.getAppComponent()).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStreamsBinding.inflate(layoutInflater)
        (requireActivity() as MenuHost).addMenuProvider(menuProvider, viewLifecycleOwner)
        return binding.root
    }

    private lateinit var channelAdapter: DelegateListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        channelAdapter = DelegateListAdapter().apply {
            addDelegate(StreamDelegate { streamId -> store.accept(
                StreamEvent.UI.StreamClick(
                    streamId
                )
            ) })
            addDelegate(TopicDelegate { topicId -> store.accept(StreamEvent.UI.TopicClick(topicId)) })
        }
        binding.apply {
            listStreams.adapter = channelAdapter
            shimmer.adapter = ShimmerAdapter(requireContext(), R.layout.stream_item_shimmer)
        }
    }

    override fun render(state: StreamState) {
        with(binding) {
            shimmer.isVisible = state.isInitLoading
            loader.isVisible = state.isLoading
            listStreams.isVisible = state.data != null
            state.data?.let {
                channelAdapter.submitList(it)
            }
        }
    }

    override fun handleEffect(effect: StreamEffect) {
        when (effect) {
            is StreamEffect.Error -> Toast.makeText(requireContext(), effect.msg, Toast.LENGTH_LONG).show()
        }
    }

    private val menuProvider = object : MenuProvider {
        override fun onMenuItemSelected(menuItem: MenuItem) = false
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menu.clear()
            menuInflater.inflate(R.menu.channels_action_bar, menu)
            val searchView = menu.findItem(R.id.action_search).actionView as SearchView

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(text: String?) = false
                override fun onQueryTextChange(text: String?): Boolean {
                    if (searchView.isIconified) return true
                    text?.let { store.accept(StreamEvent.UI.SearchTextChanged(text)) }
                    return true
                }
            })

            menu.findItem(R.id.action_new_stream).setOnMenuItemClickListener {
                NewStreamDialogFragment.show(childFragmentManager)
                true
            }
        }
    }

    companion object {
        const val ARG_SUBSCRIBED = "ARG_SUBSCRIBED"

        fun newSubscribedInstance(): Fragment {
            return StreamsFragment().apply {
                arguments = bundleOf(ARG_SUBSCRIBED to true)
            }
        }

        fun newAllInstance(): Fragment {
            return StreamsFragment().apply {
                arguments = bundleOf(ARG_SUBSCRIBED to false)
            }
        }
    }
}