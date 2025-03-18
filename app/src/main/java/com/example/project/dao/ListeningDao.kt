package com.example.project.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.project.model.Listening

@Dao
interface ListeningDao {

    @Insert
    suspend fun insertListening(listening: Listening)

    @Query("SELECT * FROM listening WHERE userId = :userId")
    suspend fun getAllListening(userId: Int): List<Listening>

    @Query("DELETE FROM listening WHERE userId = :userId")
    suspend fun deleteListeningByUser(userId: Int)

    @Query("DELETE FROM listening WHERE id = :listeningId")
    suspend fun deleteListening(listeningId: Int)
}
