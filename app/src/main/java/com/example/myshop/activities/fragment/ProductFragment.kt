package com.example.myshop.activities.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myshop.activities.activity.AddProductActivity
import com.example.myshop.databinding.FragmentProductsBinding
import com.example.myshop.util.BaseCommon

class ProductFragment : BaseFragment(),BaseCommon {
    private lateinit var binding : FragmentProductsBinding

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

    override fun setUI() {}

    override fun setListener() {
        binding.addProduct.setOnClickListener {
            AddProductActivity.create(requireContext())
        }
    }
}