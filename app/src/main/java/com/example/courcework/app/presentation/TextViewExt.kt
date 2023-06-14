package com.example.courcework.app.presentation

import android.text.Html
import android.text.Html.ImageGetter
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView

fun TextView.setClickableText(htmlText: String, imageGetter: ImageGetter, onLinkClick: (url: String) -> Unit) {
    movementMethod = LinkMovementMethod.getInstance()
    val sequence = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY, imageGetter, null).trim()
    text = SpannableStringBuilder(sequence).apply {
        getSpans(0, sequence.length, URLSpan::class.java).forEach { span ->
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) { onLinkClick(span.url) }
            }
            setSpan(clickableSpan, getSpanStart(span), getSpanEnd(span), getSpanFlags(span))
            removeSpan(span)
        }
    }
}