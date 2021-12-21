package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myshop.R
import com.example.myshop.databinding.ActivityAddAddressBinding
import com.example.myshop.model.AddressModel
import com.example.myshop.util.BaseCommon

class AddAddressActivity : BaseActivity(), BaseCommon{

    private var address : AddressModel? = null
    private var place : String = ""
    private val binding: ActivityAddAddressBinding by lazy {
        ActivityAddAddressBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        setToolbar()
        setUI()
        setListener()
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
            Log.i("result","click...")
        }
    }

    private fun detectInput():Boolean{
        if(binding.fullname.text!!.isBlank()){
            showSnackBar(getString(R.string.error_msg_enter_full_name),true)
            return false
        }else if(binding.phoneNumber.text!!.isBlank()){
            showSnackBar(getString(R.string.error_msg_enter_phone_number),true)
            return false
        }else if(binding.address.text!!.isBlank()){
            showSnackBar(getString(R.string.error_msg_enter_address),true)
            return false
        }else if(binding.zipcode.text!!.isBlank()){
            showSnackBar(getString(R.string.error_msg_enter_zip_code),true)
            return false
        }else if(binding.note.text!!.isBlank()){
            showSnackBar(getString(R.string.error_msg_enter_note),true)
            return false
        }else if(place.isBlank()){
            showSnackBar(getString(R.string.error_msg_enter_place),true)
            return false
        }
        return true
    }

    companion object{
        fun start(context: Context){
            val intent = Intent(context,AddAddressActivity::class.java)
            context.startActivity(intent)
        }
    }
}