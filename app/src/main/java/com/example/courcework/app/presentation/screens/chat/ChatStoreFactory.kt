package com.example.courcework.app.presentation.screens.chat

import androidx.media3.common.util.UnstableApi
import com.example.courcework.app.presentation.connectivity.ConnectivityObserver
import com.example.courcework.app.presentation.screens.channels.stream.model.TopicId
import com.example.courcework.data.network.auth.AuthHelper
import com.example.courcework.domain.repository.FileRepository
import com.example.courcework.domain.repository.MessageRepository
import com.example.courcework.domain.usecase.ReactionClickUseCase
import com.github.terrakok.cicerone.Router
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import vivid.money.elmslie.coroutines.ElmStoreCompat

@UnstableApi class ChatStoreFactory @AssistedInject constructor(
    @Assisted("topicId") private val topicId: TopicId,
    private val messageRepository: MessageRepository,
    private val fileRepository: FileRepository,
    private val authHelper: AuthHelper,
    private val router: Router,
    private val connectivityObserver: ConnectivityObserver,
    private val reactionClickUseCase: ReactionClickUseCase,
) {

    fun create() = ElmStoreCompat(
        initialState = ChatState(),
        reducer = ChatReducer(),
        actor = ChatActor(
            topicId = topicId,
            messageRepository = messageRepository,
            authHelper = authHelper,
            router = router,
            fileRepository = fileRepository,
            connectivityObserver = connectivityObserver,
            reactionClickUseCase = reactionClickUseCase,
        )
    )

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("topicId") topicId: TopicId,
        ) : ChatStoreFactory
    }
}