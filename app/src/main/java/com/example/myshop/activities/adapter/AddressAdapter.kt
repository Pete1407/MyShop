package com.example.myshop.activities.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.databinding.AdapterItemAddressBinding
import com.example.myshop.model.AddressModel

class AddressAdapter(private var list: ArrayList<AddressModel>,val context:Context):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var eventDeleteListener : ((address:AddressModel) -> Unit)? = null
    fun setEventDeleteAddress(event : ((address: AddressModel)->Unit)){
        this.eventDeleteListener = event
    }

    private var eventClickListener : ((idAddress : String)->Unit)? = null
    fun setEventClickAddress(event: ((idAddress : String)->Unit)){
        this.eventClickListener = event
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = AdapterItemAddressBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AddressViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is AddressViewHolder ->{
                holder.bind(list[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun deleteItem(position: Int){
        val item = list[position]
        list.removeAt(position)
        notifyItemRemoved(position)
        eventDeleteListener?.invoke(item)
    }

    inner class AddressViewHolder(val binding : AdapterItemAddressBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(item:AddressModel){
            binding.fullName.text = item.name
            binding.address.text = item.address
            binding.place.text = item.type
            binding.zipcode.text = item.zipCode
            binding.root.setOnClickListener {
                eventClickListener?.invoke(item.id)
            }
        }
    }

}