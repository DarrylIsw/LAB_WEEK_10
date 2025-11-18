package com.example.lab_week_10.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TotalViewModel : ViewModel() {

    // Backing property (mutable internal, immutable external)
    private val _total = MutableLiveData<Int>()
    val total: LiveData<Int> get() = _total

    init {
        // Initialize LiveData safely
        _total.value = 0
    }

    // Increment the total safely on the main thread
    fun incrementTotal() {
        val current = _total.value ?: 0
        _total.value = current + 1
    }

    // TotalViewModel.kt
    fun setTotal(value: Int) {
        _total.value = value
    }

}
