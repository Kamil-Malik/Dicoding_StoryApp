package com.lelestacia.dicodingstoryapp.ui.design

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.marginTop
import com.lelestacia.dicodingstoryapp.R

class EmailEdt : AppCompatEditText {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = resources.getString(R.string.please_input_your_email)
        textAlignment = TEXT_ALIGNMENT_VIEW_START
        marginTop
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        when {
            (!Patterns.EMAIL_ADDRESS.matcher((text ?: "")).matches() && (text ?: "").isNotEmpty()) ->
                error = resources.getString(R.string.invalid_email)
        }
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }
}