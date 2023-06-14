package com.example.courcework.screen

import com.example.courcework.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.text.KTextView

class MessageActionDialog : KScreen<MessageActionDialog>() {
    override val layoutId: Int = R.layout.message_action_bottom_sheet_dialog
    override val viewClass: Class<*> = BottomSheetDialog::class.java

    val addReaction = KTextView { withId(R.id.add_reaction) }
}