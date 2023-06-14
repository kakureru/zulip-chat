package com.example.courcework.app.presentation.screens.profile

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import com.example.courcework.R
import com.example.courcework.app.di.profile.DaggerProfileComponent
import com.example.courcework.app.getAppComponent
import com.example.courcework.databinding.FragmentProfileBinding
import com.example.courcework.domain.model.Presence
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import javax.inject.Inject

class ProfileFragment :
    ElmFragment<ProfileEvent, ProfileEffect, ProfileState>() {

    private val isOwnUser by lazy { arguments?.getBoolean(ARG_OWN_USER) ?: throw IllegalArgumentException() }
    private val userId by lazy { arguments?.getInt(ARG_CONTACT_ID) }

    @Inject lateinit var factory: ProfileStoreFactory.Factory
    override val storeHolder: StoreHolder<ProfileEvent, ProfileEffect, ProfileState> =
        LifecycleAwareStoreHolder(lifecycle) { factory.create(isOwnUser, userId).create() }
    override val initEvent: ProfileEvent = ProfileEvent.UI.LoadProfile

    private lateinit var binding: FragmentProfileBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerProfileComponent.factory().create(context.getAppComponent()).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            toolbar.isVisible = !isOwnUser
            btnLogout.setOnClickListener { store.accept(ProfileEvent.UI.LogoutClick) }
            btnBack.setOnClickListener { store.accept(ProfileEvent.UI.BackPressed) }
        }
    }

    override fun render(state: ProfileState) {
        with(binding) {
            shimmer.root.isVisible = state.isLoading
            profileGroup.isVisible = state.data != null
            state.data?.let {
                btnLogout.isVisible = isOwnUser
                name.text = it.name
                status.setStatus(it.presence)
                image.load(it.avatarUrl) { placeholder(R.drawable.ic_face) }
            }
        }
    }

    override fun handleEffect(effect: ProfileEffect) {
        when (effect) {
            is ProfileEffect.Error -> Toast.makeText(requireContext(), effect.msg, Toast.LENGTH_LONG).show()
        }
    }

    private fun TextView.setStatus(presence: Presence) {
        text = when(presence) {
            Presence.OFFLINE -> {
                setTextColor(Color.RED)
                resources.getString(R.string.status_offline)
            }
            Presence.IDLE -> {
                setTextColor(Color.YELLOW)
                resources.getString(R.string.status_idle)
            }
            Presence.ACTIVE -> {
                setTextColor(Color.GREEN)
                resources.getString(R.string.status_active)
            }
        }
    }

    companion object {
        private const val ARG_CONTACT_ID = "ARG_CONTACT_ID"
        private const val ARG_OWN_USER = "ARG_OWN_USER"

        fun getOwnUserInstance(): Fragment {
            return ProfileFragment().apply {
                arguments = bundleOf(ARG_OWN_USER to true)
            }
        }

        fun getInstance(contactId: Int): Fragment {
            return ProfileFragment().apply {
                arguments = bundleOf(ARG_OWN_USER to false, ARG_CONTACT_ID to contactId)
            }
        }
    }
}