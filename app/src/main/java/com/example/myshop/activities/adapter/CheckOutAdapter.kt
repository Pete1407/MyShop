package com.example.myshop.activities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.databinding.AdapterItemReceiptBinding
import com.example.myshop.databinding.AdapterItemSelectedAddressBinding
import com.example.myshop.databinding.AdapterItemsOrderedBinding
import com.example.myshop.model.AddressModel
import com.example.myshop.model.Cart
import com.example.myshop.model.ObjectType

class CheckOutAdapter(var list : ArrayList<ObjectType>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var adapter :ItemAdapter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PRODUCT -> {
                val viewHolder = AdapterItemsOrderedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                OrderedProductViewHolder(viewHolder)
            }
            TYPE_ADDRESS -> {
                val viewHolder = AdapterItemSelectedAddressBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AddressViewHolder(viewHolder)
            }
            else ->{
                val viewHolder = AdapterItemReceiptBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ReceiptViewHolder(viewHolder)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is OrderedProductViewHolder ->{
                holder.bind(list[position].objectHolder as ArrayList<Cart>)
            }
            is AddressViewHolder ->{
                holder.bind(list[position].objectHolder as AddressModel)
            }
            is ReceiptViewHolder ->{
                holder.bind(list[position].objectHolder as String)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class OrderedProductViewHolder(val binding : AdapterItemsOrderedBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(list : ArrayList<Cart>){
            adapter = ItemAdapter(list)
            binding.title.text = binding.root.context.getString(R.string.product_item,list.size.toString())
            binding.recyclerView.adapter = adapter
        }
    }

    inner class AddressViewHolder(val binding : AdapterItemSelectedAddressBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(address : AddressModel){
            binding.place.text = address.type
            binding.name.text = address.name
            binding.address.text = address.address
            binding.note.text = address.additionalNote
            binding.zipCode.text = address.zipCode
        }
    }

    inner class ReceiptViewHolder(val binding : AdapterItemReceiptBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(text : String){
            val priceText = text.split(" ")
            binding.shippingChargeValue.text = "$10.0"
            binding.subTotalValue.text = "${priceText[0]}"
            binding.totalAmountValue.text = "${priceText[1]}"
        }
    }

    companion object{
        val TYPE_PRODUCT = 0
        val TYPE_ADDRESS = 1
        val TYPE_RECEIPT = 2
    }
}