package com.example.project.addActivity

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project.database.AppDatabase
import com.example.project.activity.WritingActivity
import com.example.project.model.Writing
import com.example.senkazakh.R
import kotlinx.coroutines.launch

class AddWritingActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_writing)

        db = AppDatabase.getDatabase(this)

        val etTitle = findViewById<EditText>(R.id.etWritingTitle)
        val etWritingText = findViewById<EditText>(R.id.etWritingText)

        // Получаем массив полей для вопросов
        val etQuestions = listOf(
            findViewById<EditText>(R.id.etQuestion1),
            findViewById<EditText>(R.id.etQuestion2),
            findViewById<EditText>(R.id.etQuestion3),
            findViewById<EditText>(R.id.etQuestion4),
            findViewById<EditText>(R.id.etQuestion5)
        )

        // Получаем массив полей для ответов
        val etAnswers = listOf(
            findViewById<EditText>(R.id.etAnswer1),
            findViewById<EditText>(R.id.etAnswer2),
            findViewById<EditText>(R.id.etAnswer3),
            findViewById<EditText>(R.id.etAnswer4),
            findViewById<EditText>(R.id.etAnswer5)
        )

        val btnSaveWriting = findViewById<Button>(R.id.btnSaveWriting)

        // Получаем `userId` из `SharedPreferences`
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("USER_ID", -1)

        btnSaveWriting.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val text = etWritingText.text.toString().trim()

            // Собираем вопросы и ответы в строку через ";"
            val questions = etQuestions.joinToString(";") { it.text.toString().trim() }
            val answers = etAnswers.joinToString(";") { it.text.toString().trim() }

            if (title.isEmpty() || text.isEmpty() || questions.isEmpty() || answers.isEmpty()) {
                Toast.makeText(this, "Fill in all the fields", Toast.LENGTH_SHORT).show()
            } else {
                val writing = Writing(
                    userId = userId,
                    title = title,
                    text = text,
                    questions = questions,
                    answers = answers
                )

                lifecycleScope.launch {
                    db.writingDao().insertWriting(writing)
                    Toast.makeText(this@AddWritingActivity, "Writing added", Toast.LENGTH_SHORT).show()

                    // Переход на WritingActivity
                    val intent = Intent(this@AddWritingActivity, WritingActivity::class.java)
                    startActivity(intent)

                    finish()
                }
            }
        }
    }
}
