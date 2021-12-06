package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshop.R

class CartListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
    }

    companion object{
        fun create(context:Context){
            val intent = Intent(context,CartListActivity::class.java)
            context.startActivity(intent)
        }
    }
}