package com.example.todoproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotificationManager {

    public PendingIntent getTaskPendingIntent(Context context, int taskId) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("task_id", taskId);
        return PendingIntent.getBroadcast(context, taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // Method to schedule the task deadline notification
    public void scheduleTaskDeadlineNotification(Context context, Task task) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        try {
            Date dueDate = sdf.parse(task.getDueDate());
            if (dueDate != null) {
                calendar.setTime(dueDate);
            } else {
                Log.e("NotificationManager", "Invalid date format");
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Set the notification time using AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getTaskPendingIntent(context, task.getId());

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}
