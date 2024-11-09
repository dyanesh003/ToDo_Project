package com.example.todoproject;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoproject.Task;
import com.example.todoproject.TasksDatabaseHelper;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private ArrayList<Task> taskList;
    private TasksDatabaseHelper dbHelper;
    private Context context;

    public TaskAdapter(ArrayList<Task> taskList, TasksDatabaseHelper dbHelper, Context context) {
        this.taskList = taskList;
        this.dbHelper = dbHelper;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskTitle.setText(task.getTitle());
        holder.taskDescription.setText(task.getDescription());
        holder.taskDueDate.setText(task.getDueDate());
        // Open edit dialog on clicking the edit button
        holder.editButton.setOnClickListener(view -> openEditDialog(task, position));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private void openEditDialog(Task task, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_task, null);
        builder.setView(dialogView);

        EditText editTaskTitle = dialogView.findViewById(R.id.editTaskTitle);
        EditText editTaskDescription = dialogView.findViewById(R.id.editTaskDescription);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnDelete = dialogView.findViewById(R.id.btnDelete);
        Button btnMarkCompleted = dialogView.findViewById(R.id.btnMarkCompleted);

        // Set current task details
        editTaskTitle.setText(task.getTitle());
        editTaskDescription.setText(task.getDescription());

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(view -> {
            String newTitle = editTaskTitle.getText().toString().trim();
            String newDescription = editTaskDescription.getText().toString().trim();
            if (!newTitle.isEmpty()) {
                task.setTitle(newTitle);
                task.setDescription(newDescription);
                dbHelper.updateTask(task.getId(), newTitle, newDescription, task.getDueDate());
                notifyItemChanged(position);
                Toast.makeText(context, "Task updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(view -> {
            dbHelper.deleteTask(task.getId());
            taskList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnMarkCompleted.setOnClickListener(view -> {
            dbHelper.markTaskAsCompleted(task.getId());
            taskList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Task marked as completed", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, taskDescription, taskDueDate;
        ImageButton editButton;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            taskDueDate = itemView.findViewById(R.id.taskDueDate);
            editButton = itemView.findViewById(R.id.btnEditTask);
        }
    }
}
