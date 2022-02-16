package com.example.myshop.firebase

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.myshop.activities.activity.*
import com.example.myshop.activities.fragment.DashboardFragment
import com.example.myshop.activities.fragment.OrderFragment
import com.example.myshop.activities.fragment.ProductFragment
import com.example.myshop.model.*
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

    // Main function
    fun addProductToCart(cart: Cart?, activity: Activity) {
        cart?.let { cartItem ->
            val item = fireStore.collection(MyShopKey.CARTS).document()
            val cartID = item.id
            cartItem.id = cartID
            item.set(cartItem, SetOptions.merge())
                .addOnSuccessListener {
                    deductStockInProduct(cartItem.product_id, 1, activity)
                }
                .addOnFailureListener {}
        }
    }

    // Second function
    private fun deductStockInProduct(id: String, numberOfOrder: Int, activity: Activity) {
        fireStore.collection(MyShopKey.PRODUCTS)
            .document(id)
            .get()
            .addOnSuccessListener { result ->
                val item = result.toObject(Product::class.java)
                if (item!!.quantity!!.toInt() > numberOfOrder) {
                    var avaliableStock = item!!.quantity!!.toInt() - numberOfOrder
                    updateStock(activity, avaliableStock, id)
                } else {
                    Log.e("error", "Quantity of this product is not enough!!!")
                }

            }
            .addOnFailureListener {

            }
    }

    private fun updateStock(
        activity: Activity,
        avaliableStock: Int,
        idProd: String,
        action: String = ""
    ) {
        fireStore.collection(MyShopKey.PRODUCTS)
            .document(idProd)
            .update(MyShopKey.QUANTITY_PRODUCT, avaliableStock)
            .addOnSuccessListener {
                when (activity) {
                    is DetailProductActivity -> {
                        activity.addToCartSuccess()
                    }
                    is CartListActivity -> {
                        when (action) {
                            CartListActivity.ACTION_INCREASE -> {
                                updateOrderProduct(idProd, action)
                                activity.increaseStock(avaliableStock)
                            }
                            CartListActivity.ACTION_DECREASE -> {
                                updateOrderProduct(idProd, action)
                                activity.decreaseStock(avaliableStock)
                            }
                            else -> {
                                activity.deleteProductSuccess()
                            }
                        }
                    }
                }
            }
            .addOnFailureListener {
                if (activity is DetailProductActivity) {
                    activity.addToCartFail(it.stackTrace.toString())
                }
            }


    }

    private fun updateOrderProduct(idProd: String, action: String) {
        fireStore.collection(MyShopKey.CARTS)
            .whereEqualTo(MyShopKey.PRODUCT_ID, idProd)
            .get()
            .addOnSuccessListener {
                val result = it.documents[0].toObject(Cart::class.java)
                updateCartQuantity(result!!, action)
            }
            .addOnFailureListener {

            }
    }

    private fun updateCartQuantity(cart: Cart, action: String) {
        var a = cart.stock_quantity.toInt()
        if (action == CartListActivity.ACTION_INCREASE) {
            a += 1 // a = 3+1 --> 4
            cart.stock_quantity = a.toString()
        } else if (action == CartListActivity.ACTION_DECREASE) {
            a -= 1 // a = 3-1 --> 2
            cart.stock_quantity = a.toString()
        }

        fireStore.collection(MyShopKey.CARTS)
            .document(cart.id)
            .update(MyShopKey.STOCK_QUANTITY, cart.stock_quantity)
            .addOnSuccessListener {
                Log.i("result", "update stock quantity success")
            }
            .addOnFailureListener {

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

    fun getProductList(activity: Activity) {
        fireStore.collection(MyShopKey.PRODUCTS)
            .get()
            .addOnSuccessListener { result ->
                var prodList = ArrayList<Product>()
                for (i in result.documents) {
                    val prodObj = i.toObject(Product::class.java)!!
                    prodList.add(prodObj)
                }
                when (activity) {
                    is CartListActivity -> {
                        activity.getProductListSuccess(prodList)
                    }
                    is CheckOutActivity -> {
                        activity.getProductListSuccess(prodList)
                    }
                }


            }
            .addOnFailureListener {

            }
    }

    fun getCartList(activity: Activity) {
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
                when (activity) {
                    is CartListActivity -> {
                        activity.getCarts(list)
                    }
                    is CheckOutActivity -> {
                        activity.getCarts(list)
                    }
                }
            }.addOnFailureListener {

            }
    }

    fun deleteProductInCart(cartItem: Cart, activity: Activity, numberOrder: Int) {
        fireStore.collection(MyShopKey.CARTS)
            .document(cartItem.id)
            .delete()
            .addOnSuccessListener {
                checkStock(cartItem, activity, numberOrder, CartListActivity.ACTION_RETURN_ALL)
            }
            .addOnFailureListener {

            }
    }

    fun checkStock(cart: Cart, activity: Activity, numberOfOrder: Int, action: String) {
        fireStore.collection(MyShopKey.PRODUCTS)
            .document(cart.product_id)
            .get()
            .addOnSuccessListener { result ->
                val item = result.toObject(Product::class.java)
                var stock = item!!.quantity!!.toInt()
                // ถ้าจะเพิ่ม order แล้วของหมดโชว์แจ้งเตือน
                // ถ้าจะลด order ก็ให้ไปเพิ่มใน quantity ของ product
                if (action == CartListActivity.ACTION_INCREASE) {
                    if (stock > numberOfOrder) {
                        var diff = stock - 1
                        updateStock(activity, diff, item.id.toString(), action)
                    } else {

                        updateStock(activity, 0, item.id.toString(), action)
                        Log.i("error", "STOCK == 0 AND STOCK < NUMBER OF ORDER")
                    }
                } else if (action == CartListActivity.ACTION_DECREASE) {
                    stock += 1
                    updateStock(activity, stock, item.id.toString(), action)
                } else {
                    stock += numberOfOrder
                    updateStock(activity, stock, item.id.toString(), action)

                }

            }
            .addOnFailureListener {

            }
    }

    fun saveAddress(data: AddressModel, activity: Activity) {
        val firebaseObj = fireStore.collection(MyShopKey.ADDRESSES).document()
        val idAddress = firebaseObj.id
        data.id = idAddress
        firebaseObj.set(data, SetOptions.merge())
            .addOnSuccessListener {
                when (activity) {
                    is AddAddressActivity -> {
                        activity.saveAddressSuccess()
                    }
                }
            }
            .addOnFailureListener {

            }
    }

    fun getAddresses(activity: Activity) {
        fireStore.collection(MyShopKey.ADDRESSES)
            .get()
            .addOnSuccessListener { result ->
                when (activity) {
                    is AddressListActivity -> {
                        var list = ArrayList<AddressModel>()
                        for (i in result.documents) {
                            val item = i.toObject(AddressModel::class.java)
                            list.add(item!!)
                        }
                        activity.getAddressList(list)
                    }
                }
            }
            .addOnFailureListener {

            }
    }

    fun deleteAnAddress(addressId: String, activity: Activity) {
        fireStore.collection(MyShopKey.ADDRESSES)
            .document(addressId)
            .delete()
            .addOnSuccessListener {
                when (activity) {
                    is AddressListActivity -> {
                        activity.deleteAddressSuccess()
                    }
                }
            }
            .addOnFailureListener {

            }
    }

    fun getSpecifyAddress(id: String, activity: Activity) {
        fireStore.collection(MyShopKey.ADDRESSES)
            .document(id)
            .get()
            .addOnSuccessListener { result ->
                val item = result.toObject(AddressModel::class.java)
                when (activity) {
                    is AddAddressActivity -> {
                        activity.getExistAddress(item!!)
                    }
                }
            }
            .addOnFailureListener {

            }
    }

    fun updateAddressData(addressId: String, map: HashMap<String, Any>, activity: Activity) {
        fireStore.collection(MyShopKey.ADDRESSES)
            .document(addressId)
            .update(map)
            .addOnSuccessListener {
                when (activity) {
                    is AddAddressActivity -> {
                        activity.updateAddressSuccess()
                    }
                }
            }
            .addOnFailureListener {

            }
    }

    fun addOrderToDB(order: Order, activity: Activity) {
        val item = fireStore.collection(MyShopKey.ORDERS).document()
        order.id = item.id
        item.set(order, SetOptions.merge())
            .addOnSuccessListener {
                when (activity) {
                    is CheckOutActivity -> {
                        activity.addOrderSuccess()
                    }
                }
            }
            .addOnFailureListener {

            }
    }

    fun checkQuantity(cartItem: Cart, orderNumber: Int, activity: CartListActivity) {
        fireStore.collection(MyShopKey.PRODUCTS)
            .document(cartItem.product_id)
            .get()
            .addOnSuccessListener {
                val output = it.toObject(Product::class.java)
                if (orderNumber > output?.quantity!!) {
                    activity.showMessageOutOfStock(output?.quantity)
                } else {
                    addOrderInCartByProductId(cartItem, output.id.toString(), activity)
                    //activity.showMessageSuccess()
                }
            }
            .addOnFailureListener {
                Log.e("error", "error in checkQuantity func")
            }
    }

    // 1. เช็คว่าสินค้ามีพอกับใน stock ที่เหลือมั้ย
    // 2. ถ้ามีเหลือพอก็สั่งแก้ใน cart เพิ่มให้
    // 3. ถ้าไม่เหลือก็ไม่แก้
    // run batch ให้แก้ตรง cart กับ stock ของ product โดยใช้ id ของ product นั้นๆ
    private fun addOrderInCartByProductId(
        cartItem: Cart,
        idProd: String,
        activity: CartListActivity
    ) {
        var order = cartItem.cart_quantity.toInt() + 1
        val thisProd = fireStore.collection(MyShopKey.CARTS).document(cartItem.id)
            .update(MyShopKey.CART_QUANTITY, order.toString())
        thisProd.addOnSuccessListener {
            activity.showMessageSuccess()
        }.addOnFailureListener { error ->
            Log.e(MyShopKey.ERROR_TAG, error.message.toString())
        }
    }

    fun removeItemInCart(cartId: String, activity: Activity) {
        fireStore.collection(MyShopKey.CARTS)
            .document(cartId)
            .delete()
            .addOnSuccessListener {
                when (activity) {
                    is CartListActivity -> {
                        activity.updateCartSuccess()
                    }
                }
            }
            .addOnFailureListener {
                when (activity) {
                    is CartListActivity -> {
                        activity.hideProgressDialog()
                    }
                }
            }
    }

    // 1. กดปุ่ม place order ไปเพื่อ check out
    // 2. ลบข้อมูล item in cart ที่ทำการ check out ออกไปให้หมด
    // 3. หัก quantity product ออกตามจำนวน
    // 4. ไปหน้า dashboard

    fun checkOutCartAndProduct(cartList: ArrayList<Cart>, activity: CheckOutActivity) {
        val writeBatch = fireStore.batch()
        var map = HashMap<String, Any>()
        for (itemCart in cartList) {
            map[MyShopKey.QUANTITY_PRODUCT] =
                itemCart.stock_quantity.toInt() - itemCart.cart_quantity.toInt()
            val productRef =
                fireStore.collection(MyShopKey.PRODUCTS).document(itemCart.product_id)
            writeBatch.update(productRef, map)
            val cartRef = fireStore.collection(MyShopKey.CARTS).document(itemCart.id)
            writeBatch.delete(cartRef)
        }
        writeBatch.commit()
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(MyShopKey.ERROR_TAG, "error while checkout cart and product")
            }
    }

    fun getOrder(fragment: OrderFragment) {
        fireStore.collection(MyShopKey.ORDERS)
            .whereEqualTo(MyShopKey.USER_ID, getUserID())
            .get()
            .addOnSuccessListener {
                val result = it.documents
                var orders = ArrayList<Order>()
                for (i in result) {
                    val item = i.toObject(Order::class.java)
                    if (item != null) {
                        item.id = i.id
                    }
                    orders.add(item!!)
                }
                fragment.getMyOrderSuccess(orders)
            }
            .addOnFailureListener {
                fragment.hideProgressDialog()
            }
    }

}