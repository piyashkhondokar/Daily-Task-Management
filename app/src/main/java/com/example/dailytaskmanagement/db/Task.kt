package com.example.dailytaskmanagement.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val taskTitle: String,
    val taskDescription: String,
    val dueDate: String,
    val status: String
)