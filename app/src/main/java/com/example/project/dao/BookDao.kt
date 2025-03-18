package com.example.project.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.project.model.Book

@Dao
interface BookDao {

    @Insert
    suspend fun insertBook(book: Book)

    @Query("SELECT * FROM books WHERE userId = :userId")
    suspend fun getAllBooks(userId: Int): List<Book>

    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun deleteBook(bookId: Int)

    @Query("DELETE FROM books WHERE userId = :userId")
    suspend fun deleteBooksByUser(userId: Int)
}
