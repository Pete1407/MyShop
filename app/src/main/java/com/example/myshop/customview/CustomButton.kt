package com.example.myshop.customview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.example.myshop.R

class CustomButton(context: Context, attr: AttributeSet) : AppCompatButton(context, attr) {

    init {
        setFontStyle()
        setBackgroundButton()
    }

    private fun setFontStyle(){
        val fontStyle = Typeface.createFromAsset(context.assets,"montserrat_bold.ttf")
        typeface = fontStyle
    }

    private fun setBackgroundButton(){
        setBackgroundResource(R.drawable.bg_button)
    }
}