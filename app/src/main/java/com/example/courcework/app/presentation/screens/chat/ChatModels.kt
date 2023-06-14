package com.example.courcework.app.presentation.screens.chat

import android.content.ClipboardManager
import android.net.Uri
import com.example.courcework.app.presentation.adapter.DelegateItem
import com.example.courcework.app.presentation.screens.channels.stream.model.TopicId
import com.example.courcework.app.presentation.screens.chat.adapter.OwnMessageDelegateItem
import com.example.courcework.app.presentation.screens.chat.dialog.reaction.ReactionDialogData
import com.example.courcework.app.presentation.screens.chat.dialog.topicpicker.TopicPickerDialogData
import com.example.courcework.app.presentation.screens.chat.model.EmojiItem
import com.example.courcework.app.presentation.screens.chat.model.MessageEditData

data class ChatState (
    val isLoading: Boolean = false,
    val isTyping: Boolean = false,
    val isConnectionAvailable: Boolean = false,
    val editingMessage: MessageEditData? = null,
    val data: List<DelegateItem>? = null,
)

sealed class ChatEvent {

    sealed class UI {
        object Initialize : ChatEvent()
        class Scroll(val firstVisibleItemPosition: Int) : ChatEvent()
        class InputChanged(val text: String) : ChatEvent()
        class SendMessage(val content: String) : ChatEvent()
        class NewAttachment(val uri: Uri) : ChatEvent()
        class OtherMessageLongClick(val messageId: Int) : ChatEvent()
        class OwnMessageLongClick(val messageId: Int) : ChatEvent()
        class ReactionClick(val emojiItem: EmojiItem, val messageId: Int) : ChatEvent()
        class MessageLinkClick(val url: String) : ChatEvent()
        object CancelEdit : ChatEvent()
        object BackPressed : ChatEvent()

        // Message actions

        class AddReaction(val messageId: Int) : ChatEvent()
        class NewReactionSelected(val emoji: EmojiItem, val messageId: Int) : ChatEvent()
        class CopyMessageToClipboard(val messageId: Int, val clipboardManager: ClipboardManager) : ChatEvent()
        class EditMessage(val messageId: Int) : ChatEvent()
        class MessageEdited(val messageId: Int, val newContent: String) : ChatEvent()
        class ChangeMessageTopic(val messageId: Int) : ChatEvent()
        class NewTopicSelected(val messageId: Int, val newTopicId: TopicId) : ChatEvent()
        class DeleteMessage(val messageId: Int) : ChatEvent()
    }

    sealed class Internal {
        class MessagesLoaded(val data: List<DelegateItem>) : ChatEvent()
        class Error(val msg: String) : ChatEvent()
        class ReactionDialogReady(val dialogData: ReactionDialogData): ChatEvent()
        class TopicPickerDialogReady(val dialogData: TopicPickerDialogData): ChatEvent()
        class MessageSent(val message: OwnMessageDelegateItem) : ChatEvent()
        class ConnectionStatusChanged(val isAvailable: Boolean) : ChatEvent()
    }
}

sealed class ChatEffect {
    class Error(val msg: String): ChatEffect()
    object MessageSent : ChatEffect()
    class EditingStarted(val content: String) : ChatEffect()
    class ReactionDialog(val data: ReactionDialogData) : ChatEffect()
    class OtherMessageActionDialog(val messageId: Int) : ChatEffect()
    class OwnMessageActionDialog(val messageId: Int) : ChatEffect()
    class TopicPickerDialog(val data: TopicPickerDialogData) : ChatEffect()
}

sealed class ChatCommand {
    object LoadMessages : ChatCommand()
    object ObserveConnection : ChatCommand()
    object LoadMore : ChatCommand()
    object OnBackPressed : ChatCommand()
    class SendMessage(val text: String) : ChatCommand()
    class LoadAttachment(val uri: Uri) : ChatCommand()
    class AddReaction(val emoji: EmojiItem, val messageId: Int) : ChatCommand()
    class OnReactionClick(val emojiItem: EmojiItem, val messageId: Int) : ChatCommand()
    class OnMessageLinkClick(val url: String) : ChatCommand()
    class PrepareReactionDialog(val messageId: Int) : ChatCommand()
    class PrepareTopicPickerDialog(val messageId: Int) : ChatCommand()
    class CopyMessageToClipboard(val text: String, val clipboardManager: ClipboardManager) : ChatCommand()
    class DeleteMessage(val messageId: Int) : ChatCommand()
    class EditMessage(val messageId: Int, val newContent: String) : ChatCommand()
    class ChangeMessageTopic(val messageId: Int, val newTopicId: TopicId) : ChatCommand()
}