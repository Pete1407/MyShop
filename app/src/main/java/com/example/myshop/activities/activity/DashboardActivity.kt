package com.example.myshop.activities.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myshop.R
import com.example.myshop.databinding.ActivityDashboardBinding

class DashboardActivity : BaseActivity() {
    private lateinit var binding : ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        if(navHostFragment!=null){
            val navController = navHostFragment.navController
            NavigationUI.setupWithNavController(binding.bottomMenu,navController)
        }

    }

    override fun onBackPressed() {
        exitAppByClickTwice()
    }

    companion object{
        fun create(context: Context){
            val intent = Intent(context, DashboardActivity::class.java)
            context.startActivity(intent)
        }
    }
}