package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myshop.R
import com.example.myshop.activities.adapter.ProductAdapter
import com.example.myshop.databinding.ActivityCartListBinding
import com.example.myshop.firebase.FireStoreClass
import com.example.myshop.model.Cart
import com.example.myshop.model.ObjectType
import com.example.myshop.util.BaseCommon
import com.example.myshop.util.gone
import com.example.myshop.util.visible
import com.example.myshop.activities.fragment.ProductFragment.Companion.PART_CART_ITEM

class CartListActivity : BaseActivity(),BaseCommon {
    private var adapter : ProductAdapter? = null
    private var stockList = ArrayList<Int>()

    private val binding : ActivityCartListBinding by lazy{
        ActivityCartListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()
        setListener()
        setUI()

    }

    override fun onResume() {
        super.onResume()
        FireStoreClass().getProductInCart(this)
    }

    override fun setToolbar() {

    }

    override fun setUI() {
    }

    override fun setListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.checkOut.setOnClickListener {
            AddressListActivity.start(this)
        }
    }

    fun getCarts(result : ArrayList<Cart>){
        if(result.isEmpty()){
            binding.notFound.visible()
            binding.recyclerview.gone()
            computeTotalPrice(arrayListOf())
        }else{
            binding.notFound.gone()
            binding.recyclerview.visible()
            setAdapter(result)
            computeTotalPrice(result)

        }
    }

    private fun computeTotalPrice(items : ArrayList<Cart>){
        var totalPrice = 0
        val shippingPrice = 10
        if(items.isEmpty()){
            totalPrice = 0
            binding.constraintLayout.gone()
        }else{
            binding.constraintLayout.visible()
            items.forEach {
                totalPrice += it.price.toInt()
            }
            Log.i("result","total price --> $totalPrice")
            binding.subTotalValue.text = "$$totalPrice"
            binding.shippingChargeValue.text = "$$shippingPrice"
            binding.totalAmountValue.text = "$${totalPrice+shippingPrice}"
        }
    }

    private fun setAdapter(result : ArrayList<Cart>){
        var data = ArrayList<ObjectType>()
        result.forEach {
            val obj = ObjectType(PART_CART_ITEM,it)
            data.add(obj)
        }
        if(adapter == null){
            adapter = ProductAdapter(data)

            adapter!!.setEventDecreaseQuantityListener { number, item ->
                Log.i("result","name --> ${item.title}")
                Log.i("result","number --> $number")
                FireStoreClass().checkQuantity(item.product_id,number,this)
            }
            adapter!!.setEventIncreaseQuantityListener { number, item ->
                Log.i("result","name --> ${item.title}")
                Log.i("result","number --> $number")
                //FireStoreClass().checkQuantity(item.product_id,number,this)
            }
            adapter!!.setEventCheckStockProductListener { number, item ->
                FireStoreClass().checkQuantity(item.product_id,number,this)
            }
            adapter!!.setEventDeleteCartListener { result,numberOrder ->
                showProgressDialog()
                Log.i("result","number of order:: $numberOrder")
                FireStoreClass().deleteProductInCart(result,this,numberOrder)
            }
        }else{
            adapter?.refreshDataInCart(result)
        }

        binding.recyclerview.adapter = adapter
    }

    fun deleteProductSuccess(){
        hideProgressDialog()
        FireStoreClass().getProductInCart(this)
    }

    fun increaseStock(leftStock : Int){
        hideProgressDialog()
        Toast.makeText(this,"avaliable product --> $leftStock",Toast.LENGTH_LONG).show()
    }

    fun decreaseStock(leftStock : Int){
        hideProgressDialog()
        Toast.makeText(this,"avaliable product --> $leftStock",Toast.LENGTH_LONG).show()
    }

    fun showMessageOutOfStock(quantityOfProduct : Int){
        Log.i("result","Quantity of Product in CartList :: $quantityOfProduct")
        adapter?.updateStock(quantityOfProduct)
        showSnackBar(getString(R.string.msg_out_of_stock),true)
    }

    fun showMessageSuccess(){
        Log.i("result","check stock success")
    }

    companion object{
        const val ACTION_INCREASE = "increase-order"
        const val ACTION_DECREASE = "decrease-order"
        const val ACTION_RETURN_ALL = "return-order"

        fun create(context:Context){
            val intent = Intent(context,CartListActivity::class.java)
            context.startActivity(intent)
        }
    }
}