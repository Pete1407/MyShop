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

    fun setUI(context: Context, cart: Cart) {
        binding.numberOfProduct.text = cart.cart_quantity
        binding.decrease.setOnClickListener {
            if (cart.cart_quantity == "1") {
                if (context is CartListActivity) {
                    context.showProgressDialog()
                    FireStoreClass().removeItemInCart(cart.id, context)
                }
            } else {
                if (context is CartListActivity) {
                    context.showProgressDialog()
                }
                var map = HashMap<String, Any>()
                map[MyShopKey.CART_QUANTITY] = ((cart.cart_quantity.toInt() - 1)).toString()
                FireStoreClass().updateCart(context as CartListActivity, cart, map)
            }
        }
        binding.increase.setOnClickListener {
            if (cart.cart_quantity.toInt() < cart.stock_quantity.toInt()) {
                if (context is CartListActivity) {
                    context.showProgressDialog()
                }

                var map = HashMap<String, Any>()
                map[MyShopKey.CART_QUANTITY] = ((cart.cart_quantity.toInt()) + 1).toString()
                FireStoreClass().updateCart(context as CartListActivity, cart, map)
            } else {
                if (context is CartListActivity) {
                    context.showSnackBar(
                        context.getString(
                            R.string.msg_out_of_stock2,
                            cart.stock_quantity
                        ), true
                    )
                }
            }

        }
    }
}