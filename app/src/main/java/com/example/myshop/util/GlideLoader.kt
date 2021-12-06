package com.example.myshop.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myshop.R

class GlideLoader(val context: Context) {

    fun loadImage(url : String?,imageView : ImageView){
        Glide.with(context)
            .load(url)
            .into(imageView)
    }
}