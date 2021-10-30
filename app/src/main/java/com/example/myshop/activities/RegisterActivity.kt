package com.example.myshop.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.myshop.R
import com.example.myshop.databinding.ActivityRegisterBinding
import com.example.myshop.firebase.FireStoreClass
import com.example.myshop.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity(){

    private var firstName : String = ""
    private var lastName : String = ""
    private var email : String = ""
    private var password : String = ""
    private var confirmPassword : String = ""

    private val binding : ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()
        initUI()
    }

    private fun setToolbar(){
        setSupportActionBar(binding.toolBar)
        val actionBar = supportActionBar
        actionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }
        binding.toolBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun validateAllFields():Boolean{
        if(TextUtils.isEmpty(binding.firstName.text.toString())){
            showSnackBar(resources.getString(R.string.error_msg_enter_first_name),true)
            return false
        }

        if(TextUtils.isEmpty(binding.lastName.text.toString())){
            showSnackBar(resources.getString(R.string.error_msg_enter_last_name),true)
            return false
        }

        if(TextUtils.isEmpty(binding.emailID.text.toString())){
            showSnackBar(resources.getString(R.string.error_msg_enter_email),true)
            return false
        }

        if(TextUtils.isEmpty(binding.password.text.toString())){
            showSnackBar(resources.getString(R.string.error_msg_enter_password),true)
            return false
        }

        if(TextUtils.isEmpty(binding.confirmPassword.text.toString())){
            showSnackBar(resources.getString(R.string.error_msg_enter_confirm_password),true)
            return false
        }

        if(binding.password.text.toString() != binding.confirmPassword.text.toString()){
            showSnackBar(resources.getString(R.string.error_msg_password_and_confirmPass),true)
            return false
        }

        if(!binding.checkBox.isChecked){
            showSnackBar(resources.getString(R.string.error_msg_terms_and_condition),true)
            return false
        }

        return true
    }

    private fun initUI(){
        binding.logIn.setOnClickListener {
            onBackPressed()
        }

        binding.register.setOnClickListener {
            if(validateAllFields()){
                showProgressDialog()
                val email = binding.emailID.text.toString()
                val password = binding.password.text.toString()
                val confirmPass = binding.confirmPassword.text.toString()
                if(email.isNotEmpty() && password.isNotEmpty() && confirmPass.isNotEmpty() && password== confirmPass){
                    // เรียกใช้งาน instance ของ authentication service โดยให้สร้าง user email and password
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            hideProgressDialog()
                            if(task.isSuccessful){
                                // FirebaseUser เป็น class เก็บข้อมูลที่ถูกสร้างขึ้นของ user
                                val firebaseUser : FirebaseUser = task.result!!.user!!
                                val firstname = binding.firstName.text.toString()
                                val lastname = binding.lastName.text.toString()
                                val newUser = User(
                                    id = firebaseUser.uid,
                                    firstname = firstname,
                                    lastname = lastname,
                                    email = email
                                )
                                // ส่วนเอาข้อมูลของ User คนใหม่ไปเก็บใน firestore
                                FireStoreClass().registerUser(this@RegisterActivity,newUser)
                                //val intent = Intent(this,LoginActivity::class.java)
                                // destroy bunch of activity in background
                                // example --> login activity to register activity --> press back to login activity
                                // มันจะกำจัดตรงส่วนนี้แหละให้หายไปเลย
//                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                intent.putExtra("user_id",firebaseUser.uid)
//                                intent.putExtra("email_id",email)
                                //startActivity(intent)
                                //FirebaseAuth.getInstance().signOut()
                                // กำจัดหน้าปัจจุบันคือ หน้า register
                                finish()
                            }else{
                                Toast.makeText(this,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                            }
                        }
                }

            }
        }
    }

    fun registerNewUserSuccessfully(){
        hideProgressDialog()
        Toast.makeText(this,resources.getString(R.string.register_success),Toast.LENGTH_SHORT).show()
    }

    companion object{
        fun create(context:Context){
            val intent = Intent(context,RegisterActivity::class.java)
            context.startActivity(intent)
        }

    }
}