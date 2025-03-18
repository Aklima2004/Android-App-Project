package com.example.project.profile

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project.activity.MainActivity
import com.example.project.database.AppDatabase
import com.example.project.activity.SettingsActivity
import com.example.project.activity.TaskListActivity
import com.example.senkazakh.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var userId: Int = -1
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        db = AppDatabase.getDatabase(this)
        sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)

        val tvName = findViewById<TextView>(R.id.tvName)
        val tvContact = findViewById<TextView>(R.id.tvContact)
        val tvLevel = findViewById<TextView>(R.id.tvLevel)
        val tvGoal = findViewById<TextView>(R.id.tvGoal)
        val tvResults = findViewById<TextView>(R.id.tvResults)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.nav_profile
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, TaskListActivity::class.java))
                    true
                }
                R.id.nav_profile -> true
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Получаем ID пользователя из SharedPreferences
        userId = sharedPreferences.getInt("USER_ID", -1)
        if (userId == -1) {
            userId = intent.getIntExtra("USER_ID", -1)
            sharedPreferences.edit().putInt("USER_ID", userId).apply()
        }

        loadUserData()

        findViewById<ImageView>(R.id.ivEditProfile).setOnClickListener {
            val name = tvName.text.toString()
            val email = tvContact.text.toString()
            showEditProfileDialog(name, email)  // Теперь передаем параметры
        }

        btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun loadUserData() {
        if (userId != -1) {
            lifecycleScope.launch {
                val user = db.userDao().getUserById(userId)
                if (user != null) {
                    findViewById<TextView>(R.id.tvName).text = user.username
                    findViewById<TextView>(R.id.tvContact).text = user.email
                    findViewById<TextView>(R.id.tvLevel).text = "Level: ${user.level ?: "A1"}"
                    findViewById<TextView>(R.id.tvGoal).text = "Goal: ${user.goal ?: "No data available"}"
                }
            }

            lifecycleScope.launch {
                val testResults = db.testResultDao().getTestResultsByUser(userId)
                val resultsText = testResults.joinToString("\n") { "Date: ${it.date}, Score: ${it.score}" }
                findViewById<TextView>(R.id.tvResults).text = if (resultsText.isEmpty()) "No test results" else resultsText
            }
        }
    }

    private fun showEditProfileDialog(currentName: String, currentEmail: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_profile, null)
        val etEditName = dialogView.findViewById<EditText>(R.id.etEditName)
        val etEditEmail = dialogView.findViewById<EditText>(R.id.etEditEmail)

        etEditName.setText(currentName)
        etEditEmail.setText(currentEmail)

        AlertDialog.Builder(this)
            .setTitle("Edit Profile")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newName = etEditName.text.toString().trim()
                val newEmail = etEditEmail.text.toString().trim()
                if (newName.isNotEmpty() && newEmail.isNotEmpty()) {
                    updateUserProfile(newName, newEmail)
                } else {
                    Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun updateUserProfile(newName: String, newEmail: String) {
        lifecycleScope.launch {
            db.userDao().updateName(userId, newName)
            db.userDao().updateEmail(userId, newEmail)
            loadUserData()
            Toast.makeText(this@ProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Log out of your account")
            .setMessage("Are you sure you want to get out?")
            .setPositiveButton("Yes") { _, _ ->
                logout()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun logout() {
        lifecycleScope.launch {
            val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
            val userId = sharedPreferences.getInt("USER_ID", -1)

            if (userId != -1) {

            }

            sharedPreferences.edit().remove("USER_ID").apply()
            val intent = Intent(this@ProfileActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Toast.makeText(this@ProfileActivity, "You have logged out", Toast.LENGTH_SHORT).show()
        }
    }

}
