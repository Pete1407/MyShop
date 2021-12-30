package com.example.myshop.activities.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myshop.activities.activity.CartListActivity
import com.example.myshop.activities.activity.DetailProductActivity
import com.example.myshop.activities.activity.SettingActivity
import com.example.myshop.activities.adapter.ProductAdapter
import com.example.myshop.databinding.FragmentDashboardBinding
import com.example.myshop.firebase.FireStoreClass
import com.example.myshop.model.ObjectType
import com.example.myshop.model.Product
import com.example.myshop.util.BaseCommon
import com.example.myshop.util.gone
import com.example.myshop.util.visible
import com.example.myshop.activities.fragment.ProductFragment.Companion.PART_TITLE
import com.example.myshop.activities.fragment.ProductFragment.Companion.PART_DASHBOARD_ITEM



class DashboardFragment : BaseFragment(),BaseCommon {
    private lateinit var binding : FragmentDashboardBinding
    private var adapter : ProductAdapter? = null
//    private val viewModel : DashboardViewModel by lazy {
//        ViewModelProvider(this).get(DashboardViewModel::class.java)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setUI()
        setListener()

    }

    override fun setToolbar() {

    }

    override fun setUI() {
        setAdapter(arrayListOf())
        showProgressDialog()
        FireStoreClass().getProductInDashBoard(this)
    }

    override fun setListener() {
        binding.setting.setOnClickListener {
            SettingActivity.create(requireContext())
        }
        binding.cart.setOnClickListener {
            CartListActivity.create(requireContext())
        }
    }

    fun getDataSuccess(list : ArrayList<Product>){
        hideProgressDialog()
        setAdapter(list)
    }

    private fun setAdapter(list : ArrayList<Product>){
        var dataList = ArrayList<ObjectType>()
        if(list.isNotEmpty()){
            binding.recyclerView.visible()
            binding.labelNoItem.gone()
            list.forEach {
                dataList.add(ObjectType(PART_DASHBOARD_ITEM,it))
            }
            adapter = ProductAdapter(dataList)
            adapter!!.setEventChooseItemProductListener {
                DetailProductActivity.start(requireContext(),it.toString())
            }
            binding.recyclerView.adapter = adapter

        }else{
            binding.recyclerView.gone()
            binding.labelNoItem.visible()
        }
    }
}