package com.example.courcework.screen

import android.view.View
import com.example.courcework.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import org.hamcrest.Matcher

class ReactionDialog : KScreen<ReactionDialog>() {
    override val layoutId: Int = R.layout.reaction_bottom_sheet_dialog
    override val viewClass: Class<*> = BottomSheetDialog::class.java

    val listReactions = KRecyclerView({ withId(R.id.list_reactions) }, { itemType { ReactionItem(it) } })

    class ReactionItem(parent: Matcher<View>) : KRecyclerItem<ReactionItem>(parent)
}