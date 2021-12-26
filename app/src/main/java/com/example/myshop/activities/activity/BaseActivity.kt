package com.example.myshop.activities.activity

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.example.myshop.util.ScreenUtility
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
    lateinit var progressDialog: Dialog
    private var clickExit = false
    private var display : DisplayMetrics? = null
//    private var viewBinding: View? = null
//
//    override fun setContentView(view: View?) {
//        super.setContentView(view)
//        viewBinding = view
//    }
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        display = ScreenUtility.getDisplay(this)
        ScreenUtility.setDisplay(display!!)
    }

    fun showSnackBar(msg: String, showError: Boolean) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
        val snackBarView = snackbar.view
        if (showError) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this, R.color.snackbar_unsuccess)
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this, R.color.snackbar_success)
            )
        }
        snackbar.show()
    }

    fun showProgressDialog() {
        progressDialog = Dialog(this)
        progressDialog.setContentView(R.layout.dialog_progress)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }

    fun hideProgressDialog() {
        progressDialog.dismiss()
    }

    fun exitAppByClickTwice() {
        if (clickExit) {
            super.onBackPressed()
            return
        }
        Toast.makeText(this, resources.getString(R.string.msg_for_exit_app), Toast.LENGTH_SHORT).show()
        clickExit = true
        Handler().postDelayed({
            clickExit = false
        }, 3000)
    }
}