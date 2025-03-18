package com.example.project.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project.database.AppDatabase
import com.example.senkazakh.R
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = AppDatabase.getDatabase(this)

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val username = findViewById<EditText>(R.id.etUsername).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()

            lifecycleScope.launch {
                val user = db.userDao().getUser(username, password)
                if (user != null) {
                    // Сохраняем ID пользователя в SharedPreferences
                    val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
                    sharedPreferences.edit().putInt("USER_ID", user.id).apply()

                    // Переход на профильную страницу
                    val intent = Intent(this@LoginActivity, ProfileActivity::class.java)
                    intent.putExtra("USER_ID", user.id)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }

        }

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
