package com.example.my_time_table;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.my_time_table.database.TimeTableDAO;
import com.example.my_time_table.database.TimeTableDatabase;
import com.example.my_time_table.time_table_pojos.CheckDay;
import com.example.my_time_table.time_table_pojos.PartOfTimeTable;
import com.example.my_time_table.time_table_pojos.TimeTableWeek2;

import java.util.List;

public class TimeTableRepository {

    private final TimeTableDAO dao;
    private final LiveData<List<PartOfTimeTable>> part;
    private final LiveData<List<TimeTableWeek2>> week2;
    private final LiveData<List<CheckDay>> check;

    public TimeTableRepository(Application application) {
        TimeTableDatabase ttb = TimeTableDatabase.getDatabase(application);
        dao = ttb.timeTableDao();
        part = dao.LoadAllPartOfTimeTable();
        week2 = dao.LoadAllTimeTableWeek2();
        check = dao.LoadAllDaysNumber();
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

    void insertPart(final PartOfTimeTable partOfTimeTable) {
        TimeTableDatabase.dbWriteExecutor.execute(() -> dao.insertPartOfTimeTable(partOfTimeTable));
    }

    void insertWeek2(final TimeTableWeek2 timeTableWeek2) {
        TimeTableDatabase.dbWriteExecutor.execute(() -> dao.insertTimeTableWeek2(timeTableWeek2));
    }

    void insertCheck(final CheckDay checkDay) {
        TimeTableDatabase.dbWriteExecutor.execute(() -> dao.insertDaysNumber(checkDay));
    }

    void deleteWeek1() {
        dao.deleteWeek1();
    }

    void deleteWeek2() {
        dao.deleteWeek2();
    }

    void deleteDay(CheckDay checkDay) {
        dao.delete(checkDay);
    }

}
