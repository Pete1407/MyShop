package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myshop.R
import com.example.myshop.activities.adapter.CheckOutAdapter
import com.example.myshop.databinding.ActivityCheckOutBinding
import com.example.myshop.firebase.FireStoreClass
import com.example.myshop.util.BaseCommon
import com.example.myshop.activities.adapter.CheckOutAdapter.Companion.TYPE_PRODUCT
import com.example.myshop.activities.adapter.CheckOutAdapter.Companion.TYPE_ADDRESS
import com.example.myshop.activities.adapter.CheckOutAdapter.Companion.TYPE_RECEIPT
import com.example.myshop.model.*


class CheckOutActivity : BaseActivity(),BaseCommon {

    private var itemList = ArrayList<Cart>()
    private var prodList = ArrayList<Product>()
    private var selectedAddress : AddressModel? = null
    private var adapter : CheckOutAdapter? = null
    private var order : Order? = null
    private var subTotal : Double = 0.0
    private var total : Double = 0.0

    private val binding : ActivityCheckOutBinding by lazy{
        ActivityCheckOutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        selectedAddress = intent.getParcelableExtra(PARAM_ADDRESS)
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
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.placeOrder.setOnClickListener {
            val title = "order_${System.currentTimeMillis()}"
            showProgressDialog()
            this.selectedAddress?.let{
                order = Order(
                    user_id = FireStoreClass().getUserID(),
                    items = itemList,
                    addressModel = it,
                    title = title,
                    image = itemList[0].image,
                    sub_total_amount = subTotal.toString(),
                    total_amount = total.toString(),
                    shipping_charge = "10.0"
                )
                FireStoreClass().checkOutCartAndProduct(itemList,this)
                FireStoreClass().addOrderToDB(order!!,this)

            }
        }
    }

    override fun setUI() {
    }

    override fun setListener() {

    }

    fun getCarts(cartList : ArrayList<Cart>){
        hideProgressDialog()
        var item = ArrayList<ObjectType>()
        this.subTotal = computePrice(cartList)
        this.total = computeCharge(subTotal)
        var collectText = "$$subTotal $$total"
        for(prod in prodList){
            for(cart in cartList){
                if(prod.id == cart.product_id){
                    cart.stock_quantity = prod.quantity.toString()
                }
            }
        }
        this.itemList = cartList
        item.add(ObjectType(TYPE_PRODUCT,itemList))
        item.add(ObjectType(TYPE_ADDRESS,selectedAddress))
        item.add(ObjectType(TYPE_RECEIPT,collectText))
        adapter = CheckOutAdapter(item)
        binding.recyclerView.adapter = adapter
    }

    private fun computePrice(list : ArrayList<Cart>):Double{
        var subTotal  = 0.0
        for(i in list){
            if(i.cart_quantity.toInt() > 1){
                subTotal += (i.price.toInt() * i.cart_quantity.toInt())
            }else{
                subTotal += i.price.toInt()
            }
        }
        return subTotal
    }

    private fun computeCharge(subTotal : Double):Double{
        return subTotal + 10
    }

    fun addOrderSuccess(){
        hideProgressDialog()
        Toast.makeText(this,getString(R.string.save_order_msg),Toast.LENGTH_SHORT).show()
        val intent = Intent(this,DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    fun getProductListSuccess(prodList : ArrayList<Product>){
        this.prodList = prodList
        FireStoreClass().getCartList(this)
    }

    companion object{
        const val PARAM_ADDRESS = "param-address"

        fun start(context: Context){
            val intent = Intent(context,CheckOutActivity::class.java)
            context.startActivity(intent)
        }

        fun start(context: Context,address : AddressModel){
            val intent = Intent(context,CheckOutActivity::class.java).apply {
                putExtra(PARAM_ADDRESS,address)
            }
            context.startActivity(intent)
        }
    }

}