package com.example.myshop.activities.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    val textLiveData : LiveData<String>
        get() = textMutable

    private var textMutable : MutableLiveData<String> = MutableLiveData()

    fun getText(){
        textMutable.postValue("Hello, this is DashBoard jaaaaaa")
    }

}