package com.example.project.addActivity

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project.database.AppDatabase
import com.example.project.activity.WordsActivity
import com.example.project.model.Task
import com.example.senkazakh.R
import kotlinx.coroutines.launch

class AddTaskActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        db = AppDatabase.getDatabase(this)

        // Настройка Spinner для категорий
        val spinnerCategory = findViewById<Spinner>(R.id.spinnerCategory)
        val categories = listOf(
            "Exercise",
            "Test",
            "Grammar",
            "Listening",
            "Writing",
            "Speaking",
            "Books"
        )
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoryAdapter

        val etTaskName = findViewById<EditText>(R.id.etTaskName)
        val etTaskNameRussian = findViewById<EditText>(R.id.etTaskNameRussian)
        val etTaskNameKazakh = findViewById<EditText>(R.id.etTaskNameKazakh)
        val btnSaveTask = findViewById<Button>(R.id.btnSaveTask)

        // ✅ Получаем `userId` из `SharedPreferences`
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("USER_ID", -1)

        btnSaveTask.setOnClickListener {
            val selectedCategory = spinnerCategory.selectedItem.toString()
            val name = etTaskName.text.toString().trim()
            val nameRussian = etTaskNameRussian.text.toString().trim()
            val nameKazakh = etTaskNameKazakh.text.toString().trim()

            if (name.isEmpty() || nameRussian.isEmpty() || nameKazakh.isEmpty()) {
                Toast.makeText(this, "Fill in all the fields", Toast.LENGTH_SHORT).show()
            } else {
                val task = Task(
                    userId = userId,
                    category = selectedCategory,
                    name = name,
                    nameRussian = nameRussian,
                    nameKazakh = nameKazakh
                )

                lifecycleScope.launch {
                    db.taskDao().insertTask(task)
                    Toast.makeText(this@AddTaskActivity, "Task added", Toast.LENGTH_SHORT).show()

                    // Переход на WordsActivity
                    val intent = Intent(this@AddTaskActivity, WordsActivity::class.java)
                    startActivity(intent)

                    finish()
                }
            }
        }
    }
}
