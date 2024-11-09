package com.example.todoproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import java.util.Calendar;
import java.util.Locale;

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
        TextView editDueDateText = dialogView.findViewById(R.id.editDueDateText);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnDelete = dialogView.findViewById(R.id.btnDelete);
        Button btnMarkCompleted = dialogView.findViewById(R.id.btnMarkCompleted);

        // Set current task details in the dialog fields
        editTaskTitle.setText(task.getTitle());
        editTaskDescription.setText(task.getDescription());
        editDueDateText.setText(task.getDueDate()); // Display the current due date and time

        // Store the initial due date
        final String[] dueDate = {task.getDueDate()};

        // When clicking on the due date text, show the date and time picker
        editDueDateText.setOnClickListener(view -> showDateTimePicker(dueDate, editDueDateText));

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(view -> {
            String newTitle = editTaskTitle.getText().toString().trim();
            String newDescription = editTaskDescription.getText().toString().trim();

            if (!newTitle.isEmpty()) {
                task.setTitle(newTitle);
                task.setDescription(newDescription);
                task.setDueDate(dueDate[0]);  // Set the new due date
                dbHelper.updateTask(task.getId(), newTitle, newDescription, dueDate[0]);
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

    private void showDateTimePicker(final String[] dueDate, TextView dueDateTextView) {
        // Initialize the calendar with current date/time
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Show the date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // After selecting date, show time picker
                    TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                            (view1, selectedHour, selectedMinute) -> {
                                // Format the selected date and time
                                String formattedDateTime = String.format(Locale.getDefault(),
                                        "%02d-%02d-%d %02d:%02d", selectedDay, selectedMonth + 1, selectedYear, selectedHour, selectedMinute);
                                dueDate[0] = formattedDateTime; // Store the selected date and time
                                dueDateTextView.setText(formattedDateTime); // Update the text field
                            },
                            hour, minute, true);
                    timePickerDialog.show();
                },
                year, month, day);
        datePickerDialog.show();
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
