package com.example.courcework.app.presentation.screens.chat.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.example.courcework.R
import com.example.courcework.app.presentation.screens.chat.model.ReactionItem

class CustomOwnMessage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    var onClick: (reaction: ReactionItem) -> Unit = { }
    var onLongClick: () -> Unit = { }
    var onAddReactionClick: () -> Unit = { }

    val message: TextView
    val sendStatus: View
    private val reactions: ReactionFlexboxLayout
    private val container: View

    private val plusView = ReactionView(context, defStyleRes = R.style.EmojiView).apply {
        emoji = context.getString(R.string.emoji_plus)
        visibility = View.GONE
        layoutParams = generateDefaultLayoutParams()
        setOnClickListener { onAddReactionClick() }
    }

    init {
        inflate(context, R.layout.custom_own_message, this)
        message = findViewById(R.id.message)
        sendStatus = findViewById(R.id.send_status)
        reactions = findViewById(R.id.reactions)
        container = findViewById(R.id.bg_container)
        reactions.addView(plusView)
        isLongClickable = true
        message.setOnLongClickListener { onLongClick(); true }
    }

    override fun performLongClick(): Boolean {
        if (super.performLongClick()) return true
        onLongClick()
        return true
    }

    fun setReactions(reactions: List<ReactionItem>) {
        this.reactions.apply {
            removeAllViews()
            addView(plusView)
            reactions.forEach { addView(getReactionView(it),  childCount - 1) }
            plusView.isVisible = childCount > 1
        }
    }

    private fun getReactionView(reaction: ReactionItem) =
        ReactionView(context, defStyleRes = R.style.EmojiView).apply {
            emoji = reaction.emoji.getCodeString()
            count = reaction.count
            isSelected = reaction.selected
            setOnClickListener { onClick(reaction) }
        }

    /**
     * Measure and layout
     */

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setPadding(
            40f.dp(context).toInt(),
            2f.dp(context).toInt(),
            12f.dp(context).toInt(),
            2f.dp(context).toInt()
        )

        // status
        measureChildWithMargins(
            sendStatus,
            widthMeasureSpec,
            paddingLeft + paddingRight,
            heightMeasureSpec,
            paddingTop + paddingBottom
        )

        // text
        measureChildWithMargins(
            message,
            widthMeasureSpec,
            sendStatus.fullWidth + paddingLeft + paddingRight,
            heightMeasureSpec,
            paddingTop + paddingBottom
        )

        // reaction
        measureChildWithMargins(
            reactions,
            widthMeasureSpec,
            sendStatus.fullWidth + paddingLeft + paddingRight,
            heightMeasureSpec,
            message.fullHeight + paddingTop + paddingBottom
        )

        // container
        val containerHeight = container.paddingTop + container.paddingBottom + message.fullHeight

        val totalWidth = MeasureSpec.getSize(widthMeasureSpec)
        val totalHeight = paddingTop + paddingBottom + containerHeight + reactions.fullHeight
        setMeasuredDimension(totalWidth, totalHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var offsetY = paddingTop

        // container
        val containerWidth = message.fullWidth + container.paddingLeft + container.paddingRight
        container.layout(
            fullWidth - (paddingRight + containerWidth),
            offsetY,
            fullWidth - paddingRight,
            offsetY + message.fullHeight + container.paddingBottom + container.paddingTop
        )
        offsetY += container.paddingTop

        // status
        sendStatus.layout(
            fullWidth - (paddingRight + containerWidth + sendStatus.paddingRight + sendStatus.fullWidth),
            offsetY,
            fullWidth - (paddingRight + containerWidth + sendStatus.paddingRight),
            offsetY + sendStatus.fullHeight
        )

        // text
        message.layout(
            fullWidth - (paddingRight + container.paddingRight + message.fullWidth),
            offsetY,
            fullWidth - (paddingRight + container.paddingRight),
            offsetY + message.fullHeight
        )
        offsetY += message.fullHeight + container.paddingBottom

        // reactions
        reactions.layout(
            fullWidth - (paddingRight + reactions.fullWidth),
            offsetY + reactions.marginTop,
            fullWidth - (paddingRight + reactions.marginRight),
            offsetY + reactions.fullHeight
        )
    }

    override fun generateDefaultLayoutParams() =
        MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(p: LayoutParams) = MarginLayoutParams(p)

    override fun generateLayoutParams(attrs: AttributeSet?) = MarginLayoutParams(context, attrs)

    override fun checkLayoutParams(p: LayoutParams) = p is MarginLayoutParams
}