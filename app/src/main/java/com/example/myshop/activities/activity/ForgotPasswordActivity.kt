package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.myshop.R
import com.example.myshop.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {
    private var email : String = ""
    private val binding: ActivityForgotPasswordBinding by lazy {
        ActivityForgotPasswordBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()
        initListener()
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolBar)
        val actionBar = supportActionBar
        actionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white)
        }
    }

    private fun initListener() {
        binding.toolBar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.submit.setOnClickListener {
            email = binding.email.text.toString()
            if(validatorEmail(email)){
                showProgressDialog()
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            hideProgressDialog()
                            Toast.makeText(this,"Change password success",Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            hideProgressDialog()
                            showSnackBar(it.exception?.message.toString(),false)
                        }
                    }
            }else{
                showSnackBar(resources.getString(R.string.email_not_match),true)
            }
        }
    }

    private fun validatorEmail(email: String): Boolean {
        return email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    companion object {
        fun create(context: Context) {
            val intent = Intent(context, ForgotPasswordActivity::class.java)
            context.startActivity(intent)
        }
    }
}