package com.example.courcework.app.presentation.navigation

import android.net.Uri
import com.example.courcework.app.presentation.screens.auth.AuthFragment
import com.example.courcework.app.presentation.screens.bottom.BottomFragment
import com.example.courcework.app.presentation.screens.channels.ChannelsFragment
import com.example.courcework.app.presentation.screens.channels.stream.model.TopicId
import com.example.courcework.app.presentation.screens.chat.ChatFragment
import com.example.courcework.app.presentation.screens.chat.attachment.AttachmentFragment
import com.example.courcework.app.presentation.screens.imageviewer.ImageViewerFragment
import com.example.courcework.app.presentation.screens.people.PeopleFragment
import com.example.courcework.app.presentation.screens.profile.ProfileFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {

    fun Auth() = FragmentScreen {
        AuthFragment()
    }

    fun BottomNavigation() = FragmentScreen {
        BottomFragment()
    }

    fun Channels() = FragmentScreen {
        ChannelsFragment()
    }

    fun People() = FragmentScreen {
        PeopleFragment()
    }

    fun OwnProfile() = FragmentScreen {
        ProfileFragment.getOwnUserInstance()
    }

    fun Profile(userId: Int) = FragmentScreen {
        ProfileFragment.getInstance(userId)
    }

    fun Chat(topicId: TopicId) = FragmentScreen {
        ChatFragment.getInstance(topicId)
    }

    fun ImageViewer(imageUri: String) = FragmentScreen {
        ImageViewerFragment.getInstance(imageUri)
    }

    fun Attachment(uri: Uri, resultKey: String) = FragmentScreen {
        AttachmentFragment.getInstance(uri, resultKey)
    }
}