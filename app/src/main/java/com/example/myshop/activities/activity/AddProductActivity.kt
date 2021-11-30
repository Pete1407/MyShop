package com.example.myshop.activities.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.example.myshop.databinding.ActivityAddProductBinding
import com.example.myshop.util.BaseCommon
import com.example.myshop.util.MyShopKey
import com.example.myshop.activities.activity.UserProfileActivity.Companion.PICK_GALLERY
import com.example.myshop.firebase.FireStoreClass
import com.example.myshop.model.Product
import com.example.myshop.util.GlideLoader
import com.google.firebase.storage.FirebaseStorage

class AddProductActivity : BaseActivity(),BaseCommon {
    private var imageData : Uri? = null
    private val binding : ActivityAddProductBinding by lazy{
        ActivityAddProductBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()
        setListener()
        setUI()
    }

    override fun setToolbar() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    override fun setUI() {}

    override fun setListener() {
        binding.addImageProduct.setOnClickListener {
            chooseImage()
        }
        binding.submit.setOnClickListener {
            if(validateAllInput()){
                showProgressDialog()
                saveProduct()
            }
        }
    }

    private fun chooseImage(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            MyShopKey.showGallery(this)
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
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

    private fun validateAllInput():Boolean{
        if(binding.titleProduct.text!!.isEmpty()){
            showSnackBar(resources.getString(R.string.error_msg_enter_name_product),true)
            return false
        }
        if(binding.priceProduct.text!!.isEmpty()){
            showSnackBar(resources.getString(R.string.error_msg_enter_price_product),true)
            return false
        }
        if(binding.quantityProduct.text!!.isEmpty()){
            showSnackBar(resources.getString(R.string.error_msg_enter_quantity),true)
            return false
        }
        if(imageData == null){
            showSnackBar(resources.getString(R.string.error_msg_enter_image_product),true)
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == MyShopKey.PICK_IMAGE_REQUEST_CODE && data!=null){
            imageData = data?.data
            GlideLoader(this).loadImage(imageData.toString(),binding.imageProduct)
        }
    }

    private fun saveProduct(){
        // get unit such as jpeg,jpg,png
        val imageExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(imageData!!))
        val refer = FirebaseStorage.getInstance().reference.child("products_${System.currentTimeMillis()}.$imageExtension")
        refer.putFile(imageData!!)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { url ->
                        saveData(url)
                    }.addOnFailureListener {
                        Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
            }
    }
    // ตอนนี้ เพิ่ม product ได้แล้วแต่ไม่ได้ใส่ loading กับจัด code
    private fun saveData(imageUrl : Uri){
        var des = ""
        val pref = getSharedPreferences(MyShopKey.MYSHOPPREF, MODE_PRIVATE)
        if(binding.descriptionProduct.text!!.isNotBlank()){
            des = binding.descriptionProduct.text.toString()
        }
        var product = Product(
            title = binding.titleProduct.text.toString(),
            price = binding.priceProduct.text.toString().toInt(),
            description = des,
            quantity = binding.quantityProduct.text.toString().toInt(),
            image = imageUrl.toString(),
            user_name = pref.getString(MyShopKey.USERNAME_LOGIN,""),
            user_id = FireStoreClass().getUserID(),
        )

        FireStoreClass().addProductToDatabase(this,product)
    }

    fun addProductSuccess(){
        hideProgressDialog()
        finish()
    }

    companion object{
        fun create(context: Context){
            val intent = Intent(context,AddProductActivity::class.java)
            context.startActivity(intent)
        }
    }
}