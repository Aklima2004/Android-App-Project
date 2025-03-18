package com.example.project.addActivity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project.database.AppDatabase
import com.example.project.activity.BooksActivity
import com.example.project.model.Book
import com.example.senkazakh.R
import kotlinx.coroutines.launch

class AddBookActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var userId: Int = -1
    private lateinit var tvSelectedFile: TextView
    private var selectedFilePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        db = AppDatabase.getDatabase(this)

        val etBookTitle = findViewById<EditText>(R.id.etBookTitle)
        val etBookAuthor = findViewById<EditText>(R.id.etBookAuthor)
        tvSelectedFile = findViewById(R.id.tvSelectedFile)
        val btnSelectFile = findViewById<Button>(R.id.btnSelectFile)
        val btnSaveBook = findViewById<Button>(R.id.btnSaveBook)

        // Получаем userId из SharedPreferences
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        userId = sharedPreferences.getInt("USER_ID", -1)

        if (userId == -1) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Кнопка выбора файла
        btnSelectFile.setOnClickListener {
            selectFile()
        }

        btnSaveBook.setOnClickListener {
            val title = etBookTitle.text.toString().trim()
            val author = etBookAuthor.text.toString().trim()
            val filePath = selectedFilePath // Используем путь, сохраненный после выбора файла

            if (title.isEmpty() || author.isEmpty() || filePath == null) {
                Toast.makeText(this, "Fill in all the fields", Toast.LENGTH_SHORT).show()
            } else {
                val book = Book(title = title, author = author, filePath = filePath, userId = userId)
                lifecycleScope.launch {
                    db.bookDao().insertBook(book)
                    Toast.makeText(this@AddBookActivity, "Book added", Toast.LENGTH_SHORT).show()

                    // Возвращаемся в BooksActivity
                    val intent = Intent(this@AddBookActivity, BooksActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    // ✅ Метод для выбора файла
    private fun selectFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, FILE_PICK_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_PICK_REQUEST && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            if (uri != null) {
                selectedFilePath = uri.toString() // ✅ Сохраняем путь
                tvSelectedFile.text = "Selected file: ${uri.lastPathSegment}" // ✅ Отображаем имя файла
            }
        }
    }

    companion object {
        private const val FILE_PICK_REQUEST = 100
    }
}
