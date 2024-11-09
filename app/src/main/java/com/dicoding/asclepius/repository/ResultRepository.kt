package com.dicoding.asclepius.repository

import androidx.lifecycle.LiveData
import com.dicoding.asclepius.local.AppDAO
import com.dicoding.asclepius.local.LocalResponse

class ResultRepository(private val dao: AppDAO) {
    fun getAll(): LiveData<List<LocalResponse>> {
        return dao.getAll()
    }

    suspend fun insert(classificationResult: LocalResponse) {
        dao.insert(classificationResult)
    }

    suspend fun delete(classificationResult: LocalResponse) {
        dao.delete(classificationResult)
    }

    companion object {
        @Volatile
        private var instance: ResultRepository? = null

        fun getInstance(
            dao: AppDAO,
        ): ResultRepository =
            instance ?: synchronized(this) {
                instance ?: ResultRepository(dao)
            }.also { instance = it }
    }
}