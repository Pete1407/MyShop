package com.example.myshop.customview

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.myshop.R

class CustomEditText(context: Context, attr : AttributeSet? = null):AppCompatEditText(context,attr) {

    init {
        setFontStyle()
    }

    private fun setFontStyle(){
        val fontStyle = Typeface.createFromAsset(context.assets,"montserrat_regular.ttf")
        typeface = fontStyle
    }
}