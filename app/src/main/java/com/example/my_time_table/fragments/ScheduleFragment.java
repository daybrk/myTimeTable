package com.example.my_time_table.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_time_table.MainActivity;
import com.example.my_time_table.R;
import com.example.my_time_table.RecyclerAdapter;
import com.example.my_time_table.fragments.dummy.ScheduleData;

public class ScheduleFragment extends Fragment {

    public static RecyclerView recyclerView;

    public ScheduleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_list, container, false);

        // Устанавливаем адаптер.
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new RecyclerAdapter(ScheduleData.ITEMS));
            MainActivity.scrollToItem();
        }
        return view;
    }
}