package com.example.project.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project.addActivity.AddSpeakingActivity
import com.example.project.database.AppDatabase
import com.example.project.model.Speaking
import com.example.senkazakh.R
import kotlinx.coroutines.launch

class SpeakingActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var speakingAdapter: SpeakingAdapter
    private val speakingItems = mutableListOf<Speaking>()
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speaking)

        db = AppDatabase.getDatabase(this)
        val lvSpeaking = findViewById<ListView>(R.id.lvSpeaking)

        // Получаем userId из SharedPreferences
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        userId = sharedPreferences.getInt("USER_ID", -1)

        findViewById<Button>(R.id.btnAddSpeaking).setOnClickListener {
            startActivity(Intent(this, AddSpeakingActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            startActivity(Intent(this, TaskListActivity::class.java))
            finish()
        }

        // Загружаем данные только для текущего пользователя
        lifecycleScope.launch {
            val userSpeaking = db.speakingDao().getAllSpeaking(userId)
            speakingItems.addAll(userSpeaking)
            speakingAdapter = SpeakingAdapter(speakingItems)
            lvSpeaking.adapter = speakingAdapter
        }
    }

    // Адаптер с привязкой к userId
    inner class SpeakingAdapter(private val speakingList: MutableList<Speaking>) : BaseAdapter() {

        override fun getCount(): Int = speakingList.size

        override fun getItem(position: Int): Any = speakingList[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: layoutInflater.inflate(R.layout.item_speaking, parent, false)
            val tvSpeakingName = view.findViewById<TextView>(R.id.tvSpeakingName)
            val btnDeleteSpeaking = view.findViewById<ImageButton>(R.id.btnDeleteSpeaking)

            val speaking = speakingList[position]
            tvSpeakingName.text = speaking.name

            // Удаление Speaking (только для текущего пользователя)
            btnDeleteSpeaking.setOnClickListener {
                lifecycleScope.launch {
                    db.speakingDao().deleteSpeaking(speaking.id)
                    speakingList.removeAt(position)
                    notifyDataSetChanged()
                    Toast.makeText(this@SpeakingActivity, "The element has been deleted", Toast.LENGTH_SHORT).show()
                }
            }

            // Открытие подробного контента Speaking при нажатии
            tvSpeakingName.setOnClickListener {
                showSpeakingContentDialog(speaking)
            }

            return view
        }
    }

    // Метод для отображения содержимого Speaking
    private fun showSpeakingContentDialog(speaking: Speaking) {
        AlertDialog.Builder(this)
            .setTitle(speaking.name)
            .setMessage(speaking.content)
            .setPositiveButton("Close", null)
            .show()
    }
}
