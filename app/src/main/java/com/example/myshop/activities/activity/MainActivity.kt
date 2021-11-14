package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.myshop.databinding.ActivityMainBinding
import com.example.myshop.util.MyShopKey
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity() {
    private var username : String = ""
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getCurrentUserName()
        setUI()
    }

    private fun setUI(){
        binding.logout.setOnClickListener {
            signOut()
        }
    }

    private fun signOut(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getCurrentUserName(){
        val sharedPref = getSharedPreferences(MyShopKey.MYSHOPPREF, MODE_PRIVATE)
        username = sharedPref.getString(MyShopKey.USERNAME_LOGIN,"") as String
        binding.username.text = username
    }

    companion object{
        fun create(context:Context){
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}