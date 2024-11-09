package com.example.todoproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

        // Mark as completed
        holder.completeButton.setOnClickListener(view -> {
            dbHelper.markTaskAsCompleted(task.getId());
            taskList.remove(position);
            notifyItemRemoved(position);
        });

        // Delete task
        holder.deleteButton.setOnClickListener(view -> {
            dbHelper.deleteTask(task.getId());
            taskList.remove(position);
            notifyItemRemoved(position);
        });

        // Edit task
        holder.editButton.setOnClickListener(view -> {
            // Open Edit Task Activity or Dialog
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, taskDescription;
        Button completeButton, deleteButton, editButton;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            completeButton = itemView.findViewById(R.id.completeButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
