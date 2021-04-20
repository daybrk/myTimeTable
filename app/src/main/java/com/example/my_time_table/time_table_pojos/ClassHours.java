package com.example.my_time_table.time_table_pojos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ClassHours")
public class ClassHours {

    @PrimaryKey
    private int id_Hours;

    private String timeInterval;

    public int getId_Hours() {
        return id_Hours;
    }

    public void setId_Hours(int id_Hours) {
        this.id_Hours = id_Hours;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }
}
