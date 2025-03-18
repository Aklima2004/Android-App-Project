package com.example.project.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.project.profile.ProfileActivity
import com.example.senkazakh.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class TaskListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        findViewById<CardView>(R.id.cardTask1).setOnClickListener {
            val intent = Intent(this, WordsActivity::class.java)
            startActivity(intent)
        }

        findViewById<CardView>(R.id.cardTask2).setOnClickListener {
            val intent = Intent(this, ListeningActivity::class.java)
            startActivity(intent)
        }

        findViewById<CardView>(R.id.cardTask3).setOnClickListener {
            val intent = Intent(this, SpeakingActivity::class.java)
            startActivity(intent)
        }

        findViewById<CardView>(R.id.cardTask4).setOnClickListener {
            val intent = Intent(this, BooksActivity::class.java)
            startActivity(intent)
        }

        findViewById<CardView>(R.id.cardTask5).setOnClickListener {
            val intent = Intent(this, WritingActivity::class.java)
            startActivity(intent)
        }

        findViewById<CardView>(R.id.cardTask6).setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.nav_home
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
