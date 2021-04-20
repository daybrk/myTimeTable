package com.example.my_time_table.time_table_pojos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "DayOfWeek")
public class DayOfWeek {

    @PrimaryKey
    private int id_Day;

    private String day_Name;

    public int getId_Day() {
        return id_Day;
    }

    public void setId_Day(int id_Day) {
        this.id_Day = id_Day;
    }

    public String getDay_Name() {
        return day_Name;
    }

    public void setDay_Name(String day_Name) {
        this.day_Name = day_Name;
    }
}
