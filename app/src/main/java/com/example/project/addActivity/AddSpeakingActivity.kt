package com.example.project.addActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project.database.AppDatabase
import com.example.project.activity.SpeakingActivity
import com.example.project.model.Speaking
import com.example.senkazakh.R
import kotlinx.coroutines.launch

class AddSpeakingActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_speaking)

        db = AppDatabase.getDatabase(this)

        // Получаем userId из SharedPreferences
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        userId = sharedPreferences.getInt("USER_ID", -1)

        val etSpeakingName = findViewById<EditText>(R.id.etSpeakingName)
        val etSpeakingContent = findViewById<EditText>(R.id.etSpeakingContent)
        val btnSaveSpeaking = findViewById<Button>(R.id.btnSaveSpeaking)

        btnSaveSpeaking.setOnClickListener {
            val name = etSpeakingName.text.toString().trim()
            val content = etSpeakingContent.text.toString().trim()

            if (name.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Fill in all the fields", Toast.LENGTH_SHORT).show()
            } else {
                val speaking = Speaking(userId = userId, name = name, content = content) // ✅ Передаём userId
                lifecycleScope.launch {
                    db.speakingDao().insertSpeaking(speaking)
                    Toast.makeText(this@AddSpeakingActivity, "Speaking added", Toast.LENGTH_SHORT).show()

                    // Возвращаемся на SpeakingActivity
                    val intent = Intent(this@AddSpeakingActivity, SpeakingActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
