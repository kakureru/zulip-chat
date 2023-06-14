package com.example.courcework.app.presentation.screens.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.net.Uri
import android.text.Html
import android.webkit.MimeTypeMap
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import com.example.courcework.app.presentation.adapter.DelegateItem
import com.example.courcework.app.presentation.catchNonCancellationAndEmit
import com.example.courcework.app.presentation.connectivity.ConnectivityObserver
import com.example.courcework.app.presentation.navigation.Screens
import com.example.courcework.app.presentation.runCatchingNonCancellation
import com.example.courcework.app.presentation.screens.channels.stream.model.TopicId
import com.example.courcework.app.presentation.screens.channels.stream.model.toDomain
import com.example.courcework.app.presentation.screens.chat.adapter.DateDelegateItem
import com.example.courcework.app.presentation.screens.chat.adapter.OtherMessageDelegateItem
import com.example.courcework.app.presentation.screens.chat.adapter.OwnMessageDelegateItem
import com.example.courcework.app.presentation.screens.chat.attachment.AttachmentResult
import com.example.courcework.app.presentation.screens.chat.dialog.reaction.ReactionDialogData
import com.example.courcework.app.presentation.screens.chat.dialog.topicpicker.TopicPickerDialogData
import com.example.courcework.app.presentation.screens.chat.model.DateItem
import com.example.courcework.app.presentation.screens.chat.model.EmojiItem
import com.example.courcework.app.presentation.screens.chat.model.OtherMessageItem
import com.example.courcework.app.presentation.screens.chat.model.OwnMessageItem
import com.example.courcework.app.presentation.screens.chat.model.toDomain
import com.example.courcework.app.presentation.screens.chat.model.toOwnItem
import com.example.courcework.app.presentation.screens.chat.model.toUI
import com.example.courcework.data.network.auth.AuthHelper
import com.example.courcework.domain.model.Message
import com.example.courcework.domain.repository.FileRepository
import com.example.courcework.domain.repository.MessageRepository
import com.example.courcework.domain.usecase.ReactionClickUseCase
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import vivid.money.elmslie.coroutines.Actor
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@UnstableApi class ChatActor(
    private val topicId: TopicId,
    private val messageRepository: MessageRepository,
    private val fileRepository: FileRepository,
    private val authHelper: AuthHelper,
    private val router: Router,
    private val connectivityObserver: ConnectivityObserver,
    private val reactionClickUseCase: ReactionClickUseCase,
) : Actor<ChatCommand, ChatEvent> {

    private val attachmentResult = MutableSharedFlow<ChatEvent>()

    override fun execute(command: ChatCommand): Flow<ChatEvent> {
        return when (command) {
            ChatCommand.LoadMessages -> loadMessages()
            ChatCommand.ObserveConnection -> observeConnection()
            ChatCommand.LoadMore -> loadMore()
            ChatCommand.OnBackPressed -> onBackPressed()
            is ChatCommand.SendMessage -> sendMessage(command.text)
            is ChatCommand.LoadAttachment -> loadAttachment(command.uri)
            is ChatCommand.OnReactionClick -> onReactionClick(command.emojiItem, command.messageId)
            is ChatCommand.OnMessageLinkClick -> onMessageLinkClick(command.url)
            is ChatCommand.PrepareReactionDialog -> prepareReactionDialog(command.messageId)
            is ChatCommand.PrepareTopicPickerDialog -> prepareTopicPickerDialog(command.messageId)
            is ChatCommand.AddReaction -> addReaction(command.emoji, command.messageId)
            is ChatCommand.CopyMessageToClipboard -> copyToClipboard(command.text, command.clipboardManager)
            is ChatCommand.DeleteMessage -> deleteMessage(command.messageId)
            is ChatCommand.EditMessage -> editMessage(command.messageId, command.newContent)
            is ChatCommand.ChangeMessageTopic -> changeMessageTopic(command.messageId, command.newTopicId)
        }
    }

    private fun sendMessage(message: String): Flow<ChatEvent> = flow {
        runCatchingNonCancellation {
            messageRepository.sendMessage(topicId.toDomain(), message)
            emit(ChatEvent.Internal.MessageSent(getSendingMessage(message)))
        }.onFailure {
            emit(ChatEvent.Internal.Error("Failed to send message"))
        }
    }
    private fun loadAttachment(uri: Uri): Flow<ChatEvent> {
        router.setResultListener(ATTACHMENT_RESULT_KEY) { result ->
            onAttachmentResult(uri, result as AttachmentResult)
        }
        router.navigateTo(Screens.Attachment(uri, ATTACHMENT_RESULT_KEY))
        return attachmentResult
    }

    private fun onAttachmentResult(uri: Uri, result: AttachmentResult) = CoroutineScope(Dispatchers.IO).launch {
        runCatchingNonCancellation {
            val message = result.message.trim()

            val previewContent = getPreviewContent(message, uri.toString(), result.fileName)
            attachmentResult.emit(ChatEvent.Internal.MessageSent(getSendingMessage(previewContent)))

            val actualContent = getActualContent(message, result.link, result.fileName)
            messageRepository.sendMessage(topicId.toDomain(), actualContent)
        }.onFailure {
            attachmentResult.emit(ChatEvent.Internal.Error("Failed to send message"))
        }
    }
    private fun getPreviewContent(message: String, link: String, fileName: String) =
        "<p>$message<br>\n" +
                "<a href=\"$link\">$fileName</a></p>\n" +
                "<div class=\"message_inline_image\">" +
                "<a href=\"$link\" title=\"$fileName\">" +
                "<img src=\"$link\"></a></div>"

    private fun getActualContent(message: String, link: String, fileName: String) =
        "$message\n[$fileName]($link)"

    private fun addReaction(emoji: EmojiItem, messageId: Int): Flow<ChatEvent> = flow {
        runCatchingNonCancellation {
            messageRepository.addReaction(emoji.toDomain(), messageId)
        }.onFailure {
            emit(ChatEvent.Internal.Error("Failed to add reaction"))
        }
    }

    private fun onReactionClick(emojiItem: EmojiItem, messageId: Int): Flow<ChatEvent> = flow {
        runCatchingNonCancellation {
            reactionClickUseCase(emojiItem.toDomain(), messageId)
        }.onFailure {
            emit(ChatEvent.Internal.Error("Oops... something went wrong"))
        }
    }

    private fun onMessageLinkClick(url: String): Flow<ChatEvent> {
        if (MimeTypes.isImage(url.mimeType()))
            router.navigateTo(Screens.ImageViewer(url))
        else
            fileRepository.downloadFile(url)
        return emptyFlow()
    }

    private fun String.mimeType(): String? =
        MimeTypeMap.getFileExtensionFromUrl(this)?.let { extension ->
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }

    private fun loadMessages(): Flow<ChatEvent> =
        messageRepository.getTopicMessages(topicId.toDomain(), INIT_PAGE_SIZE).map {
            ChatEvent.Internal.MessagesLoaded(it.toDelegateList())
        }.catchNonCancellationAndEmit(
            ChatEvent.Internal.Error("Failed to load messages")
        )

    private fun observeConnection(): Flow<ChatEvent> = connectivityObserver.observe().map {
        when (it) {
            ConnectivityObserver.Status.Available -> ChatEvent.Internal.ConnectionStatusChanged(true)
            ConnectivityObserver.Status.Losing -> ChatEvent.Internal.ConnectionStatusChanged(true)
            ConnectivityObserver.Status.Lost -> ChatEvent.Internal.ConnectionStatusChanged(false)
            ConnectivityObserver.Status.Unavailable -> ChatEvent.Internal.ConnectionStatusChanged(false)
        }
    }.distinctUntilChanged()

    private fun loadMore(): Flow<ChatEvent> =
        messageRepository.requestMore(topicId.toDomain(), PAGE_SIZE).map {
            ChatEvent.Internal.MessagesLoaded(it.toDelegateList())
        }.catchNonCancellationAndEmit(
            ChatEvent.Internal.Error("Failed to load more")
        )

    private fun prepareReactionDialog(messageId: Int): Flow<ChatEvent> = flowOf (
        ChatEvent.Internal.ReactionDialogReady(
            ReactionDialogData(
                emojiSet = messageRepository.getEmojiSet().map { it.toUI() },
                messageId = messageId
            )
        )
    )

    private fun prepareTopicPickerDialog(messageId: Int): Flow<ChatEvent> = flowOf(
        ChatEvent.Internal.TopicPickerDialogReady(
            TopicPickerDialogData(
                messageId = messageId,
                currentTopicId = topicId
            )
        )
    )

    private fun copyToClipboard(text: String, clipboardManager: ClipboardManager): Flow<ChatEvent> {
        val clip = ClipData.newPlainText("", Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).trim())
        clipboardManager.setPrimaryClip(clip)
        return emptyFlow()
    }

    private fun deleteMessage(messageId: Int): Flow<ChatEvent> = flow {
        runCatchingNonCancellation {
            messageRepository.deleteMessage(messageId)
        }.onFailure {
            emit(ChatEvent.Internal.Error("Can't delete message"))
        }
    }

    private fun editMessage(messageId: Int, newContent: String): Flow<ChatEvent> = flow {
        runCatchingNonCancellation {
            messageRepository.editMessage(messageId, newContent)
        }.onFailure {
            emit(ChatEvent.Internal.Error("Can't edit message"))
        }
    }

    private fun changeMessageTopic(messageId: Int, newTopicId: TopicId): Flow<ChatEvent> = flow {
        runCatchingNonCancellation {
            messageRepository.changeMessageTopic(messageId, newTopicId.topicName)
        }.onFailure {
            emit(ChatEvent.Internal.Error("Can't change topic"))
        }
    }

    private fun onBackPressed(): Flow<ChatEvent> {
        router.exit()
        return emptyFlow()
    }

    private fun List<Message>.toDelegateList(): List<DelegateItem> {
        val userId = authHelper.userId
        return this.groupBy { it.dateTime.toDateUI() }
            .map { groupedMessages ->
                listOf(DateDelegateItem(value = groupedMessages.key)) + groupedMessages.value.map {
                    if (it.senderId == userId)
                        it.toOwnItem(userId).toOwnMessageDelegateItem()
                    else
                        it.toUI(userId).toMessageDelegateItem()
                }
            }
            .flatten()
    }

    private fun LocalDateTime.toDateUI() = DateItem(
        date = format(DateTimeFormatter.ofPattern("dd LLLL"))
    )

    private fun OtherMessageItem.toMessageDelegateItem() = OtherMessageDelegateItem(
        id = id,
        value = this
    )

    private fun OwnMessageItem.toOwnMessageDelegateItem() = OwnMessageDelegateItem(
        id = id,
        value = this
    )

    private fun getSendingMessage(message: String) = OwnMessageItem(
        id = Int.MAX_VALUE,
        text = message,
        date = LocalDate.now().toString(),
        time = LocalTime.now().toString(),
        isSending = true,
        reactions = emptyList()
    ).toOwnMessageDelegateItem()

    companion object {
        private const val ATTACHMENT_RESULT_KEY = "ATTACHMENT_RESULT_KEY"
        private const val VISIBLE_THRESHOLD = 5
        private const val PAGE_SIZE = 20
        private const val INIT_PAGE_SIZE = 50
        fun ChatEvent.UI.Scroll.shouldFetchMore() = firstVisibleItemPosition - VISIBLE_THRESHOLD <= 0
    }
}