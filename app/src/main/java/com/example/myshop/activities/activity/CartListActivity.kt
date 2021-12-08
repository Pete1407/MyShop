package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshop.R
import com.example.myshop.activities.adapter.ProductAdapter
import com.example.myshop.databinding.ActivityCartListBinding
import com.example.myshop.firebase.FireStoreClass
import com.example.myshop.model.Cart
import com.example.myshop.model.ObjectType
import com.example.myshop.util.BaseCommon
import com.example.myshop.util.gone
import com.example.myshop.util.visible
import com.example.myshop.activities.fragment.ProductFragment.Companion.PART_CART_ITEM

class CartListActivity : BaseActivity(),BaseCommon {
    private var adapter : ProductAdapter? = null
    private val binding : ActivityCartListBinding by lazy{
        ActivityCartListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()
        setListener()
        setUI()
        FireStoreClass().getProductInCart(this)
    }

    override fun setToolbar() {

    }

    override fun setUI() {
        adapter = ProductAdapter(arrayListOf())
    }

    override fun setListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    fun getCarts(result : ArrayList<Cart>){
        if(result.isEmpty()){
            binding.notFound.visible()
            binding.recyclerview.gone()
        }else{
            binding.notFound.gone()
            binding.recyclerview.visible()
            setAdapter(result)

        }
    }

    fun setAdapter(result : ArrayList<Cart>){
        var data = ArrayList<ObjectType>()
        result.forEach {
            val obj = ObjectType(PART_CART_ITEM,it)
            data.add(obj)
        }
        adapter = ProductAdapter(data)
        binding.recyclerview.adapter = adapter
    }

    companion object{
        fun create(context:Context){
            val intent = Intent(context,CartListActivity::class.java)
            context.startActivity(intent)
        }
    }
}