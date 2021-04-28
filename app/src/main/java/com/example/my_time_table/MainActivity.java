package com.example.my_time_table;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
    private TextView weekDay;

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

        // Задаём номер начальное недели
        weeklyNumber.setText("1");

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
            case "воскресенье":
                weekDay.setText("Воскресенье");
                break;
        }
    }

    void createScreenSlide() {
        // Создаём pageAdapter и запускаем viewPager
        Log.i("LiveDataSize", "Запуск viewpager");
        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(fragmentManager, lifecycle);
        viewPager2.setAdapter(pagerAdapter);
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
                    Log.i("LiveDataSize", "size перед запуском " + week1.size());
                    return new ScheduleFragment();
                case 1:
                    return new ScheduleFragment2();
            }
            return null;
        }

        @Override
        public long getItemId ( int position) {

            switch (position) {
                case 0:
                    try {
                        // Автопролистывание
                        ScheduleFragment.getRecyclerView().post(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 1; i < DataFromFB.getNextDay1().size(); i++) {
//                                    if (weekDay.getText()
//                                            .equals(ttb.timeTableDao()
//                                                    .getById1Week(nextDay1.get(i) + 1).getDay())) {
//                                        ScheduleFragment.getRecyclerView()
//                                                .smoothScrollToPosition(nextDay1.get(i) + 2);
//                                    }
//                                    if (weekDay.getText()
//                                            .equals(week1.get(nextDay1.get(i) + 1).getDay())) {
//                                        ScheduleFragment.getRecyclerView()
//                                                .smoothScrollToPosition(nextDay1.get(i) + 2);
//                                    }
                                }
                            }
                        });
                    } catch (Exception ignore) {

                    }
                    weeklyNumber.setText(String.valueOf(position + 1));

                case 1:
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