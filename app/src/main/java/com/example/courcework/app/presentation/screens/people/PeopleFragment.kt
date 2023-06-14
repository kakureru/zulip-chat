package com.example.courcework.app.presentation.screens.people

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import com.example.courcework.R
import com.example.courcework.app.di.people.DaggerPeopleComponent
import com.example.courcework.app.getAppComponent
import com.example.courcework.app.presentation.adapter.ShimmerAdapter
import com.example.courcework.databinding.FragmentPeopleBinding
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import javax.inject.Inject

class PeopleFragment : ElmFragment<PeopleEvent, PeopleEffect, PeopleState>() {

    @Inject lateinit var factory: PeopleStoreFactory
    override val storeHolder: StoreHolder<PeopleEvent, PeopleEffect, PeopleState> = LifecycleAwareStoreHolder(lifecycle) { factory.create() }
    override val initEvent: PeopleEvent = PeopleEvent.UI.Initialize

    private lateinit var binding: FragmentPeopleBinding
    private lateinit var contactAdapter: ContactAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerPeopleComponent
            .factory()
            .create(context.getAppComponent())
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPeopleBinding.inflate(layoutInflater)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            addMenuProvider(menuProvider, viewLifecycleOwner)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactAdapter = ContactAdapter { contactId -> store.accept(PeopleEvent.UI.ContactClick(contactId)) }

        binding.apply {
            listContacts.adapter = contactAdapter
            shimmer.adapter = ShimmerAdapter(requireContext(), R.layout.contact_item_shimmer, 10)
        }
    }

    private val menuProvider = object : MenuProvider {
        override fun onMenuItemSelected(menuItem: MenuItem) = false
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menu.clear()
            menuInflater.inflate(R.menu.action_bar, menu)
            val searchView = menu.findItem(R.id.action_search).actionView as SearchView

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(text: String?) = false
                override fun onQueryTextChange(text: String?): Boolean {
                    if (searchView.isIconified) return true
                    text?.let { store.accept(PeopleEvent.UI.SearchTextChanged(text)) }
                    return true
                }
            })
        }
    }

    override fun render(state: PeopleState) {
        with(binding) {
            shimmer.isVisible = state.isLoading
            loader.isVisible = state.isSearching
            listContacts.isVisible = state.data != null
            title.text =
                if (state.isConnectionAvailable) requireContext().resources.getString(R.string.people)
                else requireContext().resources.getString(R.string.waiting_for_connection)
            state.data?.let {
                contactAdapter.submitList(it)
            }
        }
    }

    override fun handleEffect(effect: PeopleEffect) {
        when (effect) {
            is PeopleEffect.Error -> Toast.makeText(requireContext(), effect.msg, Toast.LENGTH_LONG).show()
        }
    }
}