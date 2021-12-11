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
import com.example.myshop.util.gone
import com.example.myshop.util.invisible
import com.example.myshop.util.visible

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
        hideDecreaseProduct()
        binding.numberOfProduct.text = numberOfProd.toString()
        binding.decrease.setOnClickListener {
            quantity--
            hideDecreaseProduct()
            decreaseEvent.invoke(quantity,cart)
            decreaseQuantityProduct(quantity)
        }
        binding.increase.setOnClickListener {
            quantity++
            hideDecreaseProduct()
            increaseEvent.invoke(quantity, cart)
            addQuantityProduct(quantity)
        }
    }

    private fun addQuantityProduct(num: Int) {
        binding.numberOfProduct.text = num.toString()
    }

    private fun decreaseQuantityProduct(num: Int) {
        if (num == 1) {
            quantity = 1
            hideDecreaseProduct()
            binding.numberOfProduct.text = quantity.toString()
        } else {
            hideDecreaseProduct()
            binding.numberOfProduct.text = num.toString()
        }

    }

    private fun hideDecreaseProduct(){
        if(quantity == 1){
            binding.decrease.invisible()
        }else{
            binding.decrease.visible()
        }
    }

    fun getQuantity():Int{
        return quantity
    }


}