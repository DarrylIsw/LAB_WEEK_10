package com.example.lab_week_10

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalObject
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.viewmodels.TotalViewModel
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val db by lazy { prepareDatabase() }

    private val viewModel: TotalViewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val mainView = findViewById<TextView>(R.id.text_total).rootView
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeValueFromDatabase()
        prepareViewModel()
    }

    // ------------------------------------------------------------
    // UI
    // ------------------------------------------------------------
    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareViewModel() {
        viewModel.total.observe(this) { total ->
            updateText(total)
        }

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    // ------------------------------------------------------------
    // DATABASE
    // ------------------------------------------------------------
    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        )
            .allowMainThreadQueries() // simple for classroom assignment
            .build()
    }

    private fun initializeValueFromDatabase() {
        val dao = db.totalDao()
        val existing = dao.getTotal()

        if (existing == null) {
            // Create initial row with date
            dao.insert(
                Total(
                    id = ID,
                    total = TotalObject(
                        value = 0,
                        date = Date().toString()
                    )
                )
            )
            viewModel.setTotal(0)
        } else {
            viewModel.setTotal(existing.total.value)
        }
    }

    // ------------------------------------------------------------
    // LIFECYCLE: update date when app pauses
    // ------------------------------------------------------------
    override fun onPause() {
        super.onPause()

        val dao = db.totalDao()
        val current = dao.getTotal()

        if (current != null) {
            val updated = Total(
                id = current.id,
                total = TotalObject(
                    value = current.total.value,
                    date = Date().toString()
                )
            )
            dao.update(updated)
        }
    }

    // ------------------------------------------------------------
    // LIFECYCLE: show toast on start
    // ------------------------------------------------------------
    override fun onStart() {
        super.onStart()

        val dao = db.totalDao()
        val current = dao.getTotal()

        current?.let {
            Toast.makeText(
                this,
                "Last updated: ${it.total.date}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        const val ID: Long = 1L
    }
}
