package com.dicoding.asclepius.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.di.Injection
import com.dicoding.asclepius.repository.ResultRepository
import com.dicoding.asclepius.viewmodel.HistoryViewModel
import com.dicoding.asclepius.viewmodel.NewsViewModel
import com.dicoding.asclepius.viewmodel.ResultViewModel

class ViewModelFactory private constructor(private val repository: ResultRepository, ) :
    ViewModelProvider.Factory {
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel() as T
        } else if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
}


}