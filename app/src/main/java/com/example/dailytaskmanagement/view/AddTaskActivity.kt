package com.example.dailytaskmanagement.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.dailytaskmanagement.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class AddTaskActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var editTextTitle: TextInputEditText
    private lateinit var editTextDescription: TextInputEditText
    private lateinit var datePickerDueDate: DatePicker
    private lateinit var checkBoxCompleted: MaterialCheckBox
    private lateinit var buttonSave: MaterialButton

    private var taskId: Int = -1
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        initViews()
        setupToolbar()
        checkEditMode()
        setupSaveButton()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        editTextTitle = findViewById(R.id.editTextTitle)
        editTextDescription = findViewById(R.id.editTextDescription)
        datePickerDueDate = findViewById(R.id.datePickerDueDate)
        checkBoxCompleted = findViewById(R.id.checkBoxCompleted)
        buttonSave = findViewById(R.id.buttonSave)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun checkEditMode() {
        if (intent.hasExtra(EXTRA_TASK_ID)) {
            isEditMode = true
            taskId = intent.getIntExtra(EXTRA_TASK_ID, -1)
            editTextTitle.setText(intent.getStringExtra(EXTRA_TASK_TITLE))
            editTextDescription.setText(intent.getStringExtra(EXTRA_TASK_DESCRIPTION))

            val dueDateMillis = intent.getLongExtra(EXTRA_TASK_DUE_DATE, System.currentTimeMillis())
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = dueDateMillis
            datePickerDueDate.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            checkBoxCompleted.isChecked = intent.getBooleanExtra(EXTRA_TASK_COMPLETED, false)
            buttonSave.text = "Update Task"
        }
    }

    private fun setupSaveButton() {
        buttonSave.setOnClickListener {
            saveTask()
        }
    }

    private fun saveTask() {
        val title = editTextTitle.text.toString().trim()

        if (title.isEmpty()) {
            editTextTitle.error = "Please enter a task title"
            Toast.makeText(this, "Please enter a task title", Toast.LENGTH_SHORT).show()
            return
        }

        val description = editTextDescription.text.toString().trim()

        val day = datePickerDueDate.dayOfMonth
        val month = datePickerDueDate.month
        val year = datePickerDueDate.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val dueDate = calendar.time

        val isCompleted = checkBoxCompleted.isChecked

        val resultIntent = Intent().apply {
            putExtra(EXTRA_TASK_TITLE, title)
            putExtra(EXTRA_TASK_DESCRIPTION, description)
            putExtra(EXTRA_TASK_DUE_DATE, dueDate.time)
            putExtra(EXTRA_TASK_COMPLETED, isCompleted)

            if (isEditMode) {
                putExtra(EXTRA_TASK_ID, taskId)
            }
        }

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    companion object {
        const val EXTRA_TASK_ID = "com.example.dailytaskmanagement.EXTRA_TASK_ID"
        const val EXTRA_TASK_TITLE = "com.example.dailytaskmanagement.EXTRA_TASK_TITLE"
        const val EXTRA_TASK_DESCRIPTION = "com.example.dailytaskmanagement.EXTRA_TASK_DESCRIPTION"
        const val EXTRA_TASK_DUE_DATE = "com.example.dailytaskmanagement.EXTRA_TASK_DUE_DATE"
        const val EXTRA_TASK_COMPLETED = "com.example.dailytaskmanagement.EXTRA_TASK_COMPLETED"
    }
}