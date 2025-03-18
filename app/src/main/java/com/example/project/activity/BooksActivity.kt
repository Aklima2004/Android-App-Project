package com.example.project.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project.addActivity.AddBookActivity
import com.example.project.database.AppDatabase
import com.example.project.model.Book
import com.example.senkazakh.R
import kotlinx.coroutines.launch

class BooksActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var bookAdapter: BookAdapter
    private val books = mutableListOf<Book>()
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)

        db = AppDatabase.getDatabase(this)
        val lvBooks = findViewById<ListView>(R.id.lvBooks)

        // Получаем userId из SharedPreferences
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        userId = sharedPreferences.getInt("USER_ID", -1)

        if (userId == -1) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        findViewById<Button>(R.id.btnAddBook).setOnClickListener {
            startActivity(Intent(this, AddBookActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            startActivity(Intent(this, TaskListActivity::class.java))
            finish()
        }

        lifecycleScope.launch {
            books.addAll(db.bookDao().getAllBooks(userId))
            bookAdapter = BookAdapter(books)
            lvBooks.adapter = bookAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            books.clear()
            books.addAll(db.bookDao().getAllBooks(userId)) //Загружаем книги только этого пользователя
            bookAdapter.notifyDataSetChanged()
        }
    }

    inner class BookAdapter(private val bookList: MutableList<Book>) : BaseAdapter() {

        override fun getCount(): Int = bookList.size

        override fun getItem(position: Int): Any = bookList[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: layoutInflater.inflate(R.layout.item_book, parent, false)
            val tvBookTitle = view.findViewById<TextView>(R.id.tvBookTitle)
            val btnDeleteBook = view.findViewById<ImageButton>(R.id.btnDeleteBook)

            val book = bookList[position]
            tvBookTitle.text = book.title

            btnDeleteBook.setOnClickListener {
                lifecycleScope.launch {
                    db.bookDao().deleteBook(book.id)
                    bookList.removeAt(position)
                    notifyDataSetChanged()
                    Toast.makeText(this@BooksActivity, "The book was deleted", Toast.LENGTH_SHORT).show()
                }
            }

            return view
        }
    }
}
