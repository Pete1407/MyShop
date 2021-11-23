package com.example.myshop.firebase

import android.app.Activity
import android.util.Log
import com.example.myshop.activities.activity.*
import com.example.myshop.model.Product
import com.example.myshop.model.User
import com.example.myshop.util.MyShopKey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {

    private val fireStore =  FirebaseFirestore.getInstance()

    // register user into Firebase authentication
    fun registerUser(activity : RegisterActivity, user : User){
        // กำหนด collection
        fireStore.collection(MyShopKey.USERS)
                // กำหนด unique id
            .document(user.id)
                // set คือการเพิ่มลง database ถ้าเป็น get จะใช้ get เลย
                // SetOptions.merge ถ้ามี item นี้อยู่แล้วจะเข้าไปแทนที่ค่า ตาม id ถ้าไม่มีก็เพิ่มเข้าไป
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                activity.registerNewUserSuccessfully()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
            }

    }

    fun getUserID():String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser!=null){
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

    // get data from userID
    fun getCurrentUserByID(activity : Activity) {
        var output: User? = null
        fireStore.collection(MyShopKey.USERS)
            .document(getUserID())
            // สำหรับการดึงข้อมูลตาม id ของ user
            .get()
            .addOnSuccessListener { result ->
                output = result.toObject(User::class.java)
                output?.let {
                    when (activity) {
                        is LoginActivity -> {
                            activity.getCurrentUser(output!!)
                        }
                        is SettingActivity -> {
                            activity.apply {
                               hideProgressDialog()
                                getDataUser(it)
                            }
                        }
                        else ->{}
                    }
                } ?: kotlin.run {
                    Log.d("tag_fireStore", "<--  output is null  -->")
                }
            }
            .addOnFailureListener {
                when(activity){
                    is LoginActivity -> {
                        activity.showProgressDialog()
                    }
                    is SettingActivity -> {
                        activity.showProgressDialog()
                    }
                }
            }
    }

    // update data for some field
    // by use hashmap
    fun updateMobileAndGender(activity:Activity,map : HashMap<String,Any>){
        fireStore.collection(MyShopKey.USERS)
            .document(getUserID())
            // สำหรับเปลี่ยนแปลงข้อมูลบาง field
            .update(map)
            .addOnSuccessListener {
                when(activity){
                    is UserProfileActivity ->{
                        activity.updateDataSuccess()
                    }
                }
            }
            .addOnFailureListener {
                when(activity){
                    is UserProfileActivity ->{
                        activity.hideProgressDialog()
                        Log.e("error","error while update data")
                    }
                }
            }
    }

    fun addProductToDatabase(activity : Activity,newProduct : Product){
        fireStore.collection(MyShopKey.PRODUCTS)
            .document()
            .set(newProduct, SetOptions.merge())
            .addOnSuccessListener {
                when(activity){
                    is AddProductActivity ->{
                        activity.addProductSuccess()
                    }
                }
            }
            .addOnFailureListener {

            }
    }
}