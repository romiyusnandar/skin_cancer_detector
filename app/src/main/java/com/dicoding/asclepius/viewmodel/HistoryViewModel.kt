package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.local.LocalResponse
import com.dicoding.asclepius.repository.ResultRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: ResultRepository) : ViewModel() {
    private val _history = MutableLiveData<List<LocalResponse>>()
    val history: LiveData<List<LocalResponse>> get() = _history

    init {
        getAllHistory()
    }

    private fun getAllHistory() {
        viewModelScope.launch {
            repository.getAll().observeForever {
                _history.value = it
            }
        }
    }

}