package com.example.myshop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderModel(
    val user_id :String = "",
    val items : ArrayList<Cart> = arrayListOf(),
    val addressModel: AddressModel = AddressModel(),
    val title : String = "",
    val image : String,
    val sub_total_amount : String = "",
    val shipping_charge : String = "",
    val total_amount : String = "",
    var id : String = ""
):Parcelable
