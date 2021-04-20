package com.example.my_time_table;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.my_time_table.fragments.ScheduleFragment;
import com.example.my_time_table.fragments.ScheduleFragment2;
import com.example.my_time_table.fragments.dummy.ScheduleData;
import java.util.Calendar;



public class MainActivity extends AppCompatActivity {

    private static TextView weeklyNumber;
    // Кл-во листаемых страниц
    private static final int NUM_PAGES = 2;
    public static int dayOfWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weeklyNumber = (TextView) findViewById(R.id.weekly_number);
        ViewPager2 viewPager2 = (ViewPager2) findViewById(R.id.viewpager);

        // Создаём pageAdapter и запускаем viewPager
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(fragmentManager, getLifecycle());

        viewPager2.setAdapter(pagerAdapter);

        // Устанавливаем номер начальное недели
        weeklyNumber.setText("1");


        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        Log.i("DAY", String.valueOf(c.getFirstDayOfWeek()));
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 2;
        Log.i("DAYS", String.valueOf(dayOfWeek));
    }

    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Создаём фрагмент
            switch (position) {
                case 0:
                    return new ScheduleFragment();
                case 1:
                    return new ScheduleFragment2();
            }
            return null;
        }

        @Override
        public long getItemId ( int position) {
            switch (position) {
                case 0:
                case 1:
                    try {
                        // Автопролистывание
//                        ScheduleFragment.recyclerView.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                int d;
//                                try {
//                                    // В зависимости от дня недели получаем id нужного элемента.
//                                    d = ScheduleData.numbers[(dayOfWeek) + 1] - 1;
//                                } catch (ArrayIndexOutOfBoundsException e) {
//                                    d = ScheduleData.ITEMS.size() - 1;
//                                }
//                                if (d != 0) {
//                                    // TODO: НУЖНО ЛИ?
////                                    d = Integer.parseInt(ScheduleData.ITEMS.get(d).id);
////                                    // Используем auto-scroll к нужному элементу.
////                                    ScheduleFragment.recyclerView.smoothScrollToPosition(d);
//                                }
//                            }
//                        });
                    } catch (Exception ignore) {

                    }
                    // С каждым пролистыванием, меняем значение недели.
                    weeklyNumber.setText(String.valueOf(position + 1));
                    break;
            }
            return super.getItemId(position);
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    public static void scrollToItem () {


    }


}