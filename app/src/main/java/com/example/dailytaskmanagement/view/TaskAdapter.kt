package com.example.dailytaskmanagement.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytaskmanagement.R
import com.example.dailytaskmanagement.db.Task

class TaskAdapter(
    private val taskList: List<Task>,
    private val listener: HandleTaskClick
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    interface HandleTaskClick {
        fun onTaskClick(task: Task)
        fun onEditClick(task: Task)
        fun onDeleteClick(task: Task)
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val dueDateTextView: TextView = itemView.findViewById(R.id.dueDateTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item_layout, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = taskList[position]

        holder.titleTextView.text = currentTask.taskTitle
        holder.descriptionTextView.text = currentTask.taskDescription
        holder.dueDateTextView.text = currentTask.dueDate
        holder.statusTextView.text = currentTask.status

        holder.itemView.setOnClickListener {
            listener.onTaskClick(currentTask)
        }

        holder.itemView.setOnLongClickListener {
            showPopupMenu(it, currentTask)
            true
        }
    }

    private fun showPopupMenu(view: View, task: Task) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.task_item_menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    listener.onEditClick(task)
                    true
                }
                R.id.menu_delete -> {
                    listener.onDeleteClick(task)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}
