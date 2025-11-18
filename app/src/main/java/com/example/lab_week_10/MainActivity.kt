package com.example.lab_week_10

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.viewmodels.TotalViewModel

class MainActivity : AppCompatActivity() {

    // Lazily create database only when needed
    private val db by lazy { prepareDatabase() }

    // Lazily create ViewModel
    private val viewModel: TotalViewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Optional: handle system bars for fullscreen layouts
        val mainView = findViewById<TextView>(R.id.text_total).rootView
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize DB data and ViewModel observation
        initializeValueFromDatabase()
        prepareViewModel()
    }

    // --- UI update method
    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    // --- ViewModel observer and button logic
    private fun prepareViewModel() {
        // Observe LiveData
        viewModel.total.observe(this) { total ->
            updateText(total)
        }

        // Increment total on button click
        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    // --- Database initialization
    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        )
            // For simplicity only; in production use background threads!
            .allowMainThreadQueries()
            .build()
    }

    // --- Initialize value from DB
    private fun initializeValueFromDatabase() {
        val totalRecord = db.totalDao().getTotal(ID)
        if (totalRecord == null) {
            // Insert if not exists
            db.totalDao().insert(Total(id = ID, total = 0))
            viewModel.setTotal(0)
        } else {
            // Load existing value
            viewModel.setTotal(totalRecord.total)
        }
    }

    // --- Save value back to DB when app closes
    override fun onPause() {
        super.onPause()
        val currentTotal = viewModel.total.value ?: 0
        db.totalDao().update(Total(ID, currentTotal))
    }

    companion object {
        const val ID: Long = 1L
    }
}
