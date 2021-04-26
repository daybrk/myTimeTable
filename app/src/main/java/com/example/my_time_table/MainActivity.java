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
import com.example.my_time_table.time_table_pojos.TimeTableWeek2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {


    static ViewPager2 viewPager2;
    private static TextView weeklyNumber;
    private TextView numberDay;
    private static TextView weekDay;

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
    public static ArrayList<Integer> nextDay1 = new ArrayList<>();

    public static ArrayList<Integer> nextDay2 = new ArrayList<>();
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

    }

    void init() {

        // Инициализируем переменные
        sharedPreferences = this.getSharedPreferences("com.example.my_time_table", Context.MODE_PRIVATE);
        fragmentManager = getSupportFragmentManager();
        lifecycle = getLifecycle();
        // Инициализируем переменные
        viewPager2 = (ViewPager2) findViewById(R.id.viewpager);
        weeklyNumber = (TextView) findViewById(R.id.weekly_number);
        numberDay = (TextView) findViewById(R.id.number_day);
        weekDay = (TextView) findViewById(R.id.weekday);
        // Устанавливаем номер начальное недели
        weeklyNumber.setText("1");

        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "день.месяц.год" и дня недели
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("EEEЕ");
        setTextWeekDay(sdf, currentDate, dateFormat);
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
            case "среда":
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

    @Override
    protected void onStart() {
        super.onStart();
        // Получаем значение версии room ДБ, если её нет (первый запуск), то version = 0
        version = sharedPreferences.getFloat("Version", 0);

         Log.i("Version", "Значение переменной в onStart " + version);

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
                    nextDay1.add(0);
                    nextDay2.add(0);
                    // Получаем из room БД все значения "нового" дня и записываем их в список
                    int i = 0;
                    while (ttb.timeTableDao().LoadAllDaysNumber().get(i).getNum() != 999) {
                        nextDay1.add(ttb.timeTableDao().LoadAllDaysNumber().get(i).getNum());
                        i++;
                    }

                    i++;
                    while (i != ttb.timeTableDao().LoadAllDaysNumber().size()) {
                        nextDay2.add(ttb.timeTableDao().LoadAllDaysNumber().get(i).getNum());
                        i++;
                    }

                } else {
                    // Сохраняем значение версии БД
                    version = (float) Double.parseDouble(String.valueOf(dataSnapshot.child("/Version").getValue()));

                    // Log.i("Version", "Значение переменной обновляется " + version);

                    // Добавляем в nextDay первый индекс "нового" дня который всегда по умолчанию =  0
                    nextDay1.add(0);
                    nextDay2.add(0);

                    for (int i = 1; i < 3; i++) {
                        DataSnapshot ds0 = dataSnapshot.child("/Week" + "/" + i);
                        for (String s : daysWeek) {
                            DataSnapshot ds;
                            if (i == 1) {
                                if (ds0.child("/Days" + "/" + s + "/Sub").getValue() == null) {
                                    continue;
                                }else {
                                    ds = ds0.child("/Days" + "/" + s + "/Sub");
                                }
                            } else {
                                if (ds0.child("/" + s + "/Sub").getValue() == null) {
                                    continue;
                                }else {
                                    ds = ds0.child("/" + s + "/Sub");
                                }
                            }

                            for (DataSnapshot ds1 : ds.getChildren()) {
                                for (DataSnapshot ds2 : ds1.getChildren()) {
                                    // Создаём новую сущность
                                    addDataToRoomDB(ds2, new PartOfTimeTable(),
                                            new TimeTableWeek2(), s, i);
                                    // Ищем индекс "нового" дня для добавления в БД/Списка
                                    day++;
//                                    Log.i("nextDay", "в цикле " + String.valueOf(day));
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
                            if (i == 1) {
//                                Log.i("nextDay", "добавление в лист " + String.valueOf(day));
                                nextDay1.add(day);
                            } else {
                                nextDay2.add(day);
                            }
//                            Log.i("nextDay", "новая иттерация " + String.valueOf(day));
                        }

                        if (i == 1) {
                            ttb.timeTableDao().delete(ttb.timeTableDao().getByIdDay(ttb.timeTableDao().LoadAllDaysNumber().size()));
                            nextDay1.remove(nextDay1.size() - 1);
                            CheckDay checkDay = new CheckDay();
                            checkDay.setNum(999);
                            ttb.timeTableDao().insertDaysNumber(checkDay);

                            day = 0;
                        }
                    }
                    ttb.timeTableDao().delete(ttb.timeTableDao().getByIdDay(ttb.timeTableDao().LoadAllDaysNumber().size()));
                    nextDay2.remove(nextDay2.size() - 1);
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

    static void addDataToRoomDB(DataSnapshot ds2, PartOfTimeTable part, TimeTableWeek2 week2, String s, int i) {

        if (i == 1) {
            // Задаём ей номер недели
            part.setWeek(String.valueOf(i));
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
        } else if (i == 2) {
            // Задаём ей номер недели
            week2.setWeek(String.valueOf(i));
            // Задаём ей день
            week2.setDay(s);
            // Задаём ей название предмета
            week2.setSubject(ds2.getKey());
            // Задаём ей номер аудитории
            week2.setAudience(String.valueOf(ds2.child("Aud").getValue()));
            // Задаём ей подгруппу
            week2.setSubGroup(String.valueOf(ds2.child("SubGroup").getValue()));
            // Задаём ей преподователя
            week2.setTeacher(String.valueOf(ds2.child("Teach").getValue()));
            // Задаём ей временной промежуток
            week2.setClassHour(String.valueOf(ds2.child("Time").getValue()));
            // Задаём ей тип предмета
            week2.setType(String.valueOf(ds2.child("Type").getValue()));
            // Добавляем сущность в БД
            ttb.timeTableDao().insertTimeTableWeek2(week2);
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
                                for (int i = 1; i < nextDay1.size(); i++) {
                                    if (weekDay.getText()
                                            .equals(ttb.timeTableDao()
                                                    .getById1Week(nextDay1.get(i) + 1).getDay())) {
                                        ScheduleFragment.getRecyclerView()
                                                .smoothScrollToPosition(nextDay1.get(i) + 2);
                                    }
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


    public static void scrollToItem () {}

    public Context getMainContext() {
        return this;
    }

}