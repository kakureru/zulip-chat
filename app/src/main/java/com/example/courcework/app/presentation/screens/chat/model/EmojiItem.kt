package com.example.courcework.app.presentation.screens.chat.model

import android.os.Parcelable
import com.example.courcework.domain.model.EmojiNCS
import kotlinx.parcelize.Parcelize
import java.lang.NumberFormatException

@Parcelize
data class EmojiItem(
    val name: String,
    val code: String,
) : Parcelable {
    fun getCodeString(): String {
        return try {
            String(Character.toChars(code.split('-')[0].toInt(16)))
        } catch (e: NumberFormatException) {
            "?"
        }
    }
}

fun EmojiItem.toDomain() = EmojiNCS(
    name = name,
    code = code,
)

fun EmojiNCS.toUI() = EmojiItem(
    name = name,
    code = code,
)