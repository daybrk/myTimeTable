package com.example.my_time_table.time_table_pojos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Week")
public class Week {

    @PrimaryKey
    private int idWeek;

    private String weekNumber;

    public int getIdWeek() {
        return idWeek;
    }

    public void setIdWeek(int idWeek) {
        this.idWeek = idWeek;
    }

    public String getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(String weekNumber) {
        this.weekNumber = weekNumber;
    }
}
