package com.example.myshop.customview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.example.myshop.R

class NormalTextView(context: Context, attr: AttributeSet) : AppCompatTextView(context, attr) {

    var typeText : String = ""

    init {
        setAttribute(attr)
    }

    private fun setAttribute(attr: AttributeSet){
        val attr = context.obtainStyledAttributes(attr, R.styleable.NormalTextView)
        typeText = attr.getString(R.styleable.NormalTextView_typeText).toString()
        if(typeText == bold){
            setBoldText()
        }else{
            setNormalText()
        }
    }

    private fun setBoldText(){
        typeText = bold
        val fontStyle = Typeface.createFromAsset(context.assets,"montserrat_bold.ttf")
        typeface = fontStyle
    }

    private fun setNormalText(){
        typeText = normal
        val fontStyle = Typeface.createFromAsset(context.assets,"montserrat_regular.ttf")
        typeface = fontStyle
    }

    companion object{
        const val bold = "bold"
        const val normal = "normal"
    }
}