@file:Suppress("DEPRECATION")

package com.example.myshop.util

import android.app.Activity
import android.util.DisplayMetrics

object ScreenUtility {

    var displayMetrics: DisplayMetrics? = null
    fun getDisplay(activity: Activity): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
//        if (!UserManager.SystemUINotch){
//            displayMetrics.heightPixels -= UserManager.SystemUITop
//        }
        return displayMetrics
    }

    fun setDisplay(displayMetrics: DisplayMetrics){
        this.displayMetrics = displayMetrics
    }

    fun getDisplay():DisplayMetrics{
        return displayMetrics!!
    }
}