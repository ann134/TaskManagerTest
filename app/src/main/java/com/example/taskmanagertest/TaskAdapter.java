package com.example.taskmanagertest;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskItemHolder> {

    private OnTaskItemClick listener;
    private List<Task> taskList;

    public interface OnTaskItemClick {
        void onItemClick(Task task);
    }

    public TaskAdapter(OnTaskItemClick listener, List<Task> taskList) {
        this.listener = listener;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskItemHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new TaskItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskItemHolder holder, final int i) {
        final Task task = taskList.get(i);

        holder.title.setText(task.getTitle());
        holder.text.setText(task.getText());
        setColor(holder.priority, getColorByPriority(task.getPriority()));

        holder.done.setBackgroundColor(task.isDone()? App.getAppContext().getResources().getColor(R.color.done_bg) : App.getAppContext().getResources().getColor(R.color.white));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(task);
            }
        });
    }

    private void setColor(FrameLayout frameLayout, int color){
        Drawable drawable = ResourcesCompat.getDrawable(App.getAppContext().getResources(), R.drawable.bg_card_view, null);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, App.getAppContext().getResources().getColor(color));
        frameLayout.setBackground(drawable);
    }

    private int getColorByPriority(int priority){
        switch (priority) {
            case 0: {
                return R.color.priority_0;
            }
            case 1: {
                return R.color.priority_1;
            }
            case 2: {
                return R.color.priority_2;
            }
            default:{
                return R.color.priority_0;
            }
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    class TaskItemHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView text;
        private FrameLayout priority;
        private LinearLayout done;

        private TaskItemHolder(@NonNull View v) {
            super(v);
            title = v.findViewById(R.id.title);
            text = v.findViewById(R.id.text);
            priority = v.findViewById(R.id.priority_layout);
            done = v.findViewById(R.id.done_layout);
        }
    }
}
