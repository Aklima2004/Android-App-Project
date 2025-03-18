package com.example.project.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.project.model.Listening
import com.example.project.model.Task

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(task: Task)

    @Insert
    suspend fun insertTasks(tasks: List<Task>)

    @Query("SELECT * FROM tasks WHERE userId = :userId")
    suspend fun getAllTasks(userId: Int): List<Task>

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: Int)

    // Методы для Listening
    @Insert
    suspend fun insertListening(listening: Listening)

    @Query("SELECT * FROM listening WHERE userId = :userId")
    suspend fun getAllListening(userId: Int): List<Listening>

    @Query("DELETE FROM tasks WHERE userId = :userId")
    suspend fun deleteTasksByUser(userId: Int)

    @Query("DELETE FROM listening WHERE userId = :userId")
    suspend fun deleteListeningByUser(userId: Int)

}
