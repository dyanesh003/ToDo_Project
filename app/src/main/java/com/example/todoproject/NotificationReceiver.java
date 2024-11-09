package com.example.todoproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int taskId = intent.getIntExtra("task_id", -1);

        // Ensure the taskId is valid
        if (taskId == -1) {
            Log.e("NotificationReceiver", "Invalid task ID received");
            return;
        }

        // Get task details from database based on taskId
        TasksDatabaseHelper dbHelper = new TasksDatabaseHelper(context);
        Task task = dbHelper.getTaskById(taskId);

        if (task == null) {
            Log.e("NotificationReceiver", "Task not found");
            return;
        }

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "task_deadline_channel")
                .setSmallIcon(R.drawable.amrita_logo)  // Use your app icon
                .setContentTitle("Task Deadline Approaching")
                .setContentText("Task: " + task.getTitle() + " is due soon!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(taskId, builder.build());
    }
}
