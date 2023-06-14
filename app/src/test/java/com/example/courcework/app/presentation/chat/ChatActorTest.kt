package com.example.courcework.app.presentation.chat

import com.example.courcework.app.presentation.connectivity.ConnectivityObserver
import com.example.courcework.app.presentation.downloader.Downloader
import com.example.courcework.app.presentation.screens.channels.stream.model.TopicId
import com.example.courcework.app.presentation.screens.chat.ChatActor
import com.example.courcework.app.presentation.screens.chat.ChatCommand
import com.example.courcework.app.presentation.screens.chat.model.EmojiItem
import com.example.courcework.app.presentation.screens.chat.model.ReactionItem
import com.example.courcework.app.presentation.screens.chat.model.toDomain
import com.example.courcework.app.presentation.screens.chat.model.toUI
import com.example.courcework.data.emojiSet
import com.example.courcework.data.network.auth.AuthHelper
import com.example.courcework.domain.repository.MessageRepository
import com.example.courcework.stub.AuthHelperStub
import com.example.courcework.stub.ConnectivityObserverStub
import com.example.courcework.stub.DownloaderStub
import com.example.courcework.stub.MessageRepositoryStub
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.times

@OptIn(ExperimentalCoroutinesApi::class)
class ChatActorTest {

    @Test
    fun `add own reaction`() = runTest {
        val reaction = emojiSet[0]
        val messageId = 0
        val messageRepository = mock(MessageRepositoryStub::class.java)
        val router = mock(Router::class.java)
        val actor = createActor(messageRepository = messageRepository, router = router)

        actor.execute(ChatCommand.AddReaction(reaction.toUI(), messageId)).collect()

        verify(messageRepository, times(1)).addReaction(reaction, messageId)
    }

    @Test
    fun `click on reaction, add own reaction`() = runTest {
        val reaction = createReaction(emojiSet[0].toUI(), 2, false)
        val messageId = 0
        val messageRepository = mock(MessageRepositoryStub::class.java)
        val router = mock(Router::class.java)
        val actor = createActor(messageRepository = messageRepository, router = router)

        actor.execute(ChatCommand.OnReactionClick(reaction, messageId)).collect()

        verify(messageRepository, times(1)).addReaction(reaction.emoji.toDomain(), messageId)
    }
    @Test
    fun `click on reaction, delete own reaction`() = runTest {
        val reaction = createReaction(emojiSet[0].toUI(), 2, true)
        val messageId = 0
        val messageRepository = mock(MessageRepositoryStub::class.java)
        val router = mock(Router::class.java)
        val actor = createActor(messageRepository = messageRepository, router = router)

        actor.execute(ChatCommand.OnReactionClick(reaction, messageId)).collect()

        verify(messageRepository, times(1)).removeReaction(reaction.emoji.toDomain(), messageId)
    }
    private fun createActor(
        topicId: TopicId = TopicId(0, "Topic"),
        messageRepository: MessageRepository = MessageRepositoryStub(),
        authHelper: AuthHelper = AuthHelperStub(),
        router: Router,
        downloader: Downloader = DownloaderStub(),
        connectivityObserver: ConnectivityObserver = ConnectivityObserverStub(),
    ): ChatActor = ChatActor(
        topicId = topicId,
        messageRepository = messageRepository,
        authHelper = authHelper,
        router = router,
        downloader = downloader,
        connectivityObserver = connectivityObserver,
    )

    private fun createReaction(
        emojiItem: EmojiItem,
        count: Int,
        selected: Boolean,
    ): ReactionItem = ReactionItem(emojiItem, count, selected)
}