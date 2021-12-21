package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshop.R
import com.example.myshop.databinding.ActivityAddAddressBinding
import com.example.myshop.util.BaseCommon

class AddAddressActivity : BaseActivity(), BaseCommon {

    private val binding: ActivityAddAddressBinding by lazy {
        ActivityAddAddressBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
    }

    override fun setToolbar() {

    }

    override fun setUI() {

    }

    override fun setListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.submit.setOnClickListener {

        }
    }

    companion object{
        fun start(context: Context){
            val intent = Intent(context,AddAddressActivity::class.java)
            context.startActivity(intent)
        }
    }
}