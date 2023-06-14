package com.example.courcework.domain.usecase

import com.example.courcework.data.network.auth.AuthHelper
import com.example.courcework.domain.model.EmojiNCS
import com.example.courcework.domain.repository.MessageRepository

class ReactionClickUseCase(
    private val messageRepository: MessageRepository,
    private val authHelper: AuthHelper,
) {
    suspend operator fun invoke(emoji: EmojiNCS, messageId: Int) {
        val userId = authHelper.userId
        val reacted = messageRepository.getMessage(messageId).reactions.any { it.emoji == emoji && it.userId == userId }
        if (reacted) messageRepository.removeReaction(emoji, messageId)
        else messageRepository.addReaction(emoji, messageId)
    }
}