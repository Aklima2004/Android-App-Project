package com.example.project.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.project.model.Speaking

@Dao
interface SpeakingDao {

    @Insert
    suspend fun insertSpeaking(speaking: Speaking)

    @Query("SELECT * FROM speaking WHERE userId = :userId")
    suspend fun getAllSpeaking(userId: Int): List<Speaking>

    @Query("DELETE FROM speaking WHERE id = :speakingId")
    suspend fun deleteSpeaking(speakingId: Int)

    @Query("DELETE FROM speaking WHERE userId = :userId")
    suspend fun deleteSpeakingByUser(userId: Int)
}
