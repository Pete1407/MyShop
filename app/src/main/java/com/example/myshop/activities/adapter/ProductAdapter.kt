package com.example.myshop.activities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.activities.fragment.ProductFragment.Companion.PART_DASHBOARD_ITEM
import com.example.myshop.databinding.AdapterItemProductBinding
import com.example.myshop.databinding.AdapterItemTitleBinding
import com.example.myshop.model.ObjectType
import com.example.myshop.model.Product
import com.example.myshop.util.GlideLoader
import com.example.myshop.activities.fragment.ProductFragment.Companion.PART_TITLE
import com.example.myshop.activities.fragment.ProductFragment.Companion.PART_ITEM
import com.example.myshop.databinding.AdapterDashboardItemBinding


class ProductAdapter(private val list: List<ObjectType>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PART_TITLE -> {
                val holder = AdapterItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TitleViewHolder(holder)
            }
            PART_ITEM -> {
                val holder = AdapterItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ProductViewHolder(holder)
            }
            PART_DASHBOARD_ITEM ->{
                val holder = AdapterDashboardItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                ItemDashBoardViewHolder(holder)
            }
            else -> {
                val holder = AdapterItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ProductViewHolder(holder)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TitleViewHolder -> { holder.createView() }
            is ProductViewHolder -> {
                val item = list[position].objectHolder as Product
                holder.setItem(item)
            }
            is ItemDashBoardViewHolder ->{
                val item = list[position].objectHolder as Product
                holder.setItem(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class TitleViewHolder(val binding: AdapterItemTitleBinding) : RecyclerView.ViewHolder(binding.root) {

    fun createView() {

    }
}

class ProductViewHolder(val binding: AdapterItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

    fun setItem(product: Product) {
        val context = binding.root.context
        GlideLoader(context).loadImage(product.image,binding.imageProduct)
        binding.titleProduct.text = product.title
        val unit = context.resources.getString(R.string.item)
        binding.quantity.setTextColor(ContextCompat.getColor(context,android.R.color.holo_red_dark))
        binding.quantity.text = context.resources.getString(R.string.Quantity)+": ${product.quantity} $unit"
        binding.priceProduct.text = "$${product.price}"
    }
}

class ItemDashBoardViewHolder(val binding : AdapterDashboardItemBinding):RecyclerView.ViewHolder(binding.root){

    fun setItem(product: Product){
        val context = binding.root.context
        GlideLoader(context).loadImage(product.image,binding.imageProduct)
        binding.title.text = product.title
        binding.price.text = "$${product.price}"
    }
}