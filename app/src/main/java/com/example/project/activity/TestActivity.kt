package com.example.project.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project.database.AppDatabase
import com.example.project.profile.ProfileActivity
import com.example.project.model.TestResult
import com.example.senkazakh.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TestActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var currentScore = 0
    private val maxScore = 100
    private lateinit var timer: CountDownTimer
    private val timeLimit = 60000L // 60 секунд

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        db = AppDatabase.getDatabase(this)

        val tvQuestion = findViewById<TextView>(R.id.tvQuestion)
        val etAnswer = findViewById<TextView>(R.id.etAnswer)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val tvTimer = findViewById<TextView>(R.id.tvTimer)
        val tvScore = findViewById<TextView>(R.id.tvScore)

        var questions: List<Pair<String, String>> = emptyList()
        var currentQuestionIndex = 0

        // Получаем userId из SharedPreferences
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("USER_ID", -1)

        if (userId != -1) {
            lifecycleScope.launch {
                // Получаем вопросы для текущего пользователя
                val tasks = db.taskDao().getAllTasks(userId).map { it.nameKazakh to it.nameRussian }
                val writings = db.writingDao().getAllWriting(userId).flatMap { writing ->
                    val questions = writing.questions.split(";")
                    val answers = writing.answers.split(";")
                    questions.zip(answers)
                }
                questions = (tasks + writings).shuffled()

                if (questions.isEmpty()) {
                    Toast.makeText(this@TestActivity, "No questions available", Toast.LENGTH_SHORT).show()
                    finish()
                    return@launch
                }

                // Отображаем первый вопрос
                tvQuestion.text = questions[currentQuestionIndex].first
            }
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Таймер
        timer = object : CountDownTimer(timeLimit, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                tvTimer.text = "Time left: $secondsRemaining seconds"
            }

            override fun onFinish() {
                finishTest(userId)
            }
        }.start()

        btnNext.setOnClickListener {
            val userAnswer = etAnswer.text.toString().trim()
            if (userAnswer.equals(questions[currentQuestionIndex].second, ignoreCase = true)) {
                currentScore += maxScore / questions.size
                tvScore.text = "Score: $currentScore"
            }
            etAnswer.text = ""
            currentQuestionIndex++

            if (currentQuestionIndex >= questions.size) {
                finishTest(userId)
            } else {
                tvQuestion.text = questions[currentQuestionIndex].first
            }
        }

        btnSubmit.setOnClickListener {
            finishTest(userId)
        }
    }

    private fun finishTest(userId: Int) {
        timer.cancel()

        lifecycleScope.launch {
            if (userId == -1) {
                Toast.makeText(this@TestActivity, "User ID not found", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val testResult = TestResult(userId = userId, score = currentScore, date = date)
            db.testResultDao().insertTestResult(testResult)

            // Отображаем сообщение и переходим на страницу профиля
            Toast.makeText(this@TestActivity, "Test finished! Your score: $currentScore", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@TestActivity, ProfileActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)

            finish()
        }
    }
}
