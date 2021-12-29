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
import kotlinx.android.synthetic.main.activity_add_product.view.*

class AddProductView(context: Context, attributeSet: AttributeSet) :
    LinearLayoutCompat(context, attributeSet) {

    private val binding = ViewAddStockBinding.inflate(LayoutInflater.from(context), this, true)
    private var quantity: Int = 0

    fun setUI(
        numberOfProd: Int,
        cart: Cart,
        decreaseEvent: ((number: Int, item: Cart) -> Unit?),
        increaseEvent: ((number: Int, item: Cart) -> Unit?),
        quantityProduct : Int,
        checkStockEvent : ((number: Int, item: Cart) -> Unit)
    ) {
        quantity = numberOfProd
        Log.i("result","korea --> $quantityProduct")
        binding.numberOfProduct.text = numberOfProd.toString()

        binding.decrease.setOnClickListener {
            quantity--
            checkStockEvent.invoke(quantity,cart)
            decreaseEvent.invoke(quantity,cart)
            decreaseQuantityProduct(quantity)
        }
        binding.increase.setOnClickListener {
            quantity++
            checkStockEvent.invoke(quantity,cart)
            addQuantityProduct(quantity,quantityProduct)
            increaseEvent.invoke(quantity, cart)

        }
    }

    private fun addQuantityProduct(num: Int,quanInProd : Int) {
        Log.i("result","tsunami --> $num  $quanInProd")
        if(num == quanInProd && quanInProd !=0){
            --quantity
            binding.numberOfProduct.text = quantity.toString()
        }else{
            binding.numberOfProduct.text = num.toString()
        }

    }

    private fun decreaseQuantityProduct(num: Int) {
        if(num == 0){
            quantity = 1
            binding.numberOfProduct.text = quantity.toString()
        }else{
            binding.numberOfProduct.text = num.toString()
        }

    }

    fun getQuantity():Int{
        return quantity
    }


}