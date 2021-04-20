package com.example.my_time_table.fragments.dummy;

import java.util.ArrayList;
import java.util.List;


public class ScheduleData {

    public static final List<ScheduleData.ScheduleItem> ITEMS = new ArrayList<ScheduleData.ScheduleItem>();
//    public static ArrayList<Integer> numbers = new ArrayList<Integer>();
    private static boolean[] checkDays = new boolean[7];
    public static int[] numbers = new int[] {0,0,0,0,0,0};

    static {

        // Инициализируем нашу бд.
        dataItem();

        //TODO: Нужно доделать
        for (int i = 0; i < 29; i++) {
            // Вызываем метод создания и записывания item'a
            addItem(createScheduleData(i));

            // Что-то что пока не работает
            switch (ITEMS.get(i).day_Week) {
                case "Понедельник":
                    numbers[0] = i;
                    break;
                case "Вторник":
                    numbers[1] = i;
                    break;
                case "Среда":
                    numbers[2] = i;
                    break;
                case "Четверг":
                    numbers[3] = i;
                    break;
                case "Пятница":
                    numbers[4] = i;
                    break;
                case "Суббота":
                    numbers[5] = i;
                    break;
            }
        }
    }

    // Записываем item в ITEMS
    private static void addItem(ScheduleData.ScheduleItem item) {
        ITEMS.add(item);
    }

    // Создаём новый item
    private static ScheduleItem createScheduleData(int i) {

        return new ScheduleItem(String.valueOf(i), timeTable.get(i), teacherName.get(i),
                dayWeek.get(i), cabinet.get(i), time.get(i));
    }

    // Наш item и его св-ва.
    public static class ScheduleItem{

        public final String teacherName;
        public final String subjectName;
        public final String id;
        public final String day_Week;
        public final String mCabinet;
        public final String mTime;

        public ScheduleItem(String id, String subjectName, String teacherName, String day_Week,
                            String cabinet, String time) {

            this.id = id;
            this.subjectName = subjectName;
            this.teacherName = teacherName;
            this.day_Week = day_Week;
            this.mCabinet = cabinet;
            this.mTime = time;
        }

    }

    private static ArrayList<String> timeTable;
    private static ArrayList<String> teacherName;
    private static ArrayList<String> dayWeek;
    private static ArrayList<String> cabinet;
    private static ArrayList<String> time;

