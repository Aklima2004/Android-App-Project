package com.example.project.addActivity

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project.database.AppDatabase
import com.example.project.model.Writing
import com.example.senkazakh.R
import kotlinx.coroutines.launch

class EditWritingActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var writingId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_writing)

        db = AppDatabase.getDatabase(this)

        val etTitle = findViewById<EditText>(R.id.etWritingTitle)
        val etText = findViewById<EditText>(R.id.etWritingText)
        val etQuestions = findViewById<EditText>(R.id.etQuestions)
        val etAnswers = findViewById<EditText>(R.id.etAnswers)
        val btnSaveWriting = findViewById<Button>(R.id.btnSaveWriting)

        // ✅ Получаем `writingId` из `Intent`
        writingId = intent.getIntExtra("writing_id", -1)

        if (writingId != -1) {
            lifecycleScope.launch {
                val writing = db.writingDao().getWritingById(writingId)
                writing?.let {
                    etTitle.setText(it.title)
                    etText.setText(it.text)
                    etQuestions.setText(it.questions)
                    etAnswers.setText(it.answers)
                }
            }
        }

        btnSaveWriting.setOnClickListener {
            val updatedWriting = Writing(
                id = writingId, // ✅ Передаем ID
                userId = intent.getIntExtra("USER_ID", -1),
                title = etTitle.text.toString().trim(),
                text = etText.text.toString().trim(),
                questions = etQuestions.text.toString().trim(),
                answers = etAnswers.text.toString().trim()
            )

            lifecycleScope.launch {
                db.writingDao().updateWriting(updatedWriting)
                Toast.makeText(this@EditWritingActivity, "Writing updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
