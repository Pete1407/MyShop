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
import com.example.myshop.util.MyShopKey
import com.google.firebase.auth.FirebaseAuth

class AddAddressActivity : BaseActivity(), BaseCommon{

    private var address : AddressModel? = null
    private var idAddress : String? = null
    private var place:String = ""
    private var action : String? = null
    private val binding: ActivityAddAddressBinding by lazy {
        ActivityAddAddressBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        action = intent.getStringExtra(ACTION_PARAM)
        idAddress = intent.getStringExtra(ID_PARAM)
        setToolbar()
        setUI()
        setListener()
    }

    override fun onResume() {
        super.onResume()
        idAddress?.let {
            showProgressDialog()
            FireStoreClass().getSpecifyAddress(it,this)
        }
    }

    override fun setToolbar() {
        place = resources.getString(R.string.home)
        if(action == AddressListActivity.ACTION_ADD){
            binding.titleToolbar.text = getString(R.string.add_address_title)
        }else{
            binding.titleToolbar.text = getString(R.string.edit_address_title)
        }
    }

    override fun setUI() {
        address?.let {
            binding.fullname.setText(it.name)
            binding.phoneNumber.setText(it.mobileNumber)
            binding.address.setText(it.address)
            binding.zipcode.setText(it.zipCode)
            binding.note.setText(it.additionalNote)
            setButton(it.type)
        }
    }

    override fun setListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.submit.setOnClickListener {
            if(detectInput()){
                if(action == AddressListActivity.ACTION_ADD){
                    showProgressDialog()
                    saveAddress()
                }else{
                    showProgressDialog()
                    updateAddress()
                }
            }
        }
        binding.home.setOnClickListener {
            place = getString(R.string.home)
            setButton(place)
        }
        binding.office.setOnClickListener {
            place = getString(R.string.office)
            setButton(place)
        }
        binding.other.setOnClickListener {
            place = getString(R.string.other)
            setButton(place)
        }
    }

    private fun setButton(name:String){
        when(name){
            getString(R.string.home) ->{
                binding.home.isChecked = true
                binding.office.isChecked = false
                binding.other.isChecked = false
            }
            getString(R.string.office) ->{
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

    private fun saveAddress(){
        val fullName = binding.fullname.text.toString()
        val phoneNumber = binding.phoneNumber.text.toString()
        val addressValue = binding.address.text.toString()
        val zipCode = binding.zipcode.text.toString()
        val note = binding.note.text.toString()
        val placeValue = place
        val address = AddressModel(
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

    fun saveAddressSuccess(){
        hideProgressDialog()
        finish()
    }

    fun getExistAddress(result : AddressModel){
        hideProgressDialog()
        address = result
        setUI()
    }

    private fun updateAddress(){
        var map = HashMap<String,Any>()
        val fullName = binding.fullname.text.toString()
        val phoneNumber = binding.phoneNumber.text.toString()
        val addressValue = binding.address.text.toString()
        val zipCode = binding.zipcode.text.toString()
        val note = binding.note.text.toString()
        val placeValue = place
        map[MyShopKey.ADDRESS_NAME] = fullName
        map[MyShopKey.MOBILE_NUMBER] = phoneNumber
        map[MyShopKey.ADDRESS] = addressValue
        map[MyShopKey.ZIPCODE] = zipCode
        map[MyShopKey.NOTE] = note
        map[MyShopKey.TYPE] = placeValue
        map[MyShopKey.OTHER_DETAIL] = note
        FireStoreClass().updateAddressData(idAddress!!,map,this)
    }

    fun updateAddressSuccess(){
        hideProgressDialog()
        finish()
    }

    companion object{
        const val ACTION_PARAM = "action-param"
        const val ID_PARAM = "id-param"
        fun start(context: Context,action:String){
            val intent = Intent(context,AddAddressActivity::class.java).apply {
                putExtra(ACTION_PARAM,action)
            }
            context.startActivity(intent)
        }

        fun start(context: Context,action:String,idAddress:String){
            val intent = Intent(context,AddAddressActivity::class.java).apply {
                putExtra(ACTION_PARAM,action)
                putExtra(ID_PARAM,idAddress)
            }
            context.startActivity(intent)
        }
    }
}