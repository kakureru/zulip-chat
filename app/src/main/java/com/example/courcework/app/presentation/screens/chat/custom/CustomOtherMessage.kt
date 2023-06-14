package com.example.courcework.app.presentation.screens.chat.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.*
import com.example.courcework.R
import com.example.courcework.app.presentation.screens.chat.model.ReactionItem

class CustomOtherMessage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    var onClick: (reaction: ReactionItem) -> Unit = { }
    var onLongClick: () -> Unit = { }
    var onAddReactionClick: () -> Unit = { }

    val name: TextView
    val message: TextView
    val avatar: ImageView
    private val reactions: ReactionFlexboxLayout
    private val container: View

    private val plusView = ReactionView(context, defStyleRes = R.style.EmojiView).apply {
        emoji = context.getString(R.string.emoji_plus)
        visibility = View.GONE
        layoutParams = generateDefaultLayoutParams()
        setOnClickListener { onAddReactionClick() }
    }

    init {
        inflate(context, R.layout.custom_message, this)
        avatar = findViewById(R.id.avatar)
        name = findViewById(R.id.name)
        message = findViewById(R.id.message)
        reactions = findViewById(R.id.reactions)
        container = findViewById(R.id.bg_container)
        reactions.addView(plusView)
        isLongClickable = true
        name.setOnLongClickListener { onLongClick(); true }
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
            12f.dp(context).toInt(),
            2f.dp(context).toInt(),
            40f.dp(context).toInt(),
            2f.dp(context).toInt()
        )

        // image
        measureChildWithMargins(
            avatar,
            widthMeasureSpec,
            paddingLeft + paddingRight,
            heightMeasureSpec,
            paddingTop + paddingBottom
        )

        // name
        measureChildWithMargins(
            name,
            heightMeasureSpec,
            avatar.fullWidth + paddingLeft + paddingRight,
            heightMeasureSpec,
            paddingTop + paddingBottom
        )

        // text
        measureChildWithMargins(
            message,
            widthMeasureSpec,
            avatar.fullWidth + paddingLeft + paddingRight,
            heightMeasureSpec,
            name.fullHeight + paddingTop + paddingBottom
        )

        // reaction
        measureChildWithMargins(
            reactions,
            widthMeasureSpec,
            avatar.fullWidth + paddingLeft + paddingRight,
            heightMeasureSpec,
            name.fullHeight + message.fullHeight + paddingTop + paddingBottom
        )

        // container
        val containerHeight = container.paddingTop + container.paddingBottom + name.fullHeight + message.fullHeight

        val totalWidth = MeasureSpec.getSize(widthMeasureSpec)
        val totalHeight = paddingTop + paddingBottom + maxOf(avatar.fullHeight, containerHeight + reactions.fullHeight)
        setMeasuredDimension(totalWidth, totalHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var offsetX = paddingLeft
        var offsetY = paddingTop

        // image
        avatar.layout(offsetX, offsetY)
        offsetX += avatar.fullWidth + container.paddingLeft
        offsetY += container.paddingTop

        // name
        name.layout(offsetX, offsetY)
        offsetY += name.fullHeight

        // text
        message.layout(offsetX, offsetY)
        offsetY += message.fullHeight + container.paddingBottom

        // container
        container.layout(
            offsetX - container.paddingLeft,
            paddingTop,
            offsetX + maxOf(name.fullWidth, message.fullWidth) + container.paddingRight,
            offsetY
        )
        offsetX -= container.paddingLeft

        // reactions
        reactions.layout(offsetX, offsetY)
    }

    override fun generateDefaultLayoutParams() =
        MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(p: LayoutParams) = MarginLayoutParams(p)

    override fun generateLayoutParams(attrs: AttributeSet?) = MarginLayoutParams(context, attrs)

    override fun checkLayoutParams(p: LayoutParams) = p is MarginLayoutParams
}