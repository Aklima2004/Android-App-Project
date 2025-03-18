package com.example.project.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project.database.AppDatabase
import com.example.project.profile.ProfileActivity
import com.example.project.api.RetrofitInstance
import com.example.project.addActivity.AddTaskActivity
import com.example.project.model.Task
import com.example.senkazakh.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class WordsActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var taskAdapter: TaskAdapter
    private val tasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words)

        db = AppDatabase.getDatabase(this)
        val lvTasks = findViewById<ListView>(R.id.lvTasks)

        findViewById<Button>(R.id.btnAddTask).setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            startActivity(Intent(this, TaskListActivity::class.java))
            finish()
        }

        // Получаем ID текущего пользователя из SharedPreferences
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("USER_ID", -1)

        if (userId != -1) {
            // Загружаем задачи только для текущего пользователя
            lifecycleScope.launch {
                tasks.addAll(db.taskDao().getAllTasks(userId))
                taskAdapter = TaskAdapter(tasks)
                lvTasks.adapter = taskAdapter
            }
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
        }
    }

    // Добавленный адаптер для списка задач
    inner class TaskAdapter(private val taskList: MutableList<Task>) : BaseAdapter() {

        override fun getCount(): Int = taskList.size

        override fun getItem(position: Int): Any = taskList[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: layoutInflater.inflate(R.layout.item_task, parent, false)
            val tvTaskName = view.findViewById<TextView>(R.id.tvTaskName)
            val btnDeleteTask = view.findViewById<ImageButton>(R.id.btnDeleteTask)

            val task = taskList[position]
            tvTaskName.text = task.name

            // Удаление задачи
            btnDeleteTask.setOnClickListener {
                lifecycleScope.launch {
                    db.taskDao().deleteTask(task.id)
                    taskList.removeAt(position)
                    notifyDataSetChanged()
                    Toast.makeText(this@WordsActivity, "Task deleted", Toast.LENGTH_SHORT).show()
                }
            }

            // Проверка перевода при клике на название задачи
            tvTaskName.setOnClickListener {
                showTranslationDialog(task)
            }

            return view
        }
    }

    // Метод для отображения модального окна перевода
    private fun showTranslationDialog(task: Task) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_check_translation, null)
        val tvTaskNameRussian = dialogView.findViewById<TextView>(R.id.tvTaskNameRussian)
        val etTaskNameKazakh = dialogView.findViewById<EditText>(R.id.etTaskNameKazakh)
        val btnCheck = dialogView.findViewById<Button>(R.id.btnCheck)

        tvTaskNameRussian.text = task.nameRussian

        val dialog = AlertDialog.Builder(this, R.style.RoundedDialog)
            .setView(dialogView)
            .setTitle("Translation check")
            .setNegativeButton("Close", null)
            .create()

        btnCheck.setOnClickListener {
            val enteredKazakh = etTaskNameKazakh.text.toString().trim()
            if (enteredKazakh == task.nameKazakh) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Wrong. Try again!", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}
