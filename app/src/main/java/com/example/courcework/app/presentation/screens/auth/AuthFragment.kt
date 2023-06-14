package com.example.courcework.app.presentation.screens.auth

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.courcework.app.di.auth.DaggerAuthComponent
import com.example.courcework.app.getAppComponent
import com.example.courcework.data.network.model.users.AuthCredentials
import com.example.courcework.databinding.FragmentAuthBinding
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import javax.inject.Inject

class AuthFragment : ElmFragment<AuthEvent, AuthEffect, AuthState>() {

    @Inject lateinit var factory: AuthStoreFactory
    override val storeHolder: StoreHolder<AuthEvent, AuthEffect, AuthState> = LifecycleAwareStoreHolder(lifecycle) { factory.create() }
    override val initEvent: AuthEvent = AuthEvent.UI.OnOpen

    private lateinit var binding: FragmentAuthBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerAuthComponent.factory().create(context.getAppComponent()).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnSignIn.setOnClickListener {
                val credentials = AuthCredentials(
                    login = etEmail.text.toString(),
                    apiKey = etApiKey.text.toString()
                )
                store.accept(AuthEvent.UI.SingInClick(credentials))
            }
            signUpText.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    override fun render(state: AuthState) {
        binding.loader.isVisible = state.isLoading
    }

    override fun handleEffect(effect: AuthEffect) {
        when (effect) {
            is AuthEffect.Error -> Toast.makeText(requireContext(), effect.msg, Toast.LENGTH_LONG).show()
        }
    }
}