package com.example.myshop.activities.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.example.myshop.databinding.ActivityUserProfileBinding
import com.example.myshop.firebase.FireStoreClass
import com.example.myshop.model.User
import com.example.myshop.util.*
import com.google.firebase.storage.FirebaseStorage

class UserProfileActivity : BaseActivity(),BaseCommon {
    private var gender = ""
    private var imageUri : Uri? = null
    private var user : User? = null
    private var actionFromUser = ""

    private val binding : ActivityUserProfileBinding by lazy {
        ActivityUserProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        user = intent.getParcelableExtra(EXTRA_KEY_USER)
        actionFromUser = intent.getStringExtra(ACTION_EDIT_INFO).toString()
        setToolbar()
        setListener()
        setUI()
    }

    override fun setToolbar() {
        when(actionFromUser){
            MyShopKey.ACTION_EDIT_PROFILE ->{
                binding.backButton.visible()
            }
            else ->{
                binding.backButton.gone()
            }
        }
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
            if(it.mobile.toString().isNotEmpty()){
                binding.mobileNumber.setText(it.mobile.toString())
                binding.mobileNumber.isEnabled = false
            }

            if(it.gender.isNotEmpty()){
                setGenderButton(it.gender)
            }


            if(it.image.isBlank()){
                GlideLoader(this).loadImage(null,binding.imageProfile)
            }else{
                GlideLoader(this).loadImage(it.image,binding.imageProfile)
            }
        }
    }

    override fun setListener() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
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
                // get unit such as jpeg,jpg,png
                val imageExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(imageUri!!))
                val refer = FirebaseStorage.getInstance().reference.child("images_${System.currentTimeMillis()}.$imageExtension")
                refer.putFile(imageUri!!)
                    .addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata!!.reference!!.downloadUrl
                            .addOnSuccessListener { url ->
                                saveData(url)
                            }.addOnFailureListener {
                                Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                            }
                    }
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
                imageUri = data?.data
                GlideLoader(this).loadImage(imageUri.toString(),binding.imageProfile)
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
        if(imageUri == null){
            showSnackBar(resources.getString(R.string.error_msg_enter_image_profile),true)
            return false
        }
        return true
    }

    private fun saveData(url : Uri){
        var map = HashMap<String,Any>()
        map[MyShopKey.MOBILE_FIELD] = binding.mobileNumber.text.toString().toLong()
        map[MyShopKey.GENDER_FIELD] = gender
        map[MyShopKey.IMAGE_FIELD] = url.toString()
        map[MyShopKey.PROFILE_COMPLETE_FIELD] = 1
        FireStoreClass().updateMobileAndGender(this,map)
    }

    fun updateDataSuccess(){
        hideProgressDialog()
        MainActivity.create(this)
        finish()
    }

    private fun setGenderButton(gender : String){
        if(gender == MyShopKey.MALE){
            binding.male.isChecked = true
            binding.female.isChecked = false
        }else{
            binding.male.isChecked = false
            binding.female.isChecked = true
        }
    }

    companion object{
        const val EXTRA_KEY_USER = "key-user"
        const val ACTION_EDIT_INFO = "action-edit-profile"
        const val ACTION_START_FROM_LOGIN = "action-start-from-login-page"
        const val PICK_GALLERY = 10

        fun create(context: Context,user: User,action : String){
            val intent = Intent(context, UserProfileActivity::class.java).apply {
                putExtra(EXTRA_KEY_USER,user)
                putExtra(ACTION_EDIT_INFO,action)
            }
            context.startActivity(intent)
        }
    }
}