package com.example.myshop.activities.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myshop.activities.activity.AddProductActivity
import com.example.myshop.activities.adapter.ProductAdapter
import com.example.myshop.databinding.FragmentProductsBinding
import com.example.myshop.firebase.FireStoreClass
import com.example.myshop.model.ObjectType
import com.example.myshop.model.Product
import com.example.myshop.util.BaseCommon
import com.example.myshop.util.gone
import com.example.myshop.util.visible

class ProductFragment : BaseFragment(),BaseCommon {
    private lateinit var binding : FragmentProductsBinding
    private var adapter : ProductAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setListener()
        setUI()
    }

    override fun setToolbar() {}

    override fun setUI() {
        adapter = ProductAdapter(arrayListOf())
    }

    override fun setListener() {
        binding.addProduct.setOnClickListener {
            AddProductActivity.create(requireContext())
        }
    }

    override fun onResume() {
        super.onResume()
        getAllProducts()
    }

    private fun getAllProducts(){
        showProgressDialog()
        FireStoreClass().getProductsFromDatabase(this)
    }

    fun getResultProductSuccess(list : ArrayList<Product>){
        hideProgressDialog()
        setFormatAdapter(list)
    }

    private fun setFormatAdapter(list : ArrayList<Product>){
        if(list.isNotEmpty()){
            binding.labelNoItem.gone()
            binding.recyclerView.visible()
            var dataList = ArrayList<ObjectType>()
            dataList.add(ObjectType(PART_TITLE,null))
            list.forEach {
                dataList.add(ObjectType(PART_ITEM,it))
            }
            adapter = ProductAdapter(dataList)
            binding.recyclerView.adapter = adapter
        }else{
            binding.recyclerView.gone()
            binding.labelNoItem.visible()
        }

    }

    companion object{
        const val PART_TITLE = 10
        const val PART_ITEM = 20
        const val PART_DASHBOARD_ITEM = 30
    }
}