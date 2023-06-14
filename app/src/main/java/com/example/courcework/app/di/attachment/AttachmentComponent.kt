package com.example.courcework.app.di.attachment

import com.example.courcework.app.presentation.screens.chat.attachment.AttachmentFragment
import dagger.Component

@AttachmentScope
@Component(dependencies = [AttachmentDeps::class], modules = [AttachmentDataModule::class, AttachmentAppModule::class])
interface AttachmentComponent {

    fun inject(fragment: AttachmentFragment)

    @Component.Factory
    interface Factory {
        fun create(deps: AttachmentDeps): AttachmentComponent
    }
}