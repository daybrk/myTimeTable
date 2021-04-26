package com.example.my_time_table;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.my_time_table.database.TimeTableDatabase;
import com.example.my_time_table.fragments.ScheduleFragment;
import com.example.my_time_table.fragments.ScheduleFragment2;
import com.example.my_time_table.time_table_pojos.CheckDay;
import com.example.my_time_table.time_table_pojos.PartOfTimeTable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    static ViewPager2 viewPager2;
    private static TextView weeklyNumber;

    static FirebaseDatabase mDataBase;
    static DatabaseReference ref;
    static SharedPreferences sharedPreferences;
    static FragmentManager fragmentManager;
    static Lifecycle lifecycle;

    // Кл-во листаемых страниц
    private static final int NUM_PAGES = 2;
    //
    public static int dayOfWeek;
    // Хранит версию локальной (room) БД
    static float version = 0;
    // Хранит все рабочие дни
    static String[] daysWeek = new String[] {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};
    // Список для хранения индексов "нового" дня
    public static ArrayList<Integer> nextDay = new ArrayList<>();
    // Индекс "нового" дня
    static int day;
    // Экземпляр Базы данных
    static TimeTableDatabase ttb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        init();

        //TODO: доделать определение текущего дня недели

        // Calendar c = Calendar.getInstance();
        // c.setFirstDayOfWeek(Calendar.MONDAY);
        // Log.i("DAY", String.valueOf(c.getFirstDayOfWeek()));
        // dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 2;
        // Log.i("DAYS", String.valueOf(dayOfWeek));
    }

    void init() {

        // Инициализируем переменные
        sharedPreferences = this.getSharedPreferences("com.example.my_time_table", Context.MODE_PRIVATE);
        fragmentManager = getSupportFragmentManager();
        lifecycle = getLifecycle();
        // Инициализируем переменные
        viewPager2 = (ViewPager2) findViewById(R.id.viewpager);
        weeklyNumber = (TextView) findViewById(R.id.weekly_number);

        // Устанавливаем номер начальное недели
        weeklyNumber.setText("1");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Получаем значение версии room ДБ, если её нет (первый запуск), то version = 0
        version = sharedPreferences.getFloat("Version", 0);

        // Log.i("Version", "Значение переменной в onStart " + version);

        // Инициализируем переменные и задаём начальный путь для ref
        ttb = TimeTableDatabase.getDatabase(getApplicationContext());
        mDataBase = FirebaseDatabase.getInstance();
        ref = mDataBase.getReference("Group/БИМ18-01");

        // Запускаем метод для получения данных из FireBase и запуска фрагементов / либо если они
        // уже есть получения всех индексов "нового" дня и запуска фрагементов
        getDataFromDB();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Сохраняем значение версии локальной БД
        sharedPreferences.edit().putFloat("Version", version).apply();

        Log.i("Version", "Значение переменной в onStop " + sharedPreferences.getFloat("Version", 0));
    }

    static void createScreenSlide() {
        // Создаём pageAdapter и запускаем viewPager
        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(fragmentManager, lifecycle);
        viewPager2.setAdapter(pagerAdapter);
    }

    public static void getDataFromDB() {

        Log.i("Version", "Начальное значение переменной " + String.valueOf(version));

        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Проверка на совпадение локальной версии БД и версии из FireBase
                if (Double.parseDouble(String.valueOf(dataSnapshot.child("/Version")
                        .getValue())) == version) {
                    // Добавляем в nextDay первый индекс "нового" дня который всегда по умолчанию =  0
                    nextDay.add(0);
                    // Получаем из room БД все значения "нового" дня и записываем их в список
                    for (int i = 0; i < ttb.timeTableDao().LoadAllDaysNumber().size(); i++) {
                        nextDay.add(ttb.timeTableDao().LoadAllDaysNumber().get(i).getNum());
                    }
                } else {
                    // Сохраняем значение версии БД
                    version = (float) Double.parseDouble(String.valueOf(dataSnapshot.child("/Version").getValue()));

                    // Log.i("Version", "Значение переменной обновляется " + version);

                    // Добавляем в nextDay первый индекс "нового" дня который всегда по умолчанию =  0
                    nextDay.add(0);
                    for (String s : daysWeek) {
                        DataSnapshot ds = dataSnapshot.child("/Week" + "/" + weeklyNumber.getText() + "/Days" + "/" + s + "/Sub");
                        for (DataSnapshot ds1 : ds.getChildren()) {
                            for (DataSnapshot ds2 : ds1.getChildren()) {
                                // Создаём новую сущность
                                PartOfTimeTable part = new PartOfTimeTable();
                                // Задаём ей номер недели
                                part.setWeek(String.valueOf(weeklyNumber.getText()));
                                // Задаём ей день
                                part.setDay(s);
                                // Задаём ей название предмета
                                part.setSubject(ds2.getKey());
                                // Задаём ей номер аудитории
                                part.setAudience(String.valueOf(ds2.child("Aud").getValue()));
                                // Задаём ей подгруппу
                                part.setSubGroup(String.valueOf(ds2.child("SubGroup").getValue()));
                                // Задаём ей преподователя
                                part.setTeacher(String.valueOf(ds2.child("Teach").getValue()));
                                // Задаём ей временной промежуток
                                part.setClassHour(String.valueOf(ds2.child("Time").getValue()));
                                // Задаём ей тип предмета
                                part.setType(String.valueOf(ds2.child("Type").getValue()));
                                // Добавляем сущность в БД
                                ttb.timeTableDao().insertPartOfTimeTable(part);
                                // Ищем индекс "нового" дня для добавления в БД/Списка
                                day++;
                            }
                        }

                        // Создаём новую сущность для хранения индекса,
                        // в котором начинается новый день в расписании для дальнейшего использование
                        // при отсутсвии изменений в БД
                        CheckDay checkDay = new CheckDay();
                        // Задаём этот самый индекс "нового" дня
                        checkDay.setNum(day);
                        // Добавляем сущность в БД
                        ttb.timeTableDao().insertDaysNumber(checkDay);

                        nextDay.add(day);
                    }
                }
                // Устанавливаем ScreenSlidePagerAdapter и создаём фрагменты.
                createScreenSlide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addValueEventListener(vListener);
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
                case 1:
                    try {
                        //TODO: Доделать автопролистывание

                        // Автопролистывание
//                        ScheduleFragment.recyclerView.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                int d;
//                                try {
//                                    // В зависимости от дня недели получаем id нужного элемента.
//                                    d = ScheduleData.numbers[(dayOfWeek) + 1] - 1;
//                                } catch (ArrayIndexOutOfBoundsException e) {
//                                    d = ScheduleData.ITEMS.size() - 1;
//                                }
//                                if (d != 0) {
//                                    // TODO: НУЖНО ЛИ?
////                                    d = Integer.parseInt(ScheduleData.ITEMS.get(d).id);
////                                    // Используем auto-scroll к нужному элементу.
////                                    ScheduleFragment.recyclerView.smoothScrollToPosition(d);
//                                }
//                            }
//                        });
                    } catch (Exception ignore) {

                    }

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


    public static void scrollToItem () {}

    public Context getMainContext() {
        return this;
    }

}