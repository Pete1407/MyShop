package com.example.myshop.activities.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myshop.activities.activity.OrderDetailActivity
import com.example.myshop.activities.adapter.ItemAdapter
import com.example.myshop.databinding.FragmentOrdersBinding
import com.example.myshop.firebase.FireStoreClass
import com.example.myshop.model.Cart
import com.example.myshop.model.Order
import com.example.myshop.util.BaseCommon
import com.example.myshop.util.MyShopKey

class OrderFragment : BaseFragment(),BaseCommon {
    private lateinit var binding : FragmentOrdersBinding
    private var orders = ArrayList<Order>()
    private var order : Order = Order()
    private var adapter : ItemAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setUI()
        setListener()
    }

    override fun onResume() {
        super.onResume()
        showProgressDialog()
        FireStoreClass().getOrder(this)
    }

    override fun setToolbar() {

    }

    override fun setUI() {

    }

    override fun setListener() {

    }

    fun getMyOrderSuccess(orders : ArrayList<Order>){
        hideProgressDialog()
        this.orders = orders
        setAdapter(this.orders)
        Log.i(MyShopKey.TAG,"size --> ${this.orders.size}")
    }

    private fun setAdapter(orders : ArrayList<Order>){
        val newData = ArrayList<Any>()
        for(i in orders){
            newData.add(i)
        }
        adapter = ItemAdapter(newData)
        adapter!!.setEventClickOrder {
            order = it
            showOrderDetail(order)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun showOrderDetail(order : Order){
        OrderDetailActivity.start(requireContext(),order)
    }

}