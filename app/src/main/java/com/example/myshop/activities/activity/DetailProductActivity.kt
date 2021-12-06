package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myshop.databinding.ActivityDetailProductBinding
import com.example.myshop.firebase.FireStoreClass
import com.example.myshop.model.Product
import com.example.myshop.util.BaseCommon
import com.example.myshop.util.GlideLoader

class DetailProductActivity : BaseActivity(),BaseCommon {

    private var idProduct : String? = null
    private var item : Product? = null
    private val binding : ActivityDetailProductBinding by lazy{
        ActivityDetailProductBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        idProduct = intent.getStringExtra(PARAM_ID_PRODUCT).toString()
        setToolbar()
        setListener()
    }

    override fun onResume() {
        super.onResume()
        showProgressDialog()
        FireStoreClass().getDetailProduct(idProduct.toString(),this)
    }

    override fun setToolbar() {
    }

    override fun setUI() {
        item?.let {
            binding.title.text = it.title
            binding.nameProduct.text = it.title
            binding.descriptionProduct.text = it.description
            binding.numberOfProduct.text = it.quantity.toString()
            GlideLoader(this).loadImage(it.image,binding.image)
        }
    }

    override fun setListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.addToCart.setOnClickListener {

        }
    }

    fun getDetailProduct(product:Product){
        item = product
        hideProgressDialog()
        setUI()
    }

    companion object{
        const val PARAM_ID_PRODUCT = "product-id"

        fun start(context:Context,id:String){
            val intent = Intent(context,DetailProductActivity::class.java)
            intent.putExtra(PARAM_ID_PRODUCT,id)
            context.startActivity(intent)
        }
    }
}