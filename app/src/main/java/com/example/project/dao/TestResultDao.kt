package com.example.project.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.project.model.TestResult

@Dao
interface TestResultDao {
    @Insert
    suspend fun insertTestResult(testResult: TestResult)

    @Query("SELECT * FROM test_results WHERE userId = :userId ORDER BY id DESC")
    suspend fun getTestResultsByUser(userId: Int): List<TestResult>
}
