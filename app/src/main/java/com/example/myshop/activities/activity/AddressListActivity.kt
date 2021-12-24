package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.myshop.R
import com.example.myshop.activities.adapter.AddressAdapter
import com.example.myshop.databinding.ActivityAddressListBinding
import com.example.myshop.firebase.FireStoreClass
import com.example.myshop.model.AddressModel
import com.example.myshop.util.BaseCommon
import com.example.myshop.util.SwipeToDeleteCallback
import com.example.myshop.util.gone
import com.example.myshop.util.visible

class AddressListActivity : BaseActivity(),BaseCommon {

    private var adapter : AddressAdapter? = null
    private val binding : ActivityAddressListBinding by lazy {
        ActivityAddressListBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()
        setUI()
        setListener()
    }

    override fun onResume() {
        super.onResume()
        FireStoreClass().getAddresses(this)
    }

    override fun setToolbar() {

    }

    override fun setUI() {

    }

    override fun setListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.addAddress.setOnClickListener {
            AddAddressActivity.start(this)
        }
    }

    fun getAddressList(list : ArrayList<AddressModel>){
        if(list.isEmpty()){
            binding.recyclerView.gone()
            binding.notFound.visible()
        }else{
            binding.notFound.gone()
            binding.recyclerView.visible()
            adapter = AddressAdapter(list,this)
            adapter!!.setEventDeleteAddress {
                FireStoreClass().deleteAnAddress(it.id,this)
            }
            adapter!!.setCheckEmptyList {
                checkSizeList(it)
            }
            val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter!!))
            itemTouchHelper.attachToRecyclerView(binding.recyclerView)
            binding.recyclerView.adapter = adapter
        }
    }

    private fun checkSizeList(list:ArrayList<AddressModel>){
        list.forEachIndexed { index, addressModel ->
            Log.i("result","$index --> ${addressModel.name}")
        }
        if(list.isEmpty()){
            binding.recyclerView.gone()
            binding.notFound.visible()
        }else{
            binding.notFound.gone()
            binding.recyclerView.visible()
        }
    }

    fun deleteAddressSuccess(){
        Toast.makeText(this,"delete success",Toast.LENGTH_SHORT).show()
    }

    companion object{
        fun start(context:Context){
            val intent = Intent(context,AddressListActivity::class.java)
            context.startActivity(intent)
        }
    }
}