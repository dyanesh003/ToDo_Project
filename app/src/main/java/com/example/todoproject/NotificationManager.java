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
        // Parse the task's due date into a Calendar object
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        try {
            Date dueDate = sdf.parse(task.getDueDate());  // Parse the date from the task object
            if (dueDate != null) {
                calendar.setTime(dueDate);  // Set the parsed due date to the Calendar object
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        // Create PendingIntent to trigger the NotificationReceiver
        PendingIntent pendingIntent = getTaskPendingIntent(context, task.getId());

        // Set the alarm using AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            // Use setExact() to trigger the alarm at the exact time
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

}
