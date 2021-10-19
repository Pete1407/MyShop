package com.example.myshop.customview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class BoldTextView(context: Context, attr: AttributeSet? = null) :
    AppCompatTextView(context, attr) {

    init {
        setFontStyle()
    }

    private fun setFontStyle() {
        val fontStyle = Typeface.createFromAsset(context.assets, "montserrat_bold.ttf")
        typeface = fontStyle
    }
}