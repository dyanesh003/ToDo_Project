package com.example.todoproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private TasksDatabaseHelper dbHelper;
    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = new TasksDatabaseHelper(this);
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));

        // Fetch and display tasks
        taskList = dbHelper.getTasks(); // Ensure getTasks() exists in TasksDatabaseHelper
        taskAdapter = new TaskAdapter(taskList, dbHelper, this);
        recyclerViewTasks.setAdapter(taskAdapter);

        // Floating action button to add a new task
        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        // Tasks History button to navigate to CompletedTasksActivity
        Button btnTasksHistory = findViewById(R.id.btnTasksHistory);
        btnTasksHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CompletedTasksActivity.class);
                startActivity(intent);
            }
        });
    }
}
