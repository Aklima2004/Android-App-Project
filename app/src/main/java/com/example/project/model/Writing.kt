package com.example.project.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "writing")
data class Writing(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val title: String,
    val text: String,
    val questions: String,
    val answers: String
)
