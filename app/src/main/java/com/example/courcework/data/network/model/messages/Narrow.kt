package com.example.courcework.data.network.model.messages

import com.example.courcework.domain.model.TopicId
import com.google.gson.Gson

data class Narrow(
    val operand: Any,
    val operator: String
) {
    companion object {

        fun constructForTopic(topicId: TopicId): String =
            Gson().toJson(
                listOf(
                    Narrow(operand = topicId.streamId, operator = STREAM_OPERATOR),
                    Narrow(operand = topicId.topicName, operator = TOPIC_OPERATOR)
                )
            )

        private const val STREAM_OPERATOR = "stream"
        private const val TOPIC_OPERATOR = "topic"
    }
}