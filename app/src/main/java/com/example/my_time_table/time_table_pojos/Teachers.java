package com.example.my_time_table.time_table_pojos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Teachers")
public class Teachers {

    @PrimaryKey
    private int idTeacher;

    private String teacherName;

    public int getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(int idTeacher) {
        this.idTeacher = idTeacher;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
