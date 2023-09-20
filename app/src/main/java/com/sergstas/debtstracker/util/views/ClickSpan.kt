package com.sergstas.debtstracker.util.views

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.sergstas.debtstracker.R
import com.sergstas.debtstracker.util.extensions.colorFromId

class ClickSpan(
    private val context: Context,
    private val action: () -> Unit,
): ClickableSpan() {
    companion object {
        fun addActionToText(
            context: Context,
            view: TextView,
            clickableText: String,
            action: () -> Unit,
        ) {
            val text = view.text
            val string = text.toString()
            val span = ClickSpan(context, action)
            val start = string.indexOf(clickableText)
            val end = start + clickableText.length
            if (start == -1) {
                return
            }
            if (text is Spannable) {
                text.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            } else {
                val s = SpannableString.valueOf(text)
                s.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                view.text = s
            }
            val m = view.movementMethod
            if (m !is LinkMovementMethod) {
                view.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }

    override fun onClick(widget: View) =
        action()

    override fun updateDrawState(paint: TextPaint) {
        super.updateDrawState(paint)
        paint.isUnderlineText = true
        paint.color = context.colorFromId(R.color.c1)
    }
}