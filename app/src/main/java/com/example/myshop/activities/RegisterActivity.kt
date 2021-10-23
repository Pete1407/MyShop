package com.example.myshop.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.example.myshop.R
import com.example.myshop.databinding.ActivityRegisterBinding

class RegisterActivity : BaseActivity() {

    private val binding : ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    companion object{
        fun create(context:Context){
            val intent = Intent(context,RegisterActivity::class.java)
            context.startActivity(intent)
        }

    }
}