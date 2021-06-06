package com.example.my_time_table;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.my_time_table.fragments.ScheduleFragment;
import com.example.my_time_table.fragments.ScheduleFragment2;
import com.example.my_time_table.time_table_pojos.CheckDay;
import com.example.my_time_table.time_table_pojos.PartOfTimeTable;
import com.example.my_time_table.time_table_pojos.TimeTableWeek2;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    static ViewPager2 viewPager2;
    private static TextView weeklyNumber;
    private TextView numberDay;
    private static TextView weekDay;

    private static SharedPreferences sharedPreferences;
    static FragmentManager fragmentManager;
    static Lifecycle lifecycle;

    // Кл-во листаемых страниц 
    private static final int NUM_PAGES = 2;

    // Хранит версию локальной (room) БД
    static float version = 0;

    private static TimeTableViewModel viewModel;
    private static List<PartOfTimeTable> week1;
    private static List<TimeTableWeek2> week2;
    private static List<CheckDay> check;

    private static Fragment shFrag = new ScheduleFragment();
    private static Fragment shFrag2 = new ScheduleFragment2();

    public Boolean landOrientation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    void init() {

        // Инициализируем переменные
        viewModel = new ViewModelProvider(this).get(TimeTableViewModel.class);
        sharedPreferences = this.getSharedPreferences("com.example.my_time_table", Context.MODE_PRIVATE);
        fragmentManager = getSupportFragmentManager();

        SetLifecycle setLifecycle = new SetLifecycle();

        lifecycle = getLifecycle();
        lifecycle.addObserver(setLifecycle);

        viewPager2 = findViewById(R.id.viewpager);
        numberDay = findViewById(R.id.number_day);
        weekDay = findViewById(R.id.weekday);
        weeklyNumber = findViewById(R.id.weekly_number);



        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "день.месяц.год" и дня недели
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("EEЕЕ");
        // Задаём текущий день
        setTextWeekDay(sdf, currentDate, dateFormat);
//        weekDay.setText(sdf.format(currentDate));
    }

    void setTextWeekDay(SimpleDateFormat sdf, Date currentDate, DateFormat dateFormat) {

        numberDay.setText(dateFormat.format(currentDate));

        switch (sdf.format(currentDate)) {
            case "понедельник":
                weekDay.setText("Понедельник");
                break;
            case "вторник":
                weekDay.setText("Вторник");
                break;
            case "срЕЕ":
                weekDay.setText("Среда");
                break;
            case "четверг":
                weekDay.setText("Четверг");
                break;
            case "пятница":
                weekDay.setText("Пятница");
                break;
            case "суббота":
                weekDay.setText("Суббота");
                break;
            case "всЕЕ":
                weekDay.setText("Воскресенье");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        ConstraintLayout constraintLayout = findViewById(R.id.constraint);
        switch(id){
            case R.id.white:
                constraintLayout.setBackgroundColor(Color.WHITE);
                weeklyNumber.setTextColor(Color.BLACK);
                weekDay.setTextColor(Color.BLACK);
                numberDay.setTextColor(Color.BLACK);
                return true;
            case R.id.black:
                constraintLayout.setBackgroundColor(Color.parseColor("#434343"));
                weeklyNumber.setTextColor(Color.parseColor("#E6E6E6"));
                weekDay.setTextColor(Color.parseColor("#E6E6E6"));
                numberDay.setTextColor(Color.parseColor("#E6E6E6"));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void createScreenSlide() {
        // Создаём pageAdapter и запускаем viewPager
        Log.i("LiveDataSize", "Запуск viewpager");
        try {
            // Задаём номер начальное недели
            weeklyNumber.setText("1");
            FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(fragmentManager, lifecycle);
            viewPager2.setAdapter(pagerAdapter);
        } catch (NullPointerException e) {
            weeklyNumber.setText(R.string.week_2);
            fragmentManager.beginTransaction()
                    .replace(R.id.fr_cont1, shFrag)
                    .commit();
            fragmentManager.beginTransaction()
                    .replace(R.id.fr_cont2, shFrag2)
                    .commit();
        }

    }


    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Создаём фрагмент
            switch (position) {
                case 0:
                    return shFrag;
                case 1:
                    return shFrag2;
            }
            return null;
        }

        @Override
        public long getItemId ( int position) {

            switch (position) {
                case 0:
//                    try {
                        // Автопролистывание
//                        ScheduleFragment.getRecyclerView().post(() -> {
//                            for (int i = 1; i < DataFromFB.getNextDay1().size() - 1; i++) {
//                                if (weekDay.getText()
//                                        .equals(week1.get(DataFromFB.getNextDay1().get(i)).getDay())) {
//                                    ScheduleFragment.getRecyclerView()
//                                            .smoothScrollToPosition(DataFromFB.getNextDay1().get(i) + 2);
//                                }
//                            }
//                        });
//                    } catch (Exception ignore) {
//
//                    }
                    weeklyNumber.setText(String.valueOf(position + 1));

                case 1:
//                    try {
//                        // Автопролистывание
//                        ScheduleFragment2.getRecyclerView().post(() -> {
//                            for (int i = 1; i < DataFromFB.getNextDay2().size() - 1; i++) {
//                                if (weekDay.getText()
//                                        .equals(week2.get(DataFromFB.getNextDay2().get(i)).getDay())) {
//                                    ScheduleFragment2.getRecyclerView()
//                                            .smoothScrollToPosition(DataFromFB.getNextDay2().get(i) + 2);
//                                }
//                            }
//                        });
//                    } catch (Exception ignore) {
//
//                    }
                    // С пролистыванием, меняем значение недели.
                    weeklyNumber.setText(String.valueOf(position + 1));
                    break;
            }
            return super.getItemId(position);
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }


    public static float getVersion() {
        return version;
    }

    public static void setVersion(float version) {
        MainActivity.version = version;
    }

    public static TimeTableViewModel getViewModel() {
        return viewModel;
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static List<PartOfTimeTable> getWeek1() {
        return week1;
    }

    public static void setWeek1(List<PartOfTimeTable> week1) {
        MainActivity.week1 = week1;
    }

    public static List<TimeTableWeek2> getWeek2() {
        return week2;
    }

    public static void setWeek2(List<TimeTableWeek2> week2) {
        MainActivity.week2 = week2;
    }

    public static List<CheckDay> getCheck() {
        return check;
    }

    public static void setCheck(List<CheckDay> check) {
        MainActivity.check = check;
    }


}