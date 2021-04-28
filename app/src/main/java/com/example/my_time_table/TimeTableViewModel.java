package com.example.my_time_table;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.my_time_table.time_table_pojos.CheckDay;
import com.example.my_time_table.time_table_pojos.PartOfTimeTable;
import com.example.my_time_table.time_table_pojos.TimeTableWeek2;

import java.util.List;

public class TimeTableViewModel extends AndroidViewModel {


    private final LiveData<List<PartOfTimeTable>> part;
    private final LiveData<List<TimeTableWeek2>> week2;
    private final LiveData<List<CheckDay>> check;

    TimeTableRepository repository;

    public TimeTableViewModel(@NonNull Application application) {
        super(application);
        repository = new TimeTableRepository(application);
        part = repository.getAllPart();
        week2 = repository.getAllWeek2();
        check = repository.getAllCheck();
    }


    LiveData<List<PartOfTimeTable>> getAllPart() {
        return part;
    }

    LiveData<List<TimeTableWeek2>> getAllWeek2() {
        return week2;
    }

    LiveData<List<CheckDay>> getAllCheck() {
        return check;
    }

    void insertPart (final PartOfTimeTable partOfTimeTable) {
        repository.insertPart(partOfTimeTable);
    }

    void insertWeek2 (final TimeTableWeek2 timeTableWeek2) {
        repository.insertWeek2(timeTableWeek2);
    }

    void insertCheck (final CheckDay checkDay) {
       repository.insertCheck(checkDay);
    }

    void deleteWeek1() {
        repository.deleteWeek1();
    }

    void deleteWeek2() {
        repository.deleteWeek2();
    }

    void deleteDay (CheckDay checkDay) {
        repository.deleteDay(checkDay);
    }

}
