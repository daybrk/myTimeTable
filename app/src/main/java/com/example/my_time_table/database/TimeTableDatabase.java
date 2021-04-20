package com.example.my_time_table.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.my_time_table.time_table_pojos.Audiences;
import com.example.my_time_table.time_table_pojos.ClassHours;
import com.example.my_time_table.time_table_pojos.DayOfWeek;
import com.example.my_time_table.time_table_pojos.Groups;
import com.example.my_time_table.time_table_pojos.PartOfTimeTable;
import com.example.my_time_table.time_table_pojos.Subjects;
import com.example.my_time_table.time_table_pojos.Teachers;
import com.example.my_time_table.time_table_pojos.Week;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Audiences.class, ClassHours.class, DayOfWeek.class, Groups.class,
        PartOfTimeTable.class, Subjects.class, Teachers.class, Week.class}, version = 1,
        exportSchema = false)
abstract class TimeTableDatabase extends RoomDatabase {

    public abstract TimeTableDatabase timeTableDatabase();

    private static volatile TimeTableDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService dbWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TimeTableDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TimeTableDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TimeTableDatabase.class, "database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
