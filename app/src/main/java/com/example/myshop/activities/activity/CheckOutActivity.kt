package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshop.R
import com.example.myshop.activities.adapter.CheckOutAdapter
import com.example.myshop.databinding.ActivityCheckOutBinding
import com.example.myshop.firebase.FireStoreClass
import com.example.myshop.model.AddressModel
import com.example.myshop.model.Cart
import com.example.myshop.model.ObjectType
import com.example.myshop.util.BaseCommon
import com.example.myshop.activities.adapter.CheckOutAdapter.Companion.TYPE_PRODUCT
import com.example.myshop.activities.adapter.CheckOutAdapter.Companion.TYPE_ADDRESS
import com.example.myshop.activities.adapter.CheckOutAdapter.Companion.TYPE_RECEIPT



class CheckOutActivity : BaseActivity(),BaseCommon {

    private var itemList = ArrayList<Cart>()
    private var selectedAddress : AddressModel? = null
    private var adapter : CheckOutAdapter? = null
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
        FireStoreClass().getProductInCart(this)
    }

    override fun setToolbar() {
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.placeOrder.setOnClickListener {

        }
    }

    override fun setUI() {
    }

    override fun setListener() {

    }

    fun getCarts(list : ArrayList<Cart>){
        var item = ArrayList<ObjectType>()
        var sumPrice = computePrice(list)
        var totalPrice = computeCharge(sumPrice)
        var collectText = "$$sumPrice $$totalPrice"
        this.itemList = list
        item.add(ObjectType(TYPE_PRODUCT,itemList))
        item.add(ObjectType(TYPE_ADDRESS,selectedAddress))
        item.add(ObjectType(TYPE_RECEIPT,collectText))
        adapter = CheckOutAdapter(item)
        binding.recyclerView.adapter = adapter
    }

    private fun computePrice(list : ArrayList<Cart>):Double{
        var subTotal  = 0.0
        for(i in list){
            subTotal += i.price.toInt()
        }
        return subTotal
    }

    private fun computeCharge(subTotal : Double):Double{
        return subTotal + 10
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