package com.example.myshop.util

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

object MyShopKey {

    const val USERS = "users"
    const val MYSHOPPREF = "myShopPref"
    const val USERNAME_LOGIN = "username"
    const val GENDER_FIELD = "gender"
    const val MOBILE_FIELD = "mobile"
    const val MALE = "male"
    const val FEMALE = "female"
    const val PICK_IMAGE_REQUEST_CODE = 20

    fun showGallery(activity: Activity){
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }
}