package com.example.todoproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editTitle, editDescription;
    private TextView txtDueDate;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private TasksDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        dbHelper = new TasksDatabaseHelper(this);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        txtDueDate = findViewById(R.id.txtDueDate);

        Button btnSaveTask = findViewById(R.id.btnSaveTask);

        // Set up the date and time pickers
        txtDueDate.setOnClickListener(view -> {
            showDatePicker();
        });

        // Save task logic
        btnSaveTask.setOnClickListener(view -> {
            addTask();
        });
    }

    // Method to show the DatePickerDialog
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    selectedYear = year;
                    selectedMonth = monthOfYear;
                    selectedDay = dayOfMonth;
                    showTimePicker(); // Call time picker after date selection
                },
                selectedYear, selectedMonth, selectedDay);
        datePickerDialog.show();
    }

    // Method to show the TimePickerDialog
    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minute;
                    // Combine the selected date and time to show in the TextView
                    String dateTime = String.format(Locale.getDefault(),
                            "%02d-%02d-%d %02d:%02d", selectedDay, selectedMonth + 1, selectedYear, selectedHour, selectedMinute);
                    txtDueDate.setText(dateTime); // Display combined date and time
                },
                selectedHour, selectedMinute, true);
        timePickerDialog.show();
    }

    // Method to add a task with the selected date and time
    private void addTask() {
        String title = editTitle.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String dueDate = txtDueDate.getText().toString().trim(); // Due date and time

        if (title.isEmpty() || dueDate.isEmpty()) {
            Toast.makeText(this, "Title and Date are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert task into the database
        long result = dbHelper.insertTask(title, description, dueDate);
        if (result != -1) {
            // Task successfully added to the database
            Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();

            // Create a Task object with the details
            Task task = new Task((int) result, title, description, dueDate);

            // Schedule the notification for the task's deadline
            scheduleTaskDeadlineNotification(AddTaskActivity.this, task);

            // Redirect to the HomeActivity
            Intent intent = new Intent(AddTaskActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to schedule the task deadline notification
    public void scheduleTaskDeadlineNotification(Context context, Task task) {
        // Ensure the date format matches the one entered by the user (e.g., dd-MM-yyyy HH:mm)
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        try {
            // Parse the due date string into a Date object
            java.util.Date dueDate = sdf.parse(task.getDueDate());
            if (dueDate != null) {
                // Schedule the alarm (notification)
                NotificationManager notificationManager = new NotificationManager();
                notificationManager.scheduleTaskDeadlineNotification(context, task);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Invalid date format", Toast.LENGTH_SHORT).show();
        }
    }
}
