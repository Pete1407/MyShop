package com.example.myshop.activities.activity

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.myshop.R
import com.example.myshop.databinding.ActivityLoginBinding
import com.example.myshop.activities.activity.UserProfileActivity.Companion.ACTION_EDIT_INFO
import com.example.myshop.activities.activity.UserProfileActivity.Companion.ACTION_START_FROM_LOGIN
import com.example.myshop.firebase.FireStoreClass
import com.example.myshop.model.User
import com.example.myshop.util.MyShopKey
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(), View.OnClickListener {
    private var email : String = ""
    private var password : String = ""
    private val binding : ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUI()
    }

    private fun setUI(){
        initListener()
    }

    private fun initListener(){
        binding.register.setOnClickListener(this)
        binding.logIn.setOnClickListener(this)
        binding.forgotPass.setOnClickListener(this)
    }

    private fun validateAllFields():Boolean{
        if(TextUtils.isEmpty(binding.email.text.toString())){
            email = binding.email.text.toString()
            showSnackBar(resources.getString(R.string.error_msg_enter_email),true)
            return false
        }

        if(TextUtils.isEmpty(binding.password.text.toString())){
            password = binding.password.text.toString()
            showSnackBar(resources.getString(R.string.error_msg_enter_password),true)
            return false
        }
        return true
    }

    private fun logIn(){
        if(validateAllFields()){
            showProgressDialog()
            email = binding.email.text.toString()
            password = binding.password.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        hideProgressDialog()
                        if(task.isSuccessful){
                            Toast.makeText(this,"You are log in successfully",Toast.LENGTH_SHORT).show()
                            FireStoreClass().getCurrentUserByID(this)
//                            val intent = Intent(this,MainActivity::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                            intent.putExtra("user_id",FirebaseAuth.getInstance().currentUser!!.uid)
//                            intent.putExtra("email_id",email)
//                            startActivity(intent)
//                            finish()
                        }else{
                            Toast.makeText(this,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    fun getCurrentUser(user : User){
        val sharePreference = getSharedPreferences(MyShopKey.MYSHOPPREF, MODE_PRIVATE)
        val edit = sharePreference.edit()
        edit.putString(MyShopKey.USERNAME_LOGIN, "${user.firstname} ${user.lastname}")
        edit.commit()
        if(user.profileCompleted == 0){
            UserProfileActivity.create(this@LoginActivity, user, ACTION_START_FROM_LOGIN)
            finish()
        }else{
            DashboardActivity.create(this@LoginActivity)
            finish()
        }
    }

    override fun onClick(v: View?) {
        v?.let { view ->
            when(view.id){
                R.id.forgotPass -> {
                    ForgotPasswordActivity.create(this)
                }
                R.id.logIn -> {
                    logIn()
                }
                R.id.register -> {
                    RegisterActivity.create(this)
                }
                else -> { }
            }
        }
    }

    companion object{
        fun create(context:Context){
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}