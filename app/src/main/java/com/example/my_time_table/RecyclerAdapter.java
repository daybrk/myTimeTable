package com.example.my_time_table;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.my_time_table.fragments.dummy.ScheduleData;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final List<ScheduleData.ScheduleItem> mValues;
    // Типы view.
    // Основной тип, используемый для отображения основной информации.
    private final int TYPE_ITEM1 = 0;
    // Тип view в который отображает лишь шапку.
    private final int TYPE_ITEM2 = 1;
    // View отображающий footer проекта.
    private final int TYPE_ITEM3 = 2;

    public RecyclerAdapter(List<ScheduleData.ScheduleItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        // Подключаем нужный view, от полученного viewType переданного из метода getItemViewType.
        switch (viewType) {
            case TYPE_ITEM1:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_schedule, parent, false);
                return new ViewHolder(view);
            case TYPE_ITEM2:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_schedule_head, parent, false);
                return new ViewHolder(view);
            case TYPE_ITEM3:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_schedule_bottom, parent, false);
                return new ViewHolder(view);
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        int type = getItemViewType(position);

        // В зависимости от type записываем в элементы ViewHolder нужную информацию.
        switch (type) {
            case TYPE_ITEM1:
                holder.mSubject.setText(mValues.get(position).subjectName);
                holder.mTeachersName.setText(mValues.get(position).teacherName);
                holder.mCabinet.setText(mValues.get(position).mCabinet);
                holder.mTime.setText(mValues.get(position).mTime);
                break;
            case TYPE_ITEM2:
                holder.mDayWeek.setText(mValues.get(position).day_Week);
                if (Integer.parseInt(mValues.get(ScheduleData.numbers
                        [(MainActivity.dayOfWeek)]).id) == position ) {
                    holder.today.setText("Сегодня");
                } else {
                    holder.today.setText("");
                }
                break;
        }

    }

    // Размер recyclerView (кл-во его элементов)
    // Используем кл-во элементов в бд.
    @Override
    public int getItemCount() {
        return mValues.size();
    }


    @Override
    public int getItemViewType(int position) {
        for (int i = 0; i < ScheduleData.numbers.length; i++) {

            if (ScheduleData.numbers[i]== position){
                return TYPE_ITEM2;
            } else if ((ScheduleData.numbers[i] == position + 1) ||
                    (ScheduleData.ITEMS.size() == position + 1)) {
                return TYPE_ITEM3;
            }
        }
        // По default'y возвращаем основной view.
        return TYPE_ITEM1;
    }


    // View и его элементы.
    public class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public TextView mSubject;
        public TextView mTeachersName;
        public TextView mCabinet;
        public TextView mTime;
        public ImageView line;
        public TextView mDayWeek;
        public TextView today;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mSubject = (TextView) view.findViewById(R.id.subject);
            mTeachersName = (TextView) view.findViewById(R.id.teachers_name);
            mCabinet = (TextView) view.findViewById(R.id.number_class);
            mTime = (TextView) view.findViewById(R.id.time);
//            line = (ImageView) view.findViewById(R.id.imageView);
            mDayWeek = (TextView) view.findViewById(R.id.dayWeek);
            today = (TextView) view.findViewById(R.id.today);
        }


    }
}