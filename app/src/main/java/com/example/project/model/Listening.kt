package com.example.project.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "listening")
data class Listening(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val name: String,
    val url: String
)
