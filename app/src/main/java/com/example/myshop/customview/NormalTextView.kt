package com.example.myshop.customview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.example.myshop.R
import android.text.style.UnderlineSpan

import android.text.SpannableString




class NormalTextView(context: Context, attr: AttributeSet) : AppCompatTextView(context, attr) {

    var typeText : String = ""
    var addUnderLine : Boolean = false
    init {
        setAttribute(attr)
    }

    private fun setAttribute(attr: AttributeSet){
        val attr = context.obtainStyledAttributes(attr, R.styleable.NormalTextView)
        typeText = attr.getString(R.styleable.NormalTextView_typeText).toString()
        addUnderLine = attr.getBoolean(R.styleable.NormalTextView_hasUnderline,false)
        setStrike(addUnderLine)

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

    private fun setStrike(hasLine : Boolean){
        if(hasLine){
            val content = SpannableString(this.text)
            content.setSpan(UnderlineSpan(), 0, this.text.length, 0)
            this.text = content
        }
    }

    companion object{
        const val bold = "bold"
        const val normal = "normal"
    }
}