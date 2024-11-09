package com.dicoding.asclepius.repository

import com.dicoding.asclepius.api.ApiClient
import com.dicoding.asclepius.api.ApiConfig
import com.dicoding.asclepius.api.NewsResponse
import com.dicoding.asclepius.online.NewsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRepository {
    fun getHealthNews(
        onSuccess: (List<NewsItem>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        ApiClient.apiService.searchHealthNews("cancer", "health", "en", ApiConfig.API_KEY)
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                    if (response.isSuccessful) {
                        val newsItems = response.body()?.articles?: emptyList()
                        val newsList = newsItems.map {
                            NewsItem(
                                title = it.title,
                                imgUrl = it.urlToImage ?: "",
                                uri = it.url
                            )
                        }
                        onSuccess(newsList)
                    } else {
                        onFailure("Gagal mengambil data")
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    onFailure("Gagal mengambil data: ${t.message}")
                }
            })
    }
}