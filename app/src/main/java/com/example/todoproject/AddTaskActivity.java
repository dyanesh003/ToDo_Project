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

    private void showDatePicker() {
        // Initialize calendar for current date
        final Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Show date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    selectedYear = year;
                    selectedMonth = monthOfYear;
                    selectedDay = dayOfMonth;
                    showTimePicker(); // Call the time picker after date selection
                },
                selectedYear, selectedMonth, selectedDay);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        // Initialize current time
        final Calendar calendar = Calendar.getInstance();
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = calendar.get(Calendar.MINUTE);

        // Show time picker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minute;
                    // Combine date and time to show in the TextView
                    String dateTime = String.format(Locale.getDefault(),
                            "%02d-%02d-%d %02d:%02d", selectedDay, selectedMonth + 1, selectedYear, selectedHour, selectedMinute);
                    txtDueDate.setText(dateTime);
                },
                selectedHour, selectedMinute, true);
        timePickerDialog.show();
    }

    private void addTask() {
        String title = editTitle.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String dueDate = txtDueDate.getText().toString().trim();

        if (title.isEmpty() || dueDate.isEmpty()) {
            Toast.makeText(this, "Title and Date are required", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = dbHelper.insertTask(title, description, dueDate);
        if (result != -1) {
            Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddTaskActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show();
        }
    }
}
