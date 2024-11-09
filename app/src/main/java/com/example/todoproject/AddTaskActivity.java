package com.example.todoproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {
    private EditText editTitle, editDescription, editDueDate;
    private TasksDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        dbHelper = new TasksDatabaseHelper(this);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        editDueDate = findViewById(R.id.editDueDate);

        Button btnSaveTask = findViewById(R.id.btnSaveTask);
        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });
    }

    private void addTask() {
        String title = editTitle.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String dueDate = editDueDate.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show();
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
