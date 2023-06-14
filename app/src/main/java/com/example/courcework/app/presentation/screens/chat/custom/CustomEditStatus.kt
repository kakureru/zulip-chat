package com.example.courcework.app.presentation.screens.chat.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import com.example.courcework.R

class CustomEditStatus @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    var onCancelClick: () -> Unit = { }

    private val editIcon: View
    private val editTitle: TextView
    private val editMessage: TextView
    private val btnCancel: ImageButton
    private val container: View

    init {
        inflate(context, R.layout.custom_edit_status, this)
        editIcon = findViewById(R.id.edit_icon)
        editTitle = findViewById(R.id.edit_title)
        editMessage = findViewById(R.id.edit_message)
        btnCancel = findViewById(R.id.btn_cancel)
        container = findViewById(R.id.bg_container)
        btnCancel.setOnClickListener { onCancelClick() }
    }

    fun setEditMessage(text: String) {
        editMessage.text = text
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        measureChildWithMargins(
            editIcon,
            widthMeasureSpec,
            paddingLeft + paddingRight + container.paddingLeft + container.paddingRight,
            heightMeasureSpec,
            paddingTop + paddingBottom + container.paddingTop + container.paddingBottom
        )

        measureChildWithMargins(
            btnCancel,
            widthMeasureSpec,
            editIcon.fullWidth + paddingLeft + paddingRight + container.paddingLeft + container.paddingRight,
            heightMeasureSpec,
            paddingTop + paddingBottom + container.paddingTop + container.paddingBottom
        )

        measureChildWithMargins(
            editTitle,
            heightMeasureSpec,
            editIcon.fullWidth + btnCancel.fullWidth + paddingLeft + paddingRight + container.paddingLeft + container.paddingRight,
            heightMeasureSpec,
            paddingTop + paddingBottom + container.paddingTop + container.paddingBottom
        )

        measureChildWithMargins(
            editMessage,
            widthMeasureSpec,
            editIcon.fullWidth + btnCancel.fullWidth + paddingLeft + paddingRight + container.paddingLeft + container.paddingRight,
            heightMeasureSpec,
            editTitle.fullHeight + paddingTop + paddingBottom + container.paddingTop + container.paddingBottom
        )

        val totalWidth = MeasureSpec.getSize(widthMeasureSpec)
        val totalHeight = paddingTop + paddingBottom + editTitle.fullHeight + editMessage.fullHeight + container.paddingTop + container.paddingBottom
        setMeasuredDimension(totalWidth, totalHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var offsetX = paddingLeft + container.paddingLeft
        var offsetY = paddingTop + container.paddingTop

        editIcon.layout(
            offsetX + editIcon.marginLeft,
            offsetY + editIcon.marginTop,
            offsetX + editIcon.measuredWidth + editIcon.marginLeft,
            measuredHeight - (paddingBottom + container.paddingBottom - editIcon.marginTop)
        )
        offsetX += editIcon.fullWidth

        btnCancel.layout(
            measuredWidth - (btnCancel.fullWidth + paddingRight + container.paddingRight),
            offsetY,
            measuredWidth - (paddingRight + container.paddingRight),
            measuredHeight - (paddingBottom + container.paddingBottom)
        )

        editTitle.layout(offsetX, offsetY)
        offsetY += editTitle.fullHeight

        editMessage.layout(offsetX, offsetY)

        container.layout(0, 0, measuredWidth, measuredHeight)
    }

    override fun generateDefaultLayoutParams() =
        MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(p: LayoutParams) = MarginLayoutParams(p)

    override fun generateLayoutParams(attrs: AttributeSet?) = MarginLayoutParams(context, attrs)

    override fun checkLayoutParams(p: LayoutParams) = p is MarginLayoutParams
}