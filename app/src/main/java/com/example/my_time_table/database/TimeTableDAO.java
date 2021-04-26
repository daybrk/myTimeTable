package com.example.my_time_table.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.my_time_table.time_table_pojos.CheckDay;
import com.example.my_time_table.time_table_pojos.PartOfTimeTable;

import java.util.List;

@Dao
public interface TimeTableDAO {

    @Query("SELECT * FROM PartOfTimeTable")
    public List<PartOfTimeTable> LoadAllPartOfTimeTable();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertPartOfTimeTable(PartOfTimeTable partOfTimeTable);

    @Query("SELECT * FROM CheckDay")
    public List<CheckDay> LoadAllDaysNumber();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertDaysNumber(CheckDay checkDay);

}
