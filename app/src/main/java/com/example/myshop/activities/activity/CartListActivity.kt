package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import com.example.myshop.model.Product
import com.example.myshop.util.MyShopKey

class CartListActivity : BaseActivity(),BaseCommon {
    private var adapter : ProductAdapter? = null
    private var productList = ArrayList<Product>()
    private var cartList = ArrayList<Cart>()

    private val binding : ActivityCartListBinding by lazy{
        ActivityCartListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()
        setListener()
        setUI()

    }

    override fun onResume() {
        super.onResume()
        showProgressDialog()
        FireStoreClass().getProductList(this)
    }

    override fun setToolbar() {

    }

    override fun setUI() {
    }

    override fun setListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.checkOut.setOnClickListener {
            AddressListActivity.start(this)
        }
    }

    fun getCarts(cartItems : ArrayList<Cart>){
        if(cartItems.isEmpty()){
            binding.notFound.visible()
            binding.recyclerview.gone()
            computeTotalPrice(arrayListOf())
        }else{
            binding.notFound.gone()
            binding.recyclerview.visible()
            this.cartList = cartItems
            for(prod in this.productList){
                for(cart in this.cartList){
                    if(prod.id == cart.product_id){
                        cart.stock_quantity = prod.quantity.toString()
                    }
                }
            }
            setAdapter(this.cartList)
            computeTotalPrice(this.cartList)

        }
    }

    private fun computeTotalPrice(items : ArrayList<Cart>){
        var totalPrice = 0
        val shippingPrice = 10
        if(items.isEmpty()){
            binding.constraintLayout.gone()
        }else{
            binding.constraintLayout.visible()
            for(i in items){
                if(i.cart_quantity.toInt() > 1){
                    totalPrice += (i.price.toInt() * i.cart_quantity.toInt())
                }else{
                    totalPrice += i.price.toInt()
                }
            }
            binding.subTotalValue.text = "$$totalPrice"
            binding.shippingChargeValue.text = "$$shippingPrice"
            binding.totalAmountValue.text = "$${totalPrice+shippingPrice}"
        }
    }

    private fun setAdapter(cartItems : ArrayList<Cart>){
        cartItems.forEach {
            Log.i("result","$it")
        }
        var data = ArrayList<ObjectType>()
        cartItems.forEach {
            val obj = ObjectType(PART_CART_ITEM,it)
            data.add(obj)
        }
        if(adapter == null){
            adapter = ProductAdapter(data)
            adapter!!.setEventDeleteCartListener {
                this.showProgressDialog()
                FireStoreClass().removeItemInCart(it.id,this)
            }
        }else{
            adapter?.refreshDataInCart(cartItems)
        }

        binding.recyclerview.adapter = adapter
    }

    fun deleteProductSuccess(){
        hideProgressDialog()
        //FireStoreClass().getProductInCart(this)
    }

    fun increaseStock(leftStock : Int){
        hideProgressDialog()
        Toast.makeText(this,"avaliable product --> $leftStock",Toast.LENGTH_LONG).show()
    }

    fun decreaseStock(leftStock : Int){
        hideProgressDialog()
        Toast.makeText(this,"avaliable product --> $leftStock",Toast.LENGTH_LONG).show()
    }

    fun getProductListSuccess(prodList : ArrayList<Product>){
        hideProgressDialog()
        this.productList = prodList
        FireStoreClass().getCartList(this)
    }

    fun updateCartSuccess(){
        hideProgressDialog()
        FireStoreClass().getCartList(this)
    }


    companion object{
        const val ACTION_INCREASE = "increase-order"
        const val ACTION_DECREASE = "decrease-order"
        const val ACTION_RETURN_ALL = "return-order"

        fun create(context:Context){
            val intent = Intent(context,CartListActivity::class.java)
            context.startActivity(intent)
        }
    }
}