package com.example.myshop.activities.adapter

import android.util.Log
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
import com.example.myshop.activities.fragment.ProductFragment.Companion.PART_CART_ITEM
import com.example.myshop.databinding.AdapterDashboardItemBinding
import com.example.myshop.databinding.AdapterItemCartBinding
import com.example.myshop.model.Cart


class ProductAdapter(private var list: ArrayList<ObjectType>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var eventDeleteProductListener : ((id : String) -> Unit)? = null
    fun setEventDeleteListener(event : ((id : String)->Unit)){
        eventDeleteProductListener = event
    }

    var eventChooseItemListener : ((id:String)->Unit)? = null
    fun setEventChooseItemProductListener(event:((id : String)->Unit)){
        eventChooseItemListener = event
    }

    var eventDecreaseItemListener : ((number : Int,item:Cart)->Unit)? = null
    fun setEventDecreaseQuantityListener(event : ((number :Int,item:Cart)->Unit)){
        eventDecreaseItemListener = event
    }

    var eventIncreaseItemListener : ((number:Int,item:Cart)->Unit)? = null
    fun setEventIncreaseQuantityListener(event : ((number:Int,item:Cart)-> Unit)){
        eventIncreaseItemListener = event
    }

    fun refreshData(data : ArrayList<Product>){
        list.clear()
        var dataList = ArrayList<ObjectType>()
        dataList.add(ObjectType(PART_TITLE,null))
        data.forEach {
            dataList.add(ObjectType(PART_ITEM,it))
        }
        list = dataList
        notifyDataSetChanged()
    }

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
            PART_CART_ITEM ->{
                val holder = AdapterItemCartBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                CartItemViewHolder(holder)
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
            is CartItemViewHolder ->{
                val item = list[position].objectHolder as Cart
                holder.setItem(item,eventDecreaseItemListener!!,eventIncreaseItemListener!!)
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

    inner class TitleViewHolder(val binding: AdapterItemTitleBinding) : RecyclerView.ViewHolder(binding.root) {

        fun createView() {

        }
    }

    inner class ProductViewHolder(val binding: AdapterItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setItem(product: Product) {
            val context = binding.root.context
            GlideLoader(context).loadImage(product.image,binding.imageProduct)
            binding.titleProduct.text = product.title
            val unit = context.resources.getString(R.string.item)
            binding.quantity.setTextColor(ContextCompat.getColor(context,android.R.color.holo_red_dark))
            binding.quantity.text = context.resources.getString(R.string.Quantity)+": ${product.quantity} $unit"
            binding.priceProduct.text = "$${product.price}"
            binding.deleteProduct.setOnClickListener {
                eventDeleteProductListener!!.invoke(product.id.toString())
            }
            binding.root.setOnClickListener {
                eventChooseItemListener!!.invoke(product.id.toString())
            }
        }
    }

    inner class ItemDashBoardViewHolder(val binding : AdapterDashboardItemBinding):RecyclerView.ViewHolder(binding.root){

        fun setItem(product: Product){
            val context = binding.root.context
            GlideLoader(context).loadImage(product.image,binding.imageProduct)
            binding.title.text = product.title
            binding.price.text = "$${product.price}"
            binding.root.setOnClickListener {
                eventChooseItemListener?.invoke(product.id.toString())
            }
        }
    }

    inner class CartItemViewHolder(val binding : AdapterItemCartBinding):RecyclerView.ViewHolder(binding.root){

        fun setItem(cart : Cart,decreaseEvent: (number: Int, item: Cart) -> Unit?,increaseEvent: (number: Int, item: Cart) -> Unit?){
            binding.productName.text = cart.title
            binding.priceProduct.text = "$${cart.price}"
            GlideLoader(binding.root.context).loadImage(cart.image,binding.image)
            binding.addProduct.setUI(1,cart,decreaseEvent!!,increaseEvent!!)
        }
    }

}