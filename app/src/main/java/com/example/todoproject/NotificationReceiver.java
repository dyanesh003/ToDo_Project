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
        Log.d("NotificationReceiver", "Notification triggered");

        int taskId = intent.getIntExtra("task_id", -1);

        // Get task details from database based on taskId
        TasksDatabaseHelper dbHelper = new TasksDatabaseHelper(context);
        Task task = dbHelper.getTaskById(taskId);

        // Create notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "task_deadline_channel")
                .setSmallIcon(R.drawable.amrita_logo)  // Use your app icon
                .setContentTitle("Task Deadline Approaching")
                .setContentText("Task: " + task.getTitle() + " is due soon!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);  // Dismiss notification when clicked

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(taskId, builder.build());

        Log.d("NotificationReceiver", "Notification sent for task: " + task.getTitle());
    }
}
