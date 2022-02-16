package com.example.myshop.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.myshop.R
import com.example.myshop.activities.activity.CartListActivity
import com.example.myshop.databinding.ViewAddStockBinding
import com.example.myshop.firebase.FireStoreClass
import com.example.myshop.model.Cart
import com.example.myshop.util.MyShopKey
import com.example.myshop.util.gone
import com.example.myshop.util.invisible
import com.example.myshop.util.visible
import kotlinx.android.synthetic.main.activity_add_product.view.*

class AddProductView(context: Context, attributeSet: AttributeSet) :
    LinearLayoutCompat(context, attributeSet) {

    private val binding = ViewAddStockBinding.inflate(LayoutInflater.from(context), this, true)

    fun setUI(
        numberOfOrder :Int,
        dataOfCart : Cart,
        eventCheckStock : ((numberOfNewOrder:Int,cart:Cart)-> Unit)
    ) {
        var newOrder = numberOfOrder
        binding.numberOfProduct.text = newOrder.toString()
        binding.increase.setOnClickListener {
            newOrder++
            eventCheckStock.invoke(newOrder,dataOfCart)
            binding.numberOfProduct.text = newOrder.toString()
        }
        binding.decrease.setOnClickListener {
            newOrder--
            binding.numberOfProduct.text = newOrder.toString()
        }
    }

}