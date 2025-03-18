package com.example.project.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project.addActivity.AddListeningActivity
import com.example.project.database.AppDatabase
import com.example.project.model.Listening
import com.example.senkazakh.R
import kotlinx.coroutines.launch

class ListeningActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var listeningAdapter: ListeningAdapter
    private val listeningItems = mutableListOf<Listening>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listening)

        db = AppDatabase.getDatabase(this)
        val lvListening = findViewById<ListView>(R.id.lvListening)

        findViewById<Button>(R.id.btnAddListening).setOnClickListener {
            startActivity(Intent(this, AddListeningActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            startActivity(Intent(this, TaskListActivity::class.java))
            finish()
        }

        // Получаем userId из SharedPreferences
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("USER_ID", -1)

        if (userId != -1) {
            // Загружаем данные только для текущего пользователя
            lifecycleScope.launch {
                listeningItems.addAll(db.listeningDao().getAllListening(userId))
                listeningAdapter = ListeningAdapter(listeningItems)
                lvListening.adapter = listeningAdapter
            }
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
        }
    }

    // Добавленный адаптер для списка Listening
    inner class ListeningAdapter(private val listeningList: MutableList<Listening>) : BaseAdapter() {

        override fun getCount(): Int = listeningList.size

        override fun getItem(position: Int): Any = listeningList[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: layoutInflater.inflate(R.layout.item_listening, parent, false)
            val tvListeningName = view.findViewById<TextView>(R.id.tvListeningName)
            val btnDeleteListening = view.findViewById<ImageButton>(R.id.btnDeleteListening)

            val listening = listeningList[position]
            tvListeningName.text = listening.name

            // Удаление Listening
            btnDeleteListening.setOnClickListener {
                lifecycleScope.launch {
                    db.listeningDao().deleteListening(listening.id)
                    listeningList.removeAt(position)
                    notifyDataSetChanged()
                    Toast.makeText(this@ListeningActivity, "The element has been deleted", Toast.LENGTH_SHORT).show()
                }
            }

            // Открытие Listening по нажатию на название
            tvListeningName.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(listening.url))
                startActivity(intent)
            }

            return view
        }
    }
}
