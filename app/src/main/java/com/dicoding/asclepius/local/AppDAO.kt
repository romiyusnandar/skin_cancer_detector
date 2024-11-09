package com.dicoding.asclepius.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AppDAO {
    @Query("SELECT * FROM local_db ORDER BY timestamp DESC")
    fun getAll(): LiveData<List<LocalResponse>>

    @Query("SELECT * FROM local_db WHERE id = :id")
    fun getClassificationResultById(id: Int): LiveData<LocalResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg classificationResults: LocalResponse)

    @Delete
    suspend fun delete(classificationResult: LocalResponse)

}