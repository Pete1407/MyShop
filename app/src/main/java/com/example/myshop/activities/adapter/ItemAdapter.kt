package com.example.myshop.activities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.databinding.AdapterAnItemBinding
import com.example.myshop.model.Cart
import com.example.myshop.util.GlideLoader
import com.example.myshop.util.ScreenUtility

class ItemAdapter(private var list : ArrayList<Cart>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = AdapterAnItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        //viewHolder.layout.maxWidth = (ScreenUtility.getDisplay().widthPixels * 0.5).toInt()
        return ItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemViewHolder){
            holder.bind(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ItemViewHolder(val binding : AdapterAnItemBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(item : Cart){
            binding.titleProduct.text = item.title
            binding.priceProduct.text = "$${item.price}"
            GlideLoader(binding.root.context).loadImage(item.image,binding.imageProduct)
        }
    }

}