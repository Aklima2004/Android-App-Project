package com.example.project.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project.database.AppDatabase
import com.example.senkazakh.R
import kotlinx.coroutines.launch
import java.io.InputStream

class EditProfileActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var userId: Int = -1
    private lateinit var ivAvatar: ImageView
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        db = AppDatabase.getDatabase(this)
        userId = intent.getIntExtra("USER_ID", -1)

        val etName = findViewById<EditText>(R.id.etEditName)
        val etEmail = findViewById<EditText>(R.id.etEditEmail)
        ivAvatar = findViewById(R.id.ivEditAvatar)
        val btnSave = findViewById<Button>(R.id.btnSaveProfile)
        val btnChangeAvatar = findViewById<Button>(R.id.btnChangeAvatar)

        // Загружаем текущие данные пользователя
        loadUserData(etName, etEmail)

        // Смена аватарки
        btnChangeAvatar.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100)
        }

        // Сохранение профиля
        btnSave.setOnClickListener {
            val newName = etName.text.toString().trim()
            val newEmail = etEmail.text.toString().trim()
            saveProfileData(newName, newEmail)
        }
    }

    private fun loadUserData(etName: EditText, etEmail: EditText) {
        lifecycleScope.launch {
            val user = db.userDao().getUserById(userId)
            if (user != null) {
                etName.setText(user.username)
                etEmail.setText(user.email)
            }
        }
    }

    private fun saveProfileData(newName: String, newEmail: String) {
        lifecycleScope.launch {
            db.userDao().updateName(userId, newName)
            db.userDao().updateEmail(userId, newEmail)
            Toast.makeText(this@EditProfileActivity, "Profile updated", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            try {
                val inputStream: InputStream? = contentResolver.openInputStream(selectedImageUri!!)
                val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                ivAvatar.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
