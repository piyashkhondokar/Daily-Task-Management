package com.example.dailytaskmanagement.repository

import androidx.lifecycle.LiveData
import com.example.dailytaskmanagement.db.Task
import com.example.dailytaskmanagement.db.TaskDao

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()

    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }

    suspend fun delete(task: Task) {
        taskDao.delete(task)
    }

    suspend fun update(task: Task) {
        taskDao.update(task)
    }
}