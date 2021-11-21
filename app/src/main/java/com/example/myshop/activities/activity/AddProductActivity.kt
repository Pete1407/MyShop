package com.example.myshop.activities.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshop.R
import com.example.myshop.databinding.ActivityAddProductBinding
import com.example.myshop.util.BaseCommon

class AddProductActivity : BaseActivity(),BaseCommon {
    private val res = resources
    private val binding : ActivityAddProductBinding by lazy{
        ActivityAddProductBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()
        setListener()
        setUI()
    }

    override fun setToolbar() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun setUI() {

    }

    override fun setListener() {
        binding.submit.setOnClickListener {
            if(validateAllInput()){

            }
        }
    }

    fun validateAllInput():Boolean{
        if(binding.titleProduct.text!!.isEmpty()){
            showSnackBar(res.getString(R.string.error_msg_enter_name_product),true)
            return false
        }
        if(binding.priceProduct.text!!.isEmpty()){
            showSnackBar(res.getString(R.string.error_msg_enter_price_product),true)
            return false
        }
        if(binding.quantityProduct.text!!.isEmpty()){
            showSnackBar(res.getString(R.string.error_msg_enter_quantity),true)
            return false
        }
        return true
    }
}