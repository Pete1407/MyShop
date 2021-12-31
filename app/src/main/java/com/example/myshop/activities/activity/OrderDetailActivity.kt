package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshop.R
import com.example.myshop.activities.adapter.ItemAdapter
import com.example.myshop.databinding.ActivityOrderDetailBinding
import com.example.myshop.model.Cart
import com.example.myshop.model.Order
import com.example.myshop.util.BaseCommon
import com.google.firestore.v1.StructuredQuery

class OrderDetailActivity : BaseActivity(),BaseCommon {

    private var order : Order = Order()

    private val binding : ActivityOrderDetailBinding by lazy{
        ActivityOrderDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        this.order = intent.getParcelableExtra<Order>(PARAM_ORDER)!!
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
        binding.dateValue.text = order.order_dateTime.toString()
        binding.statusValue.text = "Delivered"
        binding.productItemTitle.text = getString(R.string.product_item,order.items.size)
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