package com.example.my_time_table.time_table_pojos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Audiences")
public class Audiences {

    @PrimaryKey
    private int idAudiences;

    private String audiencesName;

    public int getIdAudiences() {
        return idAudiences;
    }

    public void setIdAudiences(int idAudiences) {
        this.idAudiences = idAudiences;
    }

    public String getAudiencesName() {
        return audiencesName;
    }

    public void setAudiencesName(String audiencesName) {
        this.audiencesName = audiencesName;
    }
}
