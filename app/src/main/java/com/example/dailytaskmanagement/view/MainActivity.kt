package com.example.dailytaskmanagement.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytaskmanagement.R
import com.example.dailytaskmanagement.db.Task
import com.example.dailytaskmanagement.viewmodel.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), TaskAdapter.HandleTaskClick {

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerViewTasks: RecyclerView
    private lateinit var emptyStateView: View
    private lateinit var fabAddTask: FloatingActionButton
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskViewModel: TaskViewModel
    private var taskList = mutableListOf<Task>()

    private val addTaskLauncher = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                val title = data.getStringExtra(AddTaskActivity.EXTRA_TASK_TITLE) ?: ""
                val description = data.getStringExtra(AddTaskActivity.EXTRA_TASK_DESCRIPTION) ?: ""
                if (title.isNotEmpty()) {
                    val newTask = Task(taskTitle = title, taskDescription = description, dueDate = "", status = "Incomplete")
                    taskViewModel.insert(newTask)
                }
            }
        }
    }

    private val editTaskLauncher = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                val taskId = data.getIntExtra(AddTaskActivity.EXTRA_TASK_ID, -1)
                if (taskId != -1) {
                    val title = data.getStringExtra(AddTaskActivity.EXTRA_TASK_TITLE) ?: ""
                    val description = data.getStringExtra(AddTaskActivity.EXTRA_TASK_DESCRIPTION) ?: ""
                    val updatedTask = Task(id = taskId, taskTitle = title, taskDescription = description, dueDate = "", status = "Incomplete")
                    taskViewModel.update(updatedTask)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupToolbar()
        setupRecyclerView()
        setupFab()

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        taskViewModel.allTasks.observe(this, { tasks ->
            tasks?.let {
                taskList.clear()
                taskList.addAll(it)
                taskAdapter.notifyDataSetChanged()
                showEmptyState(it.isEmpty())
            }
        })
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks)
        emptyStateView = findViewById(R.id.emptyStateView)
        fabAddTask = findViewById(R.id.fabAddTask)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(taskList, this)
        recyclerViewTasks.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = taskAdapter
        }
    }

    private fun setupFab() {
        fabAddTask.setOnClickListener {
            val intent = Intent(this@MainActivity, AddTaskActivity::class.java)
            addTaskLauncher.launch(intent)
        }
    }

    private fun showEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            emptyStateView.visibility = View.VISIBLE
            recyclerViewTasks.visibility = View.GONE
        } else {
            emptyStateView.visibility = View.GONE
            recyclerViewTasks.visibility = View.VISIBLE
        }
    }

    override fun onTaskClick(task: Task) {
        // You can open a detail screen here if you want
    }

    override fun onEditClick(task: Task) {
        val intent = Intent(this, AddTaskActivity::class.java).apply {
            putExtra(AddTaskActivity.EXTRA_TASK_ID, task.id)
            putExtra(AddTaskActivity.EXTRA_TASK_TITLE, task.taskTitle)
            putExtra(AddTaskActivity.EXTRA_TASK_DESCRIPTION, task.taskDescription)
        }
        editTaskLauncher.launch(intent)
    }

    override fun onDeleteClick(task: Task) {
        AlertDialog.Builder(this)
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton("Delete") { _, _ ->
                taskViewModel.delete(task)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
