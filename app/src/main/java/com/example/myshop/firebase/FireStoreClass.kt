package com.example.myshop.firebase

import android.app.Activity
import android.util.Log
import com.example.myshop.activities.LoginActivity
import com.example.myshop.activities.RegisterActivity
import com.example.myshop.model.User
import com.example.myshop.util.MyShopKey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase

class FireStoreClass {

    private val fireStore =  FirebaseFirestore.getInstance()

    fun registerUser(activity : RegisterActivity,user : User){
        // กำหนด collection
        fireStore.collection(MyShopKey.USERS)
                // กำหนด unique id
            .document(user.id)
                // set คือการเพิ่มลง database ถ้าเป็น get จะใช้ get เลย
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                activity.registerNewUserSuccessfully()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
            }

    }

    private fun getUserID():String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser!=null){
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

    fun getCurrentUserByID(activity : Activity){
        var output : User? = null
        fireStore.collection(MyShopKey.USERS)
            .document(getUserID())
            .get()
            .addOnSuccessListener { result ->
                output = result.toObject(User::class.java)
                when(activity){
                    is LoginActivity -> {
                        activity.getCurrentUser(output!!)
                    }
                }
            }
    }
}