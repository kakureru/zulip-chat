package com.example.courcework.app.presentation.screens.chat.custom

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.floor

fun Float.sp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    this,
    context.resources.displayMetrics
)

fun Float.dp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this,
    context.resources.displayMetrics
)

fun View.layout(startX: Int, startY: Int) = layout(
    startX + marginLeft,
    startY + marginTop,
    startX + measuredWidth + marginLeft,
    startY + measuredHeight + marginTop
)

val View.fullWidth get() = marginLeft + measuredWidth + marginRight
val View.fullHeight get() = marginTop + measuredHeight + marginBottom

fun RecyclerView.setSpanCountByColumnWidth(columnWidth: Float) {
    val widthInDp = with(this.context.resources.displayMetrics) { widthPixels / density }
    (this.layoutManager as GridLayoutManager).spanCount = floor(widthInDp / columnWidth).toInt()
}