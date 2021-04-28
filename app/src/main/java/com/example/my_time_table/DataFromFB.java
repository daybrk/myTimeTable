package com.example.my_time_table;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.my_time_table.time_table_pojos.CheckDay;
import com.example.my_time_table.time_table_pojos.PartOfTimeTable;
import com.example.my_time_table.time_table_pojos.TimeTableWeek2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataFromFB {

    // Список для хранения индексов "нового" дня
    private static final ArrayList<Integer> nextDay1 = new ArrayList<>();
    private static final ArrayList<Integer> nextDay2 = new ArrayList<>();

    // Индекс "нового" дня
    private static int day;

    private static int start1;
    private static int start2;

    // Хранит все рабочие дни
    private static final String[] daysWeek = new String[] {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};


    public static void getDataFromDB() {

        Log.i("Version", "Начальное значение переменной " + MainActivity.getVersion());

        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Проверка на совпадение локальной версии БД и версии из FireBase
                if (Double.parseDouble(String.valueOf(dataSnapshot.child("/Version")
                        .getValue())) == MainActivity.getVersion()) {
                    // Добавляем в nextDay первый индекс "нового" дня который всегда по умолчанию =  0
                    nextDay1.add(0);
                    nextDay2.add(0);
                    // Получаем из room БД все значения "нового" дня и записываем их в список
                    int i = 0;
                    while (MainActivity.getCheck().get(i).getNum() != 999) {
                        nextDay1.add(MainActivity.getCheck().get(i).getNum());
                        i++;
                    }
                    i++;
                    while (i != MainActivity.getCheck().size()) {
                        nextDay2.add(MainActivity.getCheck().get(i).getNum());
                        i++;
                    }

                    MainActivity mainActivity = new MainActivity();
                    mainActivity.createScreenSlide();

                } else {
                    // Сохраняем значение версии БД
                    MainActivity.setVersion((float) Double.parseDouble(String.valueOf(dataSnapshot.child("/Version").getValue())));

                    try {
                        MainActivity.getViewModel().deleteWeek1();
                        MainActivity.getViewModel().deleteWeek2();
                    } catch (Exception ignored) {

                    }

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
                            MainActivity.getViewModel().insertCheck(checkDay);
                            if (i == 1) {
//                                Log.i("nextDay", "добавление в лист " + String.valueOf(day));
                                nextDay1.add(day);
                            } else {
                                nextDay2.add(day);
                            }
//                            Log.i("nextDay", "новая иттерация " + String.valueOf(day));
                        }

                        if (i == 1) {
                            nextDay1.remove(nextDay1.size() - 1);
                            CheckDay checkDay = new CheckDay();
                            checkDay.setNum(999);
//                            ttb.timeTableDao().insertDaysNumber(checkDay);
                            MainActivity.getViewModel().insertCheck(checkDay);
                            start1 = day;
                            day = 0;
                        }
                    }
                    nextDay2.remove(nextDay2.size() - 1);
                }
                start2 = day;
                // Устанавливаем ScreenSlidePagerAdapter и создаём фрагменты.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        SetLifecycle.getRef().addValueEventListener(vListener);
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
            MainActivity.getViewModel().insertPart(part);
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
            MainActivity.getViewModel().insertWeek2(week2);
        }

    }

    public static ArrayList<Integer> getNextDay1() {
        return nextDay1;
    }

    public static ArrayList<Integer> getNextDay2() {
        return nextDay2;
    }

    public static int getStart1() {
        return start1;
    }

    public static int getStart2() {
        return start2;
    }
}
