package com.example.project.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int, // Добавлено поле userId
    val category: String,
    val name: String,
    val nameRussian: String,
    val nameKazakh: String
)
