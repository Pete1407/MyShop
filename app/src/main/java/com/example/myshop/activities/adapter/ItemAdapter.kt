package com.example.myshop.activities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.databinding.AdapterAnItemBinding
import com.example.myshop.model.Cart
import com.example.myshop.model.Order
import com.example.myshop.util.GlideLoader
import com.example.myshop.util.ScreenUtility

class ItemAdapter(private var list : ArrayList<Any>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var eventClickListener:((order:Order)->Unit)? = null

    fun setEventClickOrder(event : ((order : Order)->Unit)){
        this.eventClickListener = event
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = AdapterAnItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        //viewHolder.layout.maxWidth = (ScreenUtility.getDisplay().widthPixels * 0.5).toInt()
        return ItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemViewHolder){
            when(list[position]){
                is Cart ->{
                    holder.bind(list[position])
                }
                is Order ->{
                    holder.bind(list[position])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ItemViewHolder(val binding : AdapterAnItemBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(item : Any){
            val context = binding.root.context
            if(item is Cart){
                binding.titleProduct.text = item.title
                binding.priceProduct.text = "$${item.price}"
                binding.sizeProduct.text = "${context.getString(R.string.total,item.cart_quantity.toString())}"
                GlideLoader(binding.root.context).loadImage(item.image,binding.imageProduct)
            }else if(item is Order){
                binding.titleProduct.text = item.title
                binding.priceProduct.text = "$${item.total_amount}"
                GlideLoader(binding.root.context).loadImage(item.image,binding.imageProduct)
                binding.root.setOnClickListener {
                    eventClickListener?.invoke(item)
                }
            }

        }
    }

}