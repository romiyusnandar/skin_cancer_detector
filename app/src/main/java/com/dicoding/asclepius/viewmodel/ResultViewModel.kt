package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.dicoding.asclepius.local.LocalResponse
import com.dicoding.asclepius.repository.ResultRepository


class ResultViewModel (private val repository: ResultRepository) : ViewModel() {

    fun insert(localResponse: LocalResponse) {
        viewModelScope.launch { repository.insert(localResponse) }
    }

    fun delete(localResponse: LocalResponse) {
        viewModelScope.launch { repository.delete(localResponse) }
    }
}