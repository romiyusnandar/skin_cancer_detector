package com.dicoding.asclepius.di


import android.content.Context
import com.dicoding.asclepius.local.AppDatabase
import com.dicoding.asclepius.repository.ResultRepository

object Injection {
    fun provideRepository(context: Context): ResultRepository {
        val database = AppDatabase.getDatabase(context)
        val dao = database.appDao()
        return ResultRepository.getInstance(dao)
    }
}