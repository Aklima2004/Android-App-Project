package com.example.project.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.project.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): User?

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("UPDATE users SET username = :newName WHERE id = :userId")
    suspend fun updateName(userId: Int, newName: String)

    @Query("UPDATE users SET level = :newLevel WHERE id = :userId")
    suspend fun updateLevel(userId: Int, newLevel: String)

    @Query("UPDATE users SET goal = :newGoal WHERE id = :userId")
    suspend fun updateGoal(userId: Int, newGoal: String)

    @Query("UPDATE users SET email = :newEmail WHERE id = :userId")
    suspend fun updateEmail(userId: Int, newEmail: String)

    

}

