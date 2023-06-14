package com.example.courcework.app.di.newstream

import com.example.courcework.app.presentation.screens.channels.stream.newstreamdialog.NewStreamDialogFragment
import dagger.Component

@NewStreamScope
@Component(dependencies = [NewStreamDeps::class], modules = [NewStreamModule::class])
interface NewStreamComponent {

    fun inject(fragment: NewStreamDialogFragment)

    @Component.Factory
    interface Factory {
        fun create(deps: NewStreamDeps): NewStreamComponent
    }
}