package com.example.myshop.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.myshop.R
import com.example.myshop.databinding.ViewAddStockBinding
import com.example.myshop.model.Cart

class AddProductView(context: Context, attributeSet: AttributeSet) :
    LinearLayoutCompat(context, attributeSet) {

    private val binding = ViewAddStockBinding.inflate(LayoutInflater.from(context), this, true)
    private var quantity: Int = 0

    fun setUI(
        numberOfProd: Int,
        cart: Cart,
        decreaseEvent: ((number: Int, item: Cart) -> Unit?),
        increaseEvent: ((number: Int, item: Cart) -> Unit?)
    ) {
        quantity = numberOfProd
        binding.numberOfProduct.text = numberOfProd.toString()
        binding.decrease.setOnClickListener {
            quantity--
            decreaseQuantityProduct(quantity, decreaseEvent, cart)
        }
        binding.increase.setOnClickListener {
            quantity++
            increaseEvent.invoke(quantity, cart)
            addQuantityProduct(quantity)
        }
    }

    private fun addQuantityProduct(num: Int) {
        binding.numberOfProduct.text = num.toString()
    }

    private fun decreaseQuantityProduct(
        num: Int,
        decreaseEvent: ((number: Int, item: Cart) -> Unit?),
        cart :Cart
    ) {
        if (num <= 1) {
            quantity = 1
            decreaseEvent.invoke(quantity,cart)
            binding.numberOfProduct.text = quantity.toString()
        } else {
            binding.numberOfProduct.text = num.toString()
        }

    }


}