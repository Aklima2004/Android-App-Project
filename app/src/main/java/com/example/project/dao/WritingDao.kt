package com.example.project.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.project.model.Writing

@Dao
interface WritingDao {

    @Insert
    suspend fun insertWriting(writing: Writing)

    @Query("SELECT * FROM writing WHERE userId = :userId")
    suspend fun getAllWriting(userId: Int): List<Writing>

    @Query("SELECT * FROM writing WHERE id = :writingId")
    suspend fun getWritingById(writingId: Int): Writing?

    @Update
    suspend fun updateWriting(writing: Writing)

    @Query("DELETE FROM writing WHERE id = :writingId")
    suspend fun deleteWriting(writingId: Int)
}
