package com.example.courcework.app.presentation.screens.chat

import android.text.Html
import androidx.media3.common.util.UnstableApi
import com.example.courcework.app.presentation.screens.chat.ChatActor.Companion.shouldFetchMore
import com.example.courcework.app.presentation.screens.chat.model.MessageEditData
import com.example.courcework.app.presentation.screens.chat.model.MessageItem
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

@UnstableApi class ChatReducer : DslReducer<ChatEvent, ChatState, ChatEffect, ChatCommand>() {
    override fun Result.reduce(event: ChatEvent): Any? = when (event) {

        is ChatEvent.UI.Initialize -> {
            state { ChatState(isLoading = true) }
            commands {
                +ChatCommand.LoadMessages
                +ChatCommand.ObserveConnection
            }
        }

        is ChatEvent.UI.Scroll -> {
            commands { +if (event.shouldFetchMore()) ChatCommand.LoadMore else null }
        }

        is ChatEvent.UI.SendMessage -> {
            commands { +ChatCommand.SendMessage(event.content) }
        }

        is ChatEvent.UI.NewAttachment -> {
            commands { +ChatCommand.LoadAttachment(event.uri) }
        }

        is ChatEvent.UI.ReactionClick -> {
            commands { +ChatCommand.OnReactionClick(event.emojiItem, event.messageId) }
        }

        is ChatEvent.UI.MessageLinkClick -> {
            commands { +ChatCommand.OnMessageLinkClick(event.url) }
        }

        is ChatEvent.UI.OtherMessageLongClick -> {
            effects { +ChatEffect.OtherMessageActionDialog(messageId = event.messageId) }
        }

        is ChatEvent.UI.OwnMessageLongClick -> {
            effects { +ChatEffect.OwnMessageActionDialog(messageId = event.messageId) }
        }

        is ChatEvent.UI.InputChanged -> {
            state {
                if (state.editingMessage != null)
                    state
                else if (event.text.isEmpty())
                    state.copy(isTyping = false)
                else
                    state.copy(isTyping = true)
            }
        }

        is ChatEvent.UI.CancelEdit -> {
            state { state.copy(editingMessage = null) }
        }

        ChatEvent.UI.BackPressed -> {
            commands { +ChatCommand.OnBackPressed }
        }

        // Message actions

        is ChatEvent.UI.AddReaction -> {
            commands { +ChatCommand.PrepareReactionDialog(event.messageId) }
        }

        is ChatEvent.UI.NewReactionSelected -> {
            commands { +ChatCommand.AddReaction(event.emoji, event.messageId) }
        }

        is ChatEvent.UI.CopyMessageToClipboard -> {
            state.data?.find { it.id() == event.messageId }?.let {
                commands {
                    +ChatCommand.CopyMessageToClipboard(
                        text = (it.content() as MessageItem).text,
                        clipboardManager = event.clipboardManager
                    )
                }
            }
        }

        is ChatEvent.UI.EditMessage -> {
            state.data?.find { it.id() == event.messageId }?.let {
                val content = Html.fromHtml((it.content() as MessageItem).text, Html.FROM_HTML_MODE_LEGACY).trim().toString()
                state { state.copy(editingMessage = MessageEditData(event.messageId, content)) }
                effects { +ChatEffect.EditingStarted(content) }
            }
        }

        is ChatEvent.UI.MessageEdited -> {
            state { state.copy(editingMessage = null) }
            commands { +ChatCommand.EditMessage(event.messageId, event.newContent) }
        }

        is ChatEvent.UI.ChangeMessageTopic -> {
            commands { +ChatCommand.PrepareTopicPickerDialog(event.messageId) }
        }

        is ChatEvent.UI.NewTopicSelected -> {
            commands { +ChatCommand.ChangeMessageTopic(event.messageId, event.newTopicId) }
        }

        is ChatEvent.UI.DeleteMessage -> {
            commands { +ChatCommand.DeleteMessage(event.messageId) }
        }

        // Internal

        is ChatEvent.Internal.MessagesLoaded -> {
            state { state.copy(data = event.data, isLoading = false) }
        }

        is ChatEvent.Internal.Error -> {
            effects { +ChatEffect.Error(event.msg) }
        }

        is ChatEvent.Internal.ReactionDialogReady -> {
            effects { +ChatEffect.ReactionDialog(event.dialogData) }
        }

        is ChatEvent.Internal.TopicPickerDialogReady -> {
            effects { +ChatEffect.TopicPickerDialog(data = event.dialogData) }
        }

        is ChatEvent.Internal.MessageSent -> {
            state { state.copy(data = state.data?.plus(event.message)) }
            effects { +ChatEffect.MessageSent }
        }

        is ChatEvent.Internal.ConnectionStatusChanged -> {
            state { state.copy(isConnectionAvailable = event.isAvailable) }
        }
    }
}