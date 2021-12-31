package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import android.icu.util.TimeUnit
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TimeUtils
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.example.myshop.activities.adapter.ItemAdapter
import com.example.myshop.databinding.ActivityOrderDetailBinding
import com.example.myshop.model.Cart
import com.example.myshop.model.Order
import com.example.myshop.util.BaseCommon
import com.example.myshop.util.MyShopKey
import com.google.firestore.v1.StructuredQuery
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderDetailActivity : BaseActivity(),BaseCommon {

    private var order : Order = Order()

    private val binding : ActivityOrderDetailBinding by lazy{
        ActivityOrderDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        this.order = intent.getParcelableExtra(PARAM_ORDER)!!
        setToolbar()
        setUI()
        setListener()
    }

    override fun setToolbar() {
        binding.back.setOnClickListener {
            finish()
        }
    }

    override fun setUI() {
        binding.idValue.text = "My Order ${order.id}"
        val dateFormat = SimpleDateFormat(MyShopKey.DATE_FORMAT,Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = order.order_dateTime
        val dateText = dateFormat.format(calendar.time)
        val diffInMillisecond = System.currentTimeMillis() - order.order_dateTime
        val diffInHour = java.util.concurrent.TimeUnit.MILLISECONDS.toHours(diffInMillisecond)
        Log.i(MyShopKey.TAG,"${System.currentTimeMillis()} - ${order.order_dateTime} = $$diffInMillisecond")
        Log.i(MyShopKey.TAG,"diffInHour --> $diffInHour")
        when{
            diffInHour < 1 ->{
                binding.statusValue.setTextColor(ContextCompat.getColor(this,R.color.snackbar_unsuccess))
                binding.statusValue.text = getString(R.string.status_order_pending)
            }
            diffInHour < 2 ->{
                binding.statusValue.setTextColor(ContextCompat.getColor(this,R.color.bg_status_in_progress))
                binding.statusValue.text = getString(R.string.status_order_in_progress)
            }else->{
                binding.statusValue.setTextColor(ContextCompat.getColor(this,R.color.bg_status_delivered))
                binding.statusValue.text = getString(R.string.status_order_delivered)
            }
        }
        binding.dateValue.text = dateText
        binding.productItemTitle.text = "${getString(R.string.product_item,order.items.size.toString())}"
        binding.place.text = order.addressModel.type
        binding.name.text = order.addressModel.name
        binding.note.text = order.addressModel.additionalNote
        binding.zipcode.text = order.addressModel.zipCode
        binding.address.text = order.addressModel.address
        binding.shippingChargeValue.text = "$10.0"
        val subTotal = computePrice(order.items)
        binding.subTotalValue.text = "$${subTotal}"
        val totalPrice = computeCharge(subTotal)
        binding.totalAmountValue.text = "$$totalPrice"
        setAdapter(order.items)
    }

    override fun setListener() {

    }

    private fun setAdapter(list : ArrayList<Cart>){
        var items = ArrayList<Any>()
        list.forEach {
            items.add(it)
        }
        val adapter = ItemAdapter(items)
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

    companion object{
        const val PARAM_ORDER = "param-order"

        fun start(context:Context,order : Order){
            val intent = Intent(context,OrderDetailActivity::class.java).apply {
                putExtra(PARAM_ORDER,order)
            }
            context.startActivity(intent)
        }
    }
}