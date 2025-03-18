package com.example.project.addActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project.database.AppDatabase
import com.example.project.activity.ListeningActivity
import com.example.project.model.Listening
import com.example.senkazakh.R
import kotlinx.coroutines.launch

class AddListeningActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_listening)

        db = AppDatabase.getDatabase(this)

        val etListeningName = findViewById<EditText>(R.id.etListeningName)
        val etListeningUrl = findViewById<EditText>(R.id.etListeningUrl)
        val btnSaveListening = findViewById<Button>(R.id.btnSaveListening)

        // Получаем userId из SharedPreferences
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("USER_ID", -1)

        btnSaveListening.setOnClickListener {
            val name = etListeningName.text.toString().trim()
            val url = etListeningUrl.text.toString().trim()

            if (name.isEmpty() || url.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val listening = Listening(userId = userId, name = name, url = url)

                lifecycleScope.launch {
                    db.listeningDao().insertListening(listening)
                    Toast.makeText(this@AddListeningActivity, "Listening added", Toast.LENGTH_SHORT).show()

                    // Переход в ListeningActivity
                    val intent = Intent(this@AddListeningActivity, ListeningActivity::class.java)
                    startActivity(intent)

                    finish()
                }
            }
        }
    }
}
