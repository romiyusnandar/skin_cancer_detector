package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.online.NewsItem
import com.dicoding.asclepius.repository.NewsRepository

class NewsViewModel (private val newsRepository: NewsRepository) : ViewModel() {

    private val _newsList = MutableLiveData<List<NewsItem>>()
    val newsList: MutableLiveData<List<NewsItem>> = _newsList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading


    fun getHealthNews() {
        _isLoading.value = true
        newsRepository.getHealthNews(
            onSuccess = { newsItems ->
                _newsList.value = newsItems
                _isLoading.value = false
            },
            onFailure = { errorMessage ->
                _isLoading.value = false
            }
        )
    }
}