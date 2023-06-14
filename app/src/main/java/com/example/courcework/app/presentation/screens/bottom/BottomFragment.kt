package com.example.courcework.app.presentation.screens.bottom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.courcework.R
import com.example.courcework.app.di.bottom.Bottom
import com.example.courcework.app.di.bottom.DaggerBottomComponent
import com.example.courcework.databinding.FragmentBottomNavigationBinding
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import javax.inject.Inject

class BottomFragment : ElmFragment<BottomEvent, BottomEffect, BottomState>() {

    @Inject lateinit var factory: BottomStoreFactory
    override val storeHolder: StoreHolder<BottomEvent, BottomEffect, BottomState> = LifecycleAwareStoreHolder(lifecycle) { factory.create() }
    override val initEvent: BottomEvent = BottomEvent.UI.OpenDefaultTab

    @Inject @Bottom lateinit var navigatorHolder: NavigatorHolder
    private val navigator by lazy { AppNavigator(requireActivity(), R.id.container, childFragmentManager) }

    private lateinit var binding: FragmentBottomNavigationBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerBottomComponent.builder().build().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomNavigationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationBar()
    }

    override fun render(state: BottomState) {
        binding.bottomNavigationView.menu.apply {
            when (state) {
                BottomState.Channels -> findItem(R.id.miChannels).isChecked = true
                BottomState.People -> findItem(R.id.miPeople).isChecked = true
                BottomState.Profile -> findItem(R.id.miProfile).isChecked = true
            }
        }
    }

    private fun setupNavigationBar() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            store.accept(BottomEvent.UI.BottomNavClick(item))
            false
        }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}