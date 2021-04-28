package com.example.my_time_table.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.my_time_table.time_table_pojos.CheckDay;
import com.example.my_time_table.time_table_pojos.PartOfTimeTable;
import com.example.my_time_table.time_table_pojos.TimeTableWeek2;

import java.util.List;

@Dao
public interface TimeTableDAO {

    @Query("SELECT * FROM PartOfTimeTable")
    LiveData<List<PartOfTimeTable>> LoadAllPartOfTimeTable();

    @Query("DELETE FROM PartOfTimeTable")
    void deleteWeek1();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPartOfTimeTable(PartOfTimeTable partOfTimeTable);



    @Query("SELECT * FROM TimeTableWeek2")
    LiveData<List<TimeTableWeek2>> LoadAllTimeTableWeek2();

    @Query("DELETE FROM TimeTableWeek2")
    void deleteWeek2();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTimeTableWeek2(TimeTableWeek2 timeTableWeek2);




    @Query("SELECT * FROM CheckDay")
    LiveData<List<CheckDay>> LoadAllDaysNumber();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDaysNumber(CheckDay checkDay);

    @Delete
    void delete(CheckDay checkDay);

}
