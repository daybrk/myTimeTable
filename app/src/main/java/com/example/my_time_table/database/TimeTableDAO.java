package com.example.my_time_table.database;

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
    public List<PartOfTimeTable> LoadAllPartOfTimeTable();

    @Query("SELECT * FROM PartOfTimeTable WHERE id_Part = :id")
    public PartOfTimeTable getById1Week(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertPartOfTimeTable(PartOfTimeTable partOfTimeTable);



    @Query("SELECT * FROM TimeTableWeek2")
    public List<TimeTableWeek2> LoadAllTimeTableWeek2();

    @Query("SELECT * FROM TimeTableWeek2 WHERE id_Part = :id")
    public PartOfTimeTable getById2Week(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTimeTableWeek2(TimeTableWeek2 timeTableWeek2);



    @Query("SELECT * FROM CheckDay")
    public List<CheckDay> LoadAllDaysNumber();

    @Query("SELECT * FROM CheckDay WHERE id = :id")
    public CheckDay getByIdDay(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertDaysNumber(CheckDay checkDay);

    @Delete
    public void delete(CheckDay checkDay);

}