    //TODO: В будущем получать нужную информацию через сервер с бд.
    private static void dataItem() {

        timeTable = new ArrayList<String>();
        teacherName = new ArrayList<String>();
        dayWeek = new ArrayList<String>();
        cabinet = new ArrayList<String>();
        time = new ArrayList<String>();

        //---------------------------------------------
        dayWeek.add("Понедельник");
        timeTable.add("");
        teacherName.add("");
        cabinet.add("");
        time.add("");

        dayWeek.add("");
        timeTable.add("Операционные системы и сети");
        teacherName.add("Чичиков С.А.");
        cabinet.add("Ал. 103");
        time.add("08:00-09:30");

        dayWeek.add("");
        timeTable.add("Инфокоммуникационные системы и сети");
        teacherName.add("Чичиков С.А.");
        cabinet.add("Ал. 103");
        time.add("09:40-11:10");

        dayWeek.add("");
        timeTable.add("Базы данных");
        teacherName.add("Лопатеева О.Н.");
        cabinet.add("Крп.2 405");
        time.add("11:30-13:00");

        dayWeek.add("");
        timeTable.add("Базы данных");
        teacherName.add("Лопатеева О.Н.");
        cabinet.add("Крп.2 405");
        time.add("13:30-15:00");

        dayWeek.add("");
        timeTable.add("Методы и средства проектирования информациоонных систем " +
                " и технологий");
        teacherName.add("Доррер А.Г.");
        cabinet.add("Ал. 109");
        time.add("15:10-16:40");

        dayWeek.add("");
        timeTable.add("Методы и средства проектирования информациоонных систем " +
                " и технологий");
        teacherName.add("Доррер А.Г.");
        cabinet.add("Ал. 109");
        time.add("16:50-18:20");

        dayWeek.add("");
        timeTable.add("");
        teacherName.add("");
        cabinet.add("");
        time.add("");

        //---------------------------------------------
        dayWeek.add("Вторник");
        timeTable.add("");
        teacherName.add("");
        cabinet.add("");
        time.add("");

        dayWeek.add("");
        timeTable.add("Профессионально-прикладная физическая культура");
        teacherName.add("Мунгалов А.Ю.");
        cabinet.add("Спортзал");
        time.add("09:40-11:10");

        dayWeek.add("");
        timeTable.add("Философия (Практика)");
        teacherName.add("Медова А.А.");
        cabinet.add("Ал. 417");
        time.add("11:30-13:00");

        dayWeek.add("");
        timeTable.add("Философия (Практика)");
        teacherName.add("Медова А.А.");
        cabinet.add("Ал. 417");
        time.add("13:30-15:00");

        dayWeek.add("");
        timeTable.add("");
        teacherName.add("");
        cabinet.add("");
        time.add("");

        //---------------------------------------------
        dayWeek.add("Четверг");
        timeTable.add("");
        teacherName.add("");
        cabinet.add("");
        time.add("");

        dayWeek.add("");
        timeTable.add("Профессионально-прикладная физическая культура");
        teacherName.add("Мунгалов А.Ю.");
        cabinet.add("Ал. 103");
        time.add("09:40-11:10");

        dayWeek.add("");
        timeTable.add("Технологии разработки web-приложениий");
        teacherName.add("Яровой С.В.");
        cabinet.add("Гл. 410");
        time.add("11:30-13:00");

        dayWeek.add("");
        timeTable.add("Базы данных");
        teacherName.add("Лопатеева О.Н.");
        cabinet.add("Гл. 407а");
        time.add("13:30-15:00");

        dayWeek.add("");
        timeTable.add("");
        teacherName.add("");
        cabinet.add("");
        time.add("");

        //---------------------------------------------
        dayWeek.add("Пятница");
        timeTable.add("");
        teacherName.add("");
        cabinet.add("");
        time.add("");

        dayWeek.add("");
        timeTable.add("Операционные системы и сети");
        teacherName.add("Чичиков С.А.");
        cabinet.add("Вл. 1");
        time.add("11:30-13:00");

        dayWeek.add("");
        timeTable.add("Технологии разработки web-приложениий");
        teacherName.add("Яровой С.В.");
        cabinet.add("Цл. 302");
        time.add("13:30-15:00");

        dayWeek.add("");
        timeTable.add("Инфокоммуникационные системы и сети");
        teacherName.add("Чичиков С.А.");
        cabinet.add("Гл. 409");
        time.add("15:10-16:40");

        dayWeek.add("");
        timeTable.add("Программное обеспеченме мобильных и встраиваемых систем");
        teacherName.add("Киреев Н.В.");
        cabinet.add("Ал. 103");
        time.add("16:50-18:20");

        dayWeek.add("");
        timeTable.add("");
        teacherName.add("");
        cabinet.add("");
        time.add("");

        //---------------------------------------------
        dayWeek.add("Суббота");
        timeTable.add("");
        teacherName.add("");
        cabinet.add("");
        time.add("");

        dayWeek.add("");
        timeTable.add("Экономика");
        teacherName.add("Ледянева Н.Я.");
        cabinet.add("Вл. 2");
        time.add("08:00-09:30");

        dayWeek.add("");
        timeTable.add("Философия");
        teacherName.add("Медова А.А.");
        cabinet.add("Вл. 2");
        time.add("09:40-11:10");

        dayWeek.add("");
        timeTable.add("Базы данных");
        teacherName.add("Лопатеева О.Н.");
        cabinet.add("Вл. 2");
        time.add("11:30-13:00");

        dayWeek.add("");
        timeTable.add("");
        teacherName.add("");
        cabinet.add("");
        time.add("");

    }
}

