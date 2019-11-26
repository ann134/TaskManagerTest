package com.example.taskmanagertest;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class DialogTime extends DialogFragment {

    private DialogListener listener;
    private TimePicker timePicker;

    public interface DialogListener {
        void setTime(long time);
    }

    public void setListener(DialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_time, null);

        timePicker = view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        view.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                now.setTimeInMillis(System.currentTimeMillis());
                //Log.e("time", now.getTime().toString());
                now.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                now.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                now.set(Calendar.SECOND, 0);
                //Log.e("time", now.getTime().toString());
                listener.setTime(now.getTimeInMillis() - System.currentTimeMillis());

                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        Dialog dialog = builder.create();
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;

    }
}