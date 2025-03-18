package com.example.project.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project.database.AppDatabase
import com.example.project.addActivity.EditWritingActivity
import com.example.project.model.Writing
import com.example.project.addActivity.AddWritingActivity
import com.example.senkazakh.R
import kotlinx.coroutines.launch

class WritingActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var writingAdapter: WritingAdapter
    private val writings = mutableListOf<Writing>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)

        db = AppDatabase.getDatabase(this)
        val lvWriting = findViewById<ListView>(R.id.lvWriting)

        findViewById<Button>(R.id.btnAddWriting).setOnClickListener {
            startActivity(Intent(this, AddWritingActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            startActivity(Intent(this, TaskListActivity::class.java))
            finish()
        }

        // Получаем userId из SharedPreferences
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("USER_ID", -1)

        if (userId != -1) {
            lifecycleScope.launch {
                writings.clear()
                writings.addAll(db.writingDao().getAllWriting(userId))
                writingAdapter = WritingAdapter(writings)
                lvWriting.adapter = writingAdapter
            }
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
        }
    }

    inner class WritingAdapter(private val writingList: MutableList<Writing>) : BaseAdapter() {

        override fun getCount(): Int = writingList.size

        override fun getItem(position: Int): Any = writingList[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: layoutInflater.inflate(R.layout.item_writing, parent, false)
            val tvWritingTitle = view.findViewById<TextView>(R.id.tvWritingTitle)
            val btnDeleteWriting = view.findViewById<ImageButton>(R.id.btnDeleteWriting)
            val btnEditWriting = view.findViewById<ImageButton>(R.id.btnEditWriting)

            val writing = writingList[position]
            tvWritingTitle.text = writing.title

            tvWritingTitle.setOnClickListener {
                showWritingDialog(writing)
            }

            btnDeleteWriting.setOnClickListener {
                lifecycleScope.launch {
                    db.writingDao().deleteWriting(writing.id)
                    writingList.removeAt(position)
                    notifyDataSetChanged()
                    Toast.makeText(this@WritingActivity, "Writing deleted", Toast.LENGTH_SHORT).show()
                }
            }

            btnEditWriting.setOnClickListener {
                val intent = Intent(this@WritingActivity, EditWritingActivity::class.java).apply {
                    putExtra("writing_id", writing.id)
                }
                startActivity(intent)
            }

            return view
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("USER_ID", -1)

        if (userId != -1) {
            lifecycleScope.launch {
                writings.clear()
                writings.addAll(db.writingDao().getAllWriting(userId))
                writingAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun showWritingDialog(writing: Writing) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_writing, null)
        val tvText = dialogView.findViewById<TextView>(R.id.tvWritingText)
        val etAnswers = listOf(
            dialogView.findViewById<EditText>(R.id.etAnswer1),
            dialogView.findViewById<EditText>(R.id.etAnswer2),
            dialogView.findViewById<EditText>(R.id.etAnswer3),
            dialogView.findViewById<EditText>(R.id.etAnswer4),
            dialogView.findViewById<EditText>(R.id.etAnswer5)
        )
        val tvQuestions = listOf(
            dialogView.findViewById<TextView>(R.id.tvQuestion1),
            dialogView.findViewById<TextView>(R.id.tvQuestion2),
            dialogView.findViewById<TextView>(R.id.tvQuestion3),
            dialogView.findViewById<TextView>(R.id.tvQuestion4),
            dialogView.findViewById<TextView>(R.id.tvQuestion5)
        )
        val btnCheckAnswers = dialogView.findViewById<Button>(R.id.btnCheckAnswers)

        tvText.text = writing.text

        val questions = writing.questions.split(";")
        val correctAnswers = writing.answers.split(";")

        questions.forEachIndexed { index, question ->
            tvQuestions[index].text = question
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(writing.title)
            .setNegativeButton("Close", null)
            .create()

        btnCheckAnswers.setOnClickListener {
            val userAnswers = etAnswers.map { it.text.toString().trim() }
            val result = userAnswers.zip(correctAnswers).map { (userAnswer, correctAnswer) ->
                userAnswer.equals(correctAnswer, ignoreCase = true)
            }

            if (result.all { it }) {
                Toast.makeText(this, "All answers are correct!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Some answers are incorrect. Try again.", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}
