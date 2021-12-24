package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myshop.R
import com.example.myshop.databinding.ActivityAddAddressBinding
import com.example.myshop.firebase.FireStoreClass
import com.example.myshop.model.AddressModel
import com.example.myshop.util.BaseCommon
import com.google.firebase.auth.FirebaseAuth

class AddAddressActivity : BaseActivity(), BaseCommon{

    private var address : AddressModel? = null
    private var place:String = ""
    private val binding: ActivityAddAddressBinding by lazy {
        ActivityAddAddressBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()
        setUI()
        setListener()
    }

    override fun setToolbar() {

    }

    override fun setUI() {
        place = resources.getString(R.string.home)
    }

    override fun setListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.submit.setOnClickListener {
            if(detectInput()){
                showProgressDialog()
                val fullName = binding.fullname.text.toString()
                val phoneNumber = binding.phoneNumber.text.toString()
                val addressValue = binding.address.text.toString()
                val zipCode = binding.zipcode.text.toString()
                val note = binding.note.text.toString()
                val placeValue = place
                address = AddressModel(
                    user_id = FireStoreClass().getUserID(),
                    name = fullName,
                    mobileNumber = phoneNumber,
                    address = addressValue,
                    zipCode = zipCode,
                    additionalNote = note,
                    type = placeValue,
                    otherDetails = note
                )
                FireStoreClass().saveAddress(address!!,this)

            }
        }
        binding.home.setOnClickListener {
            place = getString(R.string.home)
            setButton(it.id)
        }
        binding.office.setOnClickListener {
            place = getString(R.string.office)
            setButton(it.id)
        }
        binding.other.setOnClickListener {
            place = getString(R.string.other)
            setButton(it.id)
        }
    }

    private fun setButton(id :Int){
        when(id){
            binding.home.id ->{
                binding.home.isChecked = true
                binding.office.isChecked = false
                binding.other.isChecked = false
            }
            binding.office.id ->{
                binding.office.isChecked = true
                binding.home.isChecked = false
                binding.other.isChecked = false
            }
            else ->{
                binding.other.isChecked = true
                binding.home.isChecked = false
                binding.office.isChecked = false
            }
        }

    }

    private fun detectInput():Boolean{
        if(binding.fullname.text!!.isBlank()){
            showSnackBar(getString(R.string.error_msg_enter_full_name),true)
            return false
        }else if(binding.phoneNumber.text!!.isBlank()){
            showSnackBar(getString(R.string.error_msg_enter_phone_number),true)
            return false
        }else if(binding.address.text!!.isBlank()){
            showSnackBar(getString(R.string.error_msg_enter_address),true)
            return false
        }else if(binding.zipcode.text!!.isBlank()){
            showSnackBar(getString(R.string.error_msg_enter_zip_code),true)
            return false
        }else if(binding.note.text!!.isBlank()){
            showSnackBar(getString(R.string.error_msg_enter_note),true)
            return false
        }
        return true
    }

    fun saveAddressSuccess(){
        hideProgressDialog()
        finish()
    }

    companion object{
        fun start(context: Context){
            val intent = Intent(context,AddAddressActivity::class.java)
            context.startActivity(intent)
        }
    }
}