package com.example.taskmanagertest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListFragment extends Fragment implements TaskAdapter.OnTaskItemClick, View.OnClickListener {

    //private List<Task> tasks;
    private RecyclerView recyclerView;
    private TextView emptyListText;

    public static TaskListFragment newInstance() {
        return new TaskListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_task_list, container, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        recyclerView = v.findViewById(R.id.recicler_view);
        LinearLayoutManager manager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(manager);

        emptyListText = v.findViewById(R.id.emptylist_text);
        ImageView newTask = v.findViewById(R.id.new_item);

        newTask.setOnClickListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        getTasks();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setTitle(getString(R.string.my_tasks));
            ((MainActivity) getActivity()).showBackButton(false);
        }
    }

    private void getTasks() {
        List<Task> tasks = MyRealm.getInstance().getTaskList();
        initRecyclerAdapter(tasks);
    }

    private void initRecyclerAdapter(List<Task> tasks) {
        if (tasks.size() == 0)
            emptyListText.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(new TaskAdapter(this, tasks));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_item: {
                loadFragment(TaskFragment.newInstance());
                break;
            }
        }
    }

    @Override
    public void onItemClick(Task task) {
        loadFragment(TaskFragment.newInstance(task));
    }

    private void loadFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
