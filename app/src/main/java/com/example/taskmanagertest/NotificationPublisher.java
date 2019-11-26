package com.example.taskmanagertest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class NotificationPublisher extends BroadcastReceiver {


        public static String NOTIFICATION_ID = "notification-id";
        public static String NOTIFICATION = "notification";

        public void onReceive(Context context, Intent intent) {

            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notification = intent.getParcelableExtra(NOTIFICATION);
            int id = intent.getIntExtra(NOTIFICATION_ID, 0);

            String channelId = context.getString(R.string.app_name);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setVibrationPattern(new long[]{300, 100 ,40, 400, 10, 10 ,10});
                manager.createNotificationChannel(channel);
            }
            try {
                manager.notify("test",14, notification);
            } catch (Exception e) {
                Log.e("Notification Msg", "Not create native notification");
            }

        }


        public static Notification createNotification(Task task){
            String channelId = App.getAppContext().getString(R.string.app_name);
            String content = App.getAppContext().getString(R.string.deadline);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(App.getAppContext(), channelId);

            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle(channelId);
            builder.setContentText(content);

            //intent
            Intent notificationIntent = new Intent(App.getAppContext(), MainActivity.class);
            notificationIntent.putExtra("task", task);
            PendingIntent contentIntent = PendingIntent.getActivity(App.getAppContext(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(contentIntent);
            builder.setAutoCancel(true);

            return builder.build();
        }

}
