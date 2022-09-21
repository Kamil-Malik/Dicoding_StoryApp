package com.lelestacia.dicodingstoryapp.ui.design

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.lelestacia.dicodingstoryapp.R

class PasswordEdt : AppCompatEditText {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = resources.getString(R.string.please_input_your_password)
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        when {
            (text?.length ?: 0 ) > 0 && (text?.length ?: 0) < 6 -> error = resources.getString(R.string.empty_password)
        }
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }
}