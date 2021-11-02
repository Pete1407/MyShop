package com.example.myshop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id : String = "",
    val firstname : String = "",
    val lastname : String = "",
    val email : String = "",
    val image : String = "",
    val mobile : Long = 0,
    val gender : String = "",
    val profileCompleted : Int = 0
):Parcelable
