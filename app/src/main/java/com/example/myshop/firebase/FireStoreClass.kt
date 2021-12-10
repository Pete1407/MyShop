package com.example.myshop.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myshop.activities.activity.*
import com.example.myshop.activities.fragment.DashboardFragment
import com.example.myshop.activities.fragment.ProductFragment
import com.example.myshop.model.Cart
import com.example.myshop.model.Product
import com.example.myshop.model.User
import com.example.myshop.util.MyShopKey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {

    private val fireStore = FirebaseFirestore.getInstance()

    // register user into Firebase authentication
    fun registerUser(activity: RegisterActivity, user: User) {
        // กำหนด collection
        fireStore.collection(MyShopKey.USERS)
            // กำหนด unique id
            .document(user.id)
            // set คือการเพิ่มลง database ถ้าเป็น get จะใช้ get เลย
            // SetOptions.merge ถ้ามี item นี้อยู่แล้วจะเข้าไปแทนที่ค่า ตาม id ถ้าไม่มีก็เพิ่มเข้าไป
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                activity.registerNewUserSuccessfully()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
            }

    }

    fun getUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

    // get data from userID
    fun getCurrentUserByID(activity: Activity) {
        var output: User? = null
        fireStore.collection(MyShopKey.USERS)
            .document(getUserID())
            // สำหรับการดึงข้อมูลตาม id ของ user
            .get()
            .addOnSuccessListener { result ->
                output = result.toObject(User::class.java)
                output?.let {
                    when (activity) {
                        is LoginActivity -> {
                            activity.getCurrentUser(output!!)
                        }
                        is SettingActivity -> {
                            activity.apply {
                                hideProgressDialog()
                                getDataUser(it)
                            }
                        }
                        else -> {}
                    }
                } ?: kotlin.run {
                    Log.d("tag_fireStore", "<--  output is null  -->")
                }
            }
            .addOnFailureListener {
                when (activity) {
                    is LoginActivity -> {
                        activity.showProgressDialog()
                    }
                    is SettingActivity -> {
                        activity.showProgressDialog()
                    }
                }
            }
    }

    // update data for some field
    // by use hashmap
    fun updateMobileAndGender(activity: Activity, map: HashMap<String, Any>) {
        fireStore.collection(MyShopKey.USERS)
            .document(getUserID())
            // สำหรับเปลี่ยนแปลงข้อมูลบาง field
            .update(map)
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> {
                        activity.updateDataSuccess()
                    }
                }
            }
            .addOnFailureListener {
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                        Log.e("error", "error while update data")
                    }
                }
            }
    }

    fun addProductToDatabase(activity: Activity, newProduct: Product) {
        val addProcess = fireStore.collection(MyShopKey.PRODUCTS)
            .document()
        val id = addProcess.id
        newProduct.id = id
        addProcess.set(newProduct, SetOptions.merge())
            .addOnSuccessListener {
                when (activity) {
                    is AddProductActivity -> {
                        activity.addProductSuccess()
                    }
                }
            }
            .addOnFailureListener {

            }
    }

    fun getProductsFromDatabase(fragment: Fragment) {
        fireStore.collection(MyShopKey.PRODUCTS)
            .whereEqualTo(MyShopKey.USER_ID, getUserID())
            .get()
            .addOnSuccessListener { result ->
                when (fragment) {
                    is ProductFragment -> {
                        var list = ArrayList<Product>()
                        for (i in result.documents) {
                            val aProduct = i.toObject(Product::class.java)
                            list.add(aProduct!!)
                        }
                        fragment.getResultProductSuccess(list)
                    }
                }

            }
            .addOnFailureListener {

            }
    }

    fun getProductInDashBoard(fragment: Fragment) {
        fireStore.collection(MyShopKey.PRODUCTS)
            .get()
            .addOnSuccessListener { result ->
                when (fragment) {
                    is DashboardFragment -> {
                        var list = ArrayList<Product>()
                        val output = result.documents
                        for (i in output) {
                            val obj = i.toObject(Product::class.java)
                            list.add(obj!!)
                        }
                        fragment.getDataSuccess(list)
                    }
                }
            }
            .addOnFailureListener {

            }
    }

    fun deleteProductByUser(fragment: Fragment, productId: String) {
        fireStore.collection(MyShopKey.PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                when (fragment) {
                    is ProductFragment -> {
                        fragment.deleteProductSuccess()
                    }
                }
            }
            .addOnFailureListener {
                Log.e("error", "deleteProductByUser: ${it.stackTrace}")
            }
    }

    fun getDetailProduct(idProduct: String, activity: Activity) {
        fireStore.collection(MyShopKey.PRODUCTS)
            .document(idProduct)
            .get()
            .addOnSuccessListener { output ->
                if (activity is DetailProductActivity) {
                    val result = output.toObject(Product::class.java)
                    result?.let {
                        activity.getDetailProduct(it)
                    }

                }
            }
            .addOnFailureListener {

            }
    }

    fun addProductToCart(cart: Cart?, activity: Activity) {
        cart?.let { cartItem ->
            val item = fireStore.collection(MyShopKey.CARTS).document()
            val cartID = item.id
            cartItem.id = cartID
            item.set(cartItem, SetOptions.merge())
                .addOnSuccessListener {
                        deductStockInProduct(cartItem.product_id,1,activity)
                }
                .addOnFailureListener {}
        }
    }

    private fun deductStockInProduct(id:String,numberOfOrder: Int,activity: Activity){

        fireStore.collection(MyShopKey.PRODUCTS)
            .document(id)
            .get()
            .addOnSuccessListener { result ->
                val item = result.toObject(Product::class.java)
                if(item!!.quantity!!.toInt() > numberOfOrder){
                    var avaliableStock = item!!.quantity!!.toInt() - numberOfOrder
                    updateStock(activity,avaliableStock,id)
                }else{
                    Log.e("error","Quantity of this product is not enough!!!")
                }

            }
            .addOnFailureListener {

            }
    }

    private fun updateStock(activity: Activity,avaliableStock:Int,idProd:String){
        fireStore.collection(MyShopKey.PRODUCTS)
            .document(idProd)
            .update(MyShopKey.QUANTITY_PRODUCT,avaliableStock)
            .addOnSuccessListener {
                if(activity is DetailProductActivity){
                    activity.addToCartSuccess()
                }
            }
            .addOnFailureListener {
                if(activity is DetailProductActivity){
                    activity.addToCartFail(it.stackTrace.toString())
                }
            }
    }

    fun checkExistProduct(idProduct: String, activity: Activity) {
        fireStore.collection(MyShopKey.CARTS)
            .whereEqualTo(MyShopKey.PRODUCT_ID, idProduct)
            .whereEqualTo(MyShopKey.USER_ID, getUserID())
            .get()
            .addOnSuccessListener { result ->
                if (activity is DetailProductActivity) {
                    if (result.documents.isNotEmpty()) {
                        activity.checkExistInCart(true)
                    } else {
                        activity.checkExistInCart(false)
                    }
                }
            }
            .addOnFailureListener { error ->

            }
    }

    fun getProductInCart(activty: Activity) {
        FirebaseFirestore.getInstance().collection(MyShopKey.CARTS)
            .whereEqualTo(MyShopKey.USER_ID, getUserID())
            .get()
            .addOnSuccessListener { result ->
                var count = 0
                var list = ArrayList<Cart>()
                for (item in result) {
                    val obj = item.toObject(Cart::class.java)
                    list.add(obj)
                    Log.i("item", "item $count ${obj.title}")
                    count++
                }
                if (activty is CartListActivity) {
                    activty.getCarts(list)
                }
            }.addOnFailureListener {

            }
    }

    fun deleteProductInCart(cartId: String, activity: Activity) {
        fireStore.collection(MyShopKey.CARTS)
            .document(cartId)
            .delete()
            .addOnSuccessListener {
                when (activity) {
                    is CartListActivity -> {
                        activity.deleteProductSuccess()
                    }
                }
            }
            .addOnFailureListener {

            }
    }

    fun checkStock(cart: Cart, activity: Activity, numberOfOrder: Int,action:String) {
       fireStore.collection(MyShopKey.PRODUCTS)
            .document(cart.product_id)
            .get()
            .addOnSuccessListener { result ->
                val item = result.toObject(Product::class.java)
                var stock = item!!.quantity!!.toInt()
                if(action == CartListActivity.ACTION_INCREASE){
                    if (stock != 0 && stock > numberOfOrder) {
                        var diff = stock - numberOfOrder
                        //Log.i("result", "old stock: $stock   now: $diff")
                        if (activity is CartListActivity) {
                            activity.increaseStock(diff)
                        }
                    }else{
                        Log.i("error","STOCK == 0 AND STOCK < NUMBER OF ORDER")
                    }
                 // CONDITION OF ACTION_DECREASE
                }else{
                    var leftStock = stock + numberOfOrder
                    Log.i("result","leftstock --> $leftStock")
                    if(leftStock > stock){

                    }else{
                        if(activity is CartListActivity){
                            activity.decreaseStock(leftStock)
                        }
                    }

                }

            }
            .addOnFailureListener {

            }
    }
}