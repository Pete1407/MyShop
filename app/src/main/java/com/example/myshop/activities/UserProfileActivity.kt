package com.example.myshop.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.params.MandatoryStreamCombination
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.example.myshop.databinding.ActivityUserProfileBinding
import com.example.myshop.firebase.FireStoreClass
import com.example.myshop.model.User
import com.example.myshop.util.BaseCommon
import com.example.myshop.util.GlideLoader
import com.example.myshop.util.MyShopKey
import kotlin.coroutines.coroutineContext

class UserProfileActivity : BaseActivity(),BaseCommon {
    private var gender = ""
    private var mobileNum : String = ""
    private var user : User? = null
    private val binding : ActivityUserProfileBinding by lazy {
        ActivityUserProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        user = intent.getParcelableExtra(EXTRA_KEY_USER)
        setToolbar()
        setListener()
        setUI()
    }

    override fun setToolbar() {
    }

    override fun setUI() {
        user?.let {
            if(it.firstname.isNotEmpty()){
                binding.firstName.setText(it.firstname)
                binding.firstName.isEnabled = false
            }
            if(it.lastname.isNotEmpty()){
                binding.lastName.setText(it.lastname)
                binding.lastName.isEnabled = false
            }
            if(it.email.isNotEmpty()){
                binding.email.setText(it.email)
                binding.email.isEnabled = false
            }
        }
    }

    override fun setListener() {
        binding.changeImage.setOnClickListener {
            requestPermissionFromUser()
        }
        binding.male.setOnClickListener {
            gender = MyShopKey.MALE
            binding.female.isChecked = false
            binding.male.isChecked = true
        }
        binding.female.setOnClickListener {
            gender = MyShopKey.FEMALE
            binding.male.isChecked = false
            binding.female.isChecked = true
        }
        binding.save.setOnClickListener {
            if(validateInputs()){
                showProgressDialog()
                var map = HashMap<String,Any>()
                map[MyShopKey.MOBILE_FIELD] = binding.mobileNumber.text.toString().toLong()
                map[MyShopKey.GENDER_FIELD] = gender
                FireStoreClass().updateMobileAndGender(this,map)
            }
        }
    }

    private fun requestPermissionFromUser(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            MyShopKey.showGallery(this)
        }else{
            // request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PICK_GALLERY
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PICK_GALLERY){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                MyShopKey.showGallery(this)
            }else{
                Toast.makeText(this,
                    resources.getString(R.string.notify_user_permission),
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == MyShopKey.PICK_IMAGE_REQUEST_CODE){
                val imageResult = data?.data
                GlideLoader(this).loadImage(imageResult.toString(),binding.imageProfile)
            }
        }
    }

    private fun validateInputs():Boolean{
        if(binding.mobileNumber.text.toString().isEmpty()){
            showSnackBar(resources.getString(R.string.error_msg_enter_mobile_number),true)
            return false
        }

        if(gender.isEmpty()){
            showSnackBar(resources.getString(R.string.error_msg_enter_gender),true)
            return false
        }
        return true
    }

    fun updateDataSuccess(){
        hideProgressDialog()
        MainActivity.create(this)
    }

    companion object{
        const val EXTRA_KEY_USER = "key-user"
        const val PICK_GALLERY = 10

        fun create(context: Context,user: User){
            val intent = Intent(context,UserProfileActivity::class.java).apply {
                putExtra(EXTRA_KEY_USER,user)
            }
            context.startActivity(intent)
        }
    }
}