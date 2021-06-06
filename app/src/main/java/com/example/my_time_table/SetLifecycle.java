package com.example.my_time_table;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetLifecycle implements LifecycleObserver, LifecycleOwner {

    static FirebaseDatabase mDataBase;
    static DatabaseReference ref;

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void initViewModel() {
        // Получаем значение версии room ДБ, если её нет (первый запуск), то version = 0
        MainActivity.setVersion(MainActivity.getSharedPreferences().getFloat("Version", 0));

        Log.i("Version", "Значение переменной в onStart " + MainActivity.getVersion());

        // Инициализируем переменные, устанавливаем наблюдателей и задаём начальный путь для ref
        MainActivity.getViewModel().getAllCheck().observe(this,
                checkDays -> MainActivity.setCheck(checkDays));
        MainActivity.getViewModel().getAllPart().observe(this,
                partOfTimeTables -> MainActivity.setWeek1(partOfTimeTables));
        MainActivity.getViewModel().getAllWeek2().observe(this, timeTableWeek2s -> {
            MainActivity.setWeek2(timeTableWeek2s);
            // Ждём пока liveData полностью обновится, после чего запускаем фрагменты
            if (timeTableWeek2s.size() == DataFromFB.getStart2()) {
                MainActivity mainActivity = new MainActivity();
                mainActivity.createScreenSlide();
            }else if (MainActivity.getCheck().size() != 0) {
                MainActivity mainActivity = new MainActivity();
                mainActivity.createScreenSlide();
            }
        });

        mDataBase = FirebaseDatabase.getInstance();
        ref = mDataBase.getReference("Group/БИМ18-01");

        // Запускаем метод для получения данных из FireBase и запуска фрагементов / либо если они
        // уже есть получения всех индексов "нового" дня и запуска фрагементов
        DataFromFB.getDataFromDB();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void savePref() {
        // Сохраняем значение версии локальной БД
        MainActivity.getSharedPreferences().edit().putFloat("Version",
                MainActivity.getVersion()).apply();

        Log.i("Version", "Значение переменной в onStop " +
                MainActivity.getSharedPreferences().getFloat("Version", 0));
    }

    public static DatabaseReference getRef() {
        return ref;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return MainActivity.lifecycle;
    }
}
