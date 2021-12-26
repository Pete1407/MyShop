package com.example.myshop.util

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

object MyShopKey {

    // collections
    const val USERS = "users"
    const val PRODUCTS = "products"
    const val CARTS = "carts"
    const val ADDRESSES = "addresses"
    const val ORDER = "orders"

    const val MYSHOPPREF = "myShopPref"
    const val USERNAME_LOGIN = "username"
    const val MALE = "male"
    const val FEMALE = "female"

    // field in users collection
    const val FIRSTNAME_FIELD = "firstname"
    const val LASTNAME_FIELD = "lastname"
    const val MOBILE_FIELD = "mobile"
    const val GENDER_FIELD = "gender"
    const val IMAGE_FIELD = "image"
    const val PROFILE_COMPLETE_FIELD = "profileCompleted"

    // field in products collection
    const val NAME_PRODUCT = "name"
    const val PRICE_PRODUCT = "price"
    const val DESCRIPTION_PRODUCT = "description"
    const val QUANTITY_PRODUCT = "quantity"
    const val IMAGE_PRODUCT = "image"
    const val USER_ID = "user_id"
    const val PRODUCT_ID = "product_id"
    const val PICK_IMAGE_REQUEST_CODE = 20
    const val ACTION_EDIT_PROFILE = "action-edit-profile"
    const val ACTION_ADD_INFO = "action-add-info"

    const val DELAY_EDIT_DATA = 3000L

    const val STOCK_QUANTITY = "stock_quantity"

    // field of address
    const val ADDRESS_NAME = "name"
    const val MOBILE_NUMBER = "mobileNumber"
    const val ADDRESS = "address"
    const val ZIPCODE = "zipCode"
    const val NOTE = "additionalNote"
    const val TYPE = "type"
    const val OTHER_DETAIL = "otherDetails"


    fun showGallery(activity: Activity){
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }
}