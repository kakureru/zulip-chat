package com.example.courcework.screen

import android.view.View
import com.example.courcework.R
import com.example.courcework.app.presentation.screens.chat.ChatFragment
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KSnackbar
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

class ChatScreen : KScreen<ChatScreen>() {
    override val layoutId: Int = R.layout.fragment_chat
    override val viewClass: Class<*> = ChatFragment::class.java

    val messageList = KRecyclerView({ withId(R.id.list_messages) }, { itemType { MessageItem(it) } })
    val errorMessage = KSnackbar()

    class MessageItem(parent: Matcher<View>) : KRecyclerItem<MessageItem>(parent) {
        val date = KTextView(parent) { withId(R.id.date) }
        val reactions = KView(parent) { withId(R.id.reactions) }
    }
}