package com.example.project.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "speaking")
data class Speaking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val content: String,
    val userId: Int
)
