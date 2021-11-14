package com.example.myshop.activities.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myshop.activities.activity.SettingActivity
import com.example.myshop.databinding.FragmentDashboardBinding
import com.example.myshop.util.BaseCommon

class DashboardFragment : Fragment(),BaseCommon {
    private lateinit var binding : FragmentDashboardBinding

//    private val viewModel : DashboardViewModel by lazy {
//        ViewModelProvider(this).get(DashboardViewModel::class.java)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setUI()
        setListener()

    }

    override fun setToolbar() {

    }

    override fun setUI() {

    }

    override fun setListener() {
        binding.setting.setOnClickListener {
            SettingActivity.create(requireContext())
        }
    }
}