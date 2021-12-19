package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshop.R
import com.example.myshop.databinding.ActivityAddressListBinding
import com.example.myshop.util.BaseCommon

class AddressListActivity : BaseActivity(),BaseCommon {
    private val binding : ActivityAddressListBinding by lazy {
        ActivityAddressListBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()
        setUI()
        setListener()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun setToolbar() {

    }

    override fun setUI() {

    }

    override fun setListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.addAddress.setOnClickListener {
            AddAddressActivity.start(this)
        }

    }

    companion object{
        fun start(context:Context){
            val intent = Intent(context,AddressListActivity::class.java)
            context.startActivity(intent)
        }
    }
}