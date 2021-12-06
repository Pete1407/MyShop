package com.example.myshop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cart(
    val user_id : String = "",
    val product_id : String = "",
    val title : String = "",
    val price : String = "",
    val image : String = "",
    val cart_quantity : String = "",
    val stock_quantity : String = "",
    val id : String = ""
):Parcelable
