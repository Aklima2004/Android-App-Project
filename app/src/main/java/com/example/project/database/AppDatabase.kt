package com.example.project.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.project.dao.*
import com.example.project.model.*
import androidx.room.migration.Migration

@Database(
    entities = [User::class, Task::class, Listening::class, Speaking::class, Book::class, Writing::class, TestResult::class],
    version = 13, // 🔥 Обновляем версию БД!
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao
    abstract fun listeningDao(): ListeningDao
    abstract fun speakingDao(): SpeakingDao
    abstract fun bookDao(): BookDao
    abstract fun writingDao(): WritingDao
    abstract fun testResultDao(): TestResultDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // ✅ Миграция с версии 12 на 13 (Добавляем userId в books)
        private val MIGRATION_12_13 = object : Migration(12, 13) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE books ADD COLUMN userId INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addMigrations(MIGRATION_12_13) // ✅ Добавляем миграцию!
                    .fallbackToDestructiveMigration() // ОСТОРОЖНО: Удалит старые данные, если миграция не сработает!
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
