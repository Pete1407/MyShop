package com.example.myshop.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myshop.R
import com.example.myshop.databinding.ActivityMainBinding
import com.example.myshop.firebase.FireStoreClass
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity() {
    private var userId : String = ""
    private var emailId : String = ""
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        userId = intent.getStringExtra("user_id").toString()
        emailId = intent.getStringExtra("email_id").toString()
        setUI()
    }

    private fun setUI(){
        binding.userId.text = "${resources.getString(R.string.user_id)}: $userId"
        binding.emailId.text = "${resources.getString(R.string.email_id)}: $emailId"
        binding.logout.setOnClickListener {
            signOut()
        }
    }

    private fun signOut(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object{
        fun create(context:Context){
            val intent = Intent(context,MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}