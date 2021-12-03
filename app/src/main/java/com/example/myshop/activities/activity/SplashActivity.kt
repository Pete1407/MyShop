package com.example.myshop.activities.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.myshop.databinding.ActivitySplashBinding
import com.example.myshop.util.MyShopKey

class SplashActivity : AppCompatActivity() {
    private val binding : ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if(checkLogInYet()){
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            },3000)
        }else{

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            },3000)
        }
    }





    private fun checkLogInYet():Boolean{
        val sharedPref = getSharedPreferences(MyShopKey.MYSHOPPREF, MODE_PRIVATE)
        val userName = sharedPref!!.getString(MyShopKey.USERNAME_LOGIN,"") as String
        Log.i("result","username --> $userName")
        if(userName.isEmpty()){
            return false
        }
        return true
    }
}