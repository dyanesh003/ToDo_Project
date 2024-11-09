package com.example.todoproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;


public class TasksDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tasks";

    public TasksDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS tasks;");
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "description TEXT," +
//                "createdTime DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "dueDate DATETIME," +
                "isCompleted INTEGER DEFAULT 0" +
                ");";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert a new task
    public long insertTask(String title, String description, String dueDate, int priority, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("dueDate", dueDate);
        values.put("isCompleted",0);
        return db.insert(TABLE_NAME, null, values);
    }


    // Method to update a task based on its original title
    public int updateTask(String originalTitle, String newTitle, String newDescription, String newDueDate, int newPriority, String newType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", newTitle);
        values.put("description", newDescription);
        values.put("dueDate", newDueDate);
        values.put("priority", newPriority);
        values.put("type", newType);

        // Update the task where the title matches the original title
        return db.update(TABLE_NAME, values, "title = ?", new String[]{originalTitle});
    }

    // Method to fetch completed tasks ordered by due date (recent first)
    public ArrayList<String> getCompletedTasksOrderedByDueDate() {
        ArrayList<String> completedTasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT title, dueDate FROM tasks WHERE isCompleted = 1 ORDER BY dueDate DESC", null);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String dueDate = cursor.getString(cursor.getColumnIndexOrThrow("dueDate"));
                completedTasks.add(title + " - Due: " + dueDate);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return completedTasks;
    }

    // Method to delete a task by its id
    public int deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete the task where the id matches
        return db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
    }

    public int deleteTaskByTitle(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete the task where the title matches
        return db.delete(TABLE_NAME, "title = ?", new String[]{title});
    }

}

