package com.example.my_time_table.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.my_time_table.time_table_pojos.Audiences;
import com.example.my_time_table.time_table_pojos.ClassHours;
import com.example.my_time_table.time_table_pojos.DayOfWeek;
import com.example.my_time_table.time_table_pojos.Groups;
import com.example.my_time_table.time_table_pojos.PartOfTimeTable;
import com.example.my_time_table.time_table_pojos.Subjects;
import com.example.my_time_table.time_table_pojos.Teachers;
import com.example.my_time_table.time_table_pojos.Week;

import java.util.List;

@Dao
public interface TimeTableDAO {

    // Запросы для таблицы Аудиторий

    @Query("SELECT * FROM Audiences")
    public List<Audiences> LoadAllAudiences();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAudiences(Audiences audiences);

    // Запросы для таблицы Часов занятий

    @Query("SELECT * FROM ClassHours")
    public List<ClassHours> LoadAllClassHours();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertClassHours(ClassHours classHours);

    // Запросы для таблицы Дни недели

    @Query("SELECT * FROM DayOfWeek")
    public List<DayOfWeek> LoadAllDayOfWeek();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertDayOfWeek(DayOfWeek dayOfWeek);

    // Запросы для таблицы Группы

    @Query("SELECT * FROM Groups")
    public List<Groups> LoadAllGroups();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertGroups(Groups groups);

    // Запросы для таблицы Часть расписания

    @Query("SELECT * FROM PartOfTimeTable")
    public List<PartOfTimeTable> LoadAllPartOfTimeTable();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertPartOfTimeTable(PartOfTimeTable partOfTimeTable);

    // Запросы для таблицы Предметов

    @Query("SELECT * FROM Subjects")
    public List<Subjects> LoadAllSubjects();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertSubjects(Subjects subjects);

    // Запросы для таблицы Учителей

    @Query("SELECT * FROM Teachers")
    public List<Teachers> LoadAllTeachers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTeachers(Teachers teachers);

    // Запросы для таблицы Недель

    @Query("SELECT * FROM Week")
    public List<Week> LoadAllWeek();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertWeek(Week week);








}
