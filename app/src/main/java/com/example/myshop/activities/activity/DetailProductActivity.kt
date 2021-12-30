package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.example.myshop.databinding.ActivityDetailProductBinding
import com.example.myshop.firebase.FireStoreClass
import com.example.myshop.model.Cart
import com.example.myshop.model.Product
import com.example.myshop.util.BaseCommon
import com.example.myshop.util.GlideLoader
import com.example.myshop.util.gone
import com.example.myshop.util.visible
import com.google.firebase.auth.FirebaseAuth

class DetailProductActivity : BaseActivity(),BaseCommon {
    private var outOfStock : Boolean = false
    private var idProduct : String? = null
    private var item : Product? = null
    private var isExist : Boolean = false
    private val binding : ActivityDetailProductBinding by lazy{
        ActivityDetailProductBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        idProduct = intent.getStringExtra(PARAM_ID_PRODUCT).toString()
        setToolbar()
        setListener()
    }

    override fun onResume() {
        super.onResume()
        showProgressDialog()
        FireStoreClass().getDetailProduct(idProduct.toString(),this)
        FireStoreClass().checkExistProduct(idProduct.toString(),this)
    }

    override fun setToolbar() {
    }

    override fun setUI() {
        item?.let {
            binding.title.text = it.title
            binding.nameProduct.text = it.title
            binding.descriptionProduct.text = it.description
            if(it.quantity!! < 1){
                outOfStock = true
                binding.numberOfProduct.setTextColor(ContextCompat.getColor(this,R.color.snackbar_unsuccess))
                binding.numberOfProduct.text = resources.getString(R.string.out_of_stock)

            }else{
                outOfStock = false
                binding.numberOfProduct.text = it.quantity.toString()
            }

            GlideLoader(this).loadImage(it.image,binding.image)
        }
    }

    override fun setListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.addToCart.setOnClickListener {
            binding.addToCart.gone()
            binding.goToCart.visible()
            item?.let {
                binding.numberOfProduct.text = (it.quantity!!.toInt() - 1).toString()
                var cart = Cart(
                    FirebaseAuth.getInstance().uid.toString(),
                    it.id.toString(),
                    it.title.toString(),
                    it.price.toString(),
                    it.image.toString(),
                    "1",
                    stock_quantity = "1"
                )
                FireStoreClass().addProductToCart(cart,this)
            }

        }
        binding.goToCart.setOnClickListener {
            CartListActivity.create(this)
        }
    }

    fun getDetailProduct(product:Product){
        item = product
        outOfStock = item!!.quantity == 0
        hideProgressDialog()
        setUI()
    }

    fun addToCartSuccess(){
        val text = resources.getString(R.string.add_to_cart)
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }

    fun addToCartFail(errorText:String){
        Toast.makeText(this,errorText,Toast.LENGTH_SHORT).show()
    }

    fun checkExistInCart(isHave : Boolean){
        isExist = isHave
        Log.i("result","isExist --> $isExist")
        if(isExist){
            binding.addToCart.gone()
            binding.goToCart.visible()
        }else{
            binding.addToCart.visible()
            binding.goToCart.gone()
            if(outOfStock){
                binding.addToCart.gone()
            }else{
                binding.addToCart.visible()
            }
        }
    }

    companion object{
        const val PARAM_ID_PRODUCT = "product-id"

        fun start(context:Context,id:String){
            val intent = Intent(context,DetailProductActivity::class.java)
            intent.putExtra(PARAM_ID_PRODUCT,id)
            context.startActivity(intent)
        }
    }
}