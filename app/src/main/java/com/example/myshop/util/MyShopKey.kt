package com.example.myshop.util

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

object MyShopKey {

    const val USERS = "users"
    const val MYSHOPPREF = "myShopPref"
    const val USERNAME_LOGIN = "username"
    const val MALE = "male"
    const val FEMALE = "female"

    const val FIRSTNAME_FIELD = "firstname"
    const val LASTNAME_FIELD = "lastname"
    const val MOBILE_FIELD = "mobile"
    const val GENDER_FIELD = "gender"
    const val IMAGE_FIELD = "image"
    const val PROFILE_COMPLETE_FIELD = "profileCompleted"

    const val PICK_IMAGE_REQUEST_CODE = 20
    const val ACTION_EDIT_PROFILE = "action-edit-profile"
    const val ACTION_ADD_INFO = "action-add-info"

    const val DELAY_EDIT_DATA = 3000L

    fun showGallery(activity: Activity){
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }
}