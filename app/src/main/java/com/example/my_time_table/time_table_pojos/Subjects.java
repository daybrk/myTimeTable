package com.example.my_time_table.time_table_pojos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Subjects")
public class Subjects {

    @PrimaryKey
    private int idSubject;

    private String subjectName;

    public int getIdSubject() {
        return idSubject;
    }

    public void setIdSubject(int idSubject) {
        this.idSubject = idSubject;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
