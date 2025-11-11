package com.example.lab_week_10.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TotalViewModel : ViewModel() {

    // Backing property (private MutableLiveData)
    private val _total = MutableLiveData<Int>().apply { value = 0 }

    // Public immutable LiveData
    val total: LiveData<Int> = _total

    fun incrementTotal() {
        _total.value = (_total.value ?: 0) + 1
    }
}
