package com.example.taskmanagertest;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class TaskFragment extends Fragment implements View.OnClickListener, DialogTime.DialogListener {

    private Task task;

    private EditText title;
    private EditText text;
    private RadioButton done;

    private ImageView imgPriority0;
    private ImageView imgPriority1;
    private ImageView imgPriority2;

    public static TaskFragment newInstance() {
        return new TaskFragment();
    }

    public static TaskFragment newInstance(Task task) {
        TaskFragment fragment = new TaskFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("task", task);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("task")) {
            task = (Task) getArguments().getSerializable("task");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_task, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        title = view.findViewById(R.id.et_title);
        text = view.findViewById(R.id.et_text);
        imgPriority0 = view.findViewById(R.id.priority_0);
        imgPriority1 = view.findViewById(R.id.priority_1);
        imgPriority2 = view.findViewById(R.id.priority_2);
        done = view.findViewById(R.id.done_rb);

        view.findViewById(R.id.bt_save).setOnClickListener(this);
        view.findViewById(R.id.bt_notification).setOnClickListener(this);
        done.setOnClickListener(this);
        imgPriority0.setOnClickListener(this);
        imgPriority1.setOnClickListener(this);
        imgPriority2.setOnClickListener(this);

        if (task == null) {
            task = new Task();
        }

        init();
        setBackgroundTint();

        return view;
    }

    private void init() {
        title.setText(task.getTitle());
        text.setText(task.getText());
        done.setChecked(task.isDone());
        setPriority();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setTitle("");
            ((MainActivity) getActivity()).showBackButton(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save: {
                saveTask();
                if (getActivity() != null && getActivity() instanceof MainActivity) {
                    getActivity().onBackPressed();
                }
                break;
            }
            case R.id.priority_0: {
                task.setPriority(0);
                setPriority();
                break;
            }
            case R.id.priority_1: {
                task.setPriority(1);
                setPriority();
                break;
            }
            case R.id.priority_2: {
                task.setPriority(2);
                setPriority();
                break;
            }
            case R.id.bt_notification: {
                DialogTime dialogFragment = new DialogTime();
                dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
                dialogFragment.setListener(TaskFragment.this);
                if (getFragmentManager() != null)
                    dialogFragment.show(getFragmentManager(), "AddStepDialogFragment");
                break;
            }
            case R.id.done_rb: {
                task.setDone(done.isChecked());
                break;
            }
        }
    }


    private void setPriority() {
        clearPriority();
        switch (task.getPriority()) {
            case 0: {
                imgPriority0.setImageResource(R.drawable.ic_check);
                break;
            }
            case 1: {
                imgPriority1.setImageResource(R.drawable.ic_check);
                break;
            }
            case 2: {
                imgPriority2.setImageResource(R.drawable.ic_check);
                break;
            }

        }
    }

    private void clearPriority() {
        imgPriority0.setImageResource(0);
        imgPriority1.setImageResource(0);
        imgPriority2.setImageResource(0);
    }

    @Override
    public void setTime(long time) {
        task.setTime(time);
    }

    private void saveTask() {
        task.setTitle(title.getText().toString());
        task.setText(text.getText().toString());
        MyRealm.getInstance().addOrUpdateTask(task);


        if (task.getTime() != 0){
            if (getActivity() != null && getActivity() instanceof MainActivity) {

                ((MainActivity) getActivity()).scheduleNotification(NotificationPublisher.createNotification(task), task.getTime());
            }
        }
    }

    //bg tint for api 19
    private void setBackgroundTint() {
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            setColor(imgPriority0, R.color.priority_0);
            setColor(imgPriority1, R.color.priority_1);
            setColor(imgPriority2, R.color.priority_2);
        }
    }

    private void setColor(ImageView imageView, int color) {
        Drawable drawable = ResourcesCompat.getDrawable(App.getAppContext().getResources(), R.drawable.button_circle, null);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, App.getAppContext().getResources().getColor(color));
        imageView.setBackground(drawable);
    }

}
