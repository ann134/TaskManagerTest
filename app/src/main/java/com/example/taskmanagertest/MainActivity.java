package com.example.taskmanagertest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //actionbar
    private TextView actionBarTitle;
    private LinearLayout btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //actionbar
        actionBarTitle = findViewById(R.id.toolbar_title);
        btnBack = findViewById(R.id.button_back_layout);
        btnBack.setOnClickListener(this);

        if (savedInstanceState == null) {
            loadFragment(TaskListFragment.newInstance());
        }

        if (getIntent().hasExtra("task")) {
            Task task = (Task) getIntent().getSerializableExtra("task");
            loadFragment(TaskFragment.newInstance(task));
        }
    }

    private void loadFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back_layout:
                onBackPressed();
                break;
        }
    }

    public void setTitle(String title) {
        actionBarTitle.setText(title);
    }

    public void showBackButton(boolean show) {
        if (show)
            btnBack.setVisibility(View.VISIBLE);
        else
            btnBack.setVisibility(View.GONE);
    }


    public void scheduleNotification(Notification notification,  long futureInMillis) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMilliss = SystemClock.elapsedRealtime() + futureInMillis;
        //Log.e("time1", futureInMillis+"");
        //Log.e("time2", futureInMilliss+"");
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMilliss, pendingIntent);
    }

}
