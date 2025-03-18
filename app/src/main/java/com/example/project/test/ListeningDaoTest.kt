package com.example.project.test

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.project.dao.ListeningDao
import com.example.project.database.AppDatabase
import com.example.project.model.Listening
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListeningDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var listeningDao: ListeningDao
    private val testUserId = 1 // ✅ Добавлен ID пользователя

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        listeningDao = db.listeningDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertListeningTest() = runBlocking {
        val listening = Listening(userId = testUserId, name = "Test Listening", url = "http://example.com") // ✅ Добавлен userId
        listeningDao.insertListening(listening)

        val insertedListening = listeningDao.getAllListening(testUserId).first() // ✅ Передан userId
        assertEquals(listening.name, insertedListening.name)
        assertEquals(listening.url, insertedListening.url)
    }

    @Test
    fun deleteListeningTest() = runBlocking {
        val listening = Listening(userId = testUserId, name = "Test Listening", url = "http://example.com") // ✅ Добавлен userId
        listeningDao.insertListening(listening)

        listeningDao.deleteListening(listening.id) // ✅ Удаляем конкретную запись
        val remainingList = listeningDao.getAllListening(testUserId) // ✅ Передан userId
        assert(remainingList.isEmpty())
    }
}
