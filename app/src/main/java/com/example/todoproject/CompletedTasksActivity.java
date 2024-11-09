package com.example.todoproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class CompletedTasksActivity extends AppCompatActivity {

    private ListView tasksListView;
    private TasksDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_tasks); // Referencing the layout file

        // Initialize ListView and Database Helper
        tasksListView = findViewById(R.id.tasksListView);
        dbHelper = new TasksDatabaseHelper(this);

        fetchAndDisplayTasksGroupedByCompletion();

    }

        // Method to fetch tasks grouped by completion and display them in the ListView
        private void fetchAndDisplayTasksGroupedByCompletion () {
            ArrayList<String> tasks = dbHelper.getCompletedTasksOrderedByDueDate();
            setTitle("Tasks History");
            displayTasksWithCategory(tasks);
        }


        // Helper method to display tasks in the ListView with differentiation between categories
private void displayTasksWithCategory(ArrayList<String> tasks) {

    ArrayList<String> displayList = new ArrayList<>(tasks);

    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
    tasksListView.setAdapter(adapter);
}
    }
