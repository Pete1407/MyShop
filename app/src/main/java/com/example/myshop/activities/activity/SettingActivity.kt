package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myshop.R
import com.example.myshop.databinding.ActivitySettingBinding
import com.example.myshop.firebase.FireStoreClass
import com.example.myshop.model.User
import com.example.myshop.util.BaseCommon
import com.google.firebase.auth.FirebaseAuth
import com.example.myshop.activities.activity.UserProfileActivity.Companion.ACTION_EDIT_INFO
import com.example.myshop.util.GlideLoader

class SettingActivity : BaseActivity(),BaseCommon {
    private var currentUser : User? = null

    private val binding : ActivitySettingBinding by lazy{
        ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUI()
        setToolbar()
        setListener()
    }

    override fun onResume() {
        super.onResume()
        getDetailOfUser()
    }

    override fun setToolbar() {}

    override fun setUI() {}

    override fun setListener() {
        binding.back.setOnClickListener {
            onBackPressed()
        }
        binding.edit.setOnClickListener {
            UserProfileActivity.create(this,currentUser!!, ACTION_EDIT_INFO)
        }
        binding.logOut.setOnClickListener {
            logOut()
        }
    }

    private fun logOut(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this,LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun getDetailOfUser(){
        showProgressDialog()
        FireStoreClass().getCurrentUserByID(this)
    }

    fun getDataUser(result : User?){
        Log.i("result",result.toString())
        currentUser = result
        result?.let {
            binding.name.text = "${it.firstname} ${it.lastname}"
            binding.gender.text = it.gender
            binding.email.text = it.email
            binding.phoneNumber.text = it.mobile.toString()
            GlideLoader(this).loadImage(it.image,binding.image)
        }
    }

    companion object{
        fun create(context : Context){
            val intent = Intent(context,SettingActivity::class.java)
            context.startActivity(intent)
        }
    }
}