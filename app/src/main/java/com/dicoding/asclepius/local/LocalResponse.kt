package com.dicoding.asclepius.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "local_db")
@Parcelize
data class LocalResponse(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo("timestamp") val timestamp: Long,
    @ColumnInfo("image_uri") val imageUri: String,
    @ColumnInfo("label") val label: String,
    @ColumnInfo("score") val score: Float,
) : Parcelable