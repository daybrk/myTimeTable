package com.example.my_time_table;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.my_time_table.time_table_pojos.PartOfTimeTable;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final List<PartOfTimeTable> mValues;
    // Типы view.
    // Основной тип, используемый для отображения основной информации.
    private final int TYPE_ITEM1 = 0;
    // Тип view в который отображает лишь шапку.
    private final int TYPE_ITEM2 = 1;
    // View отображающий footer проекта.
    private final int TYPE_ITEM3 = 2;

    String day;

    public RecyclerAdapter(List<PartOfTimeTable> items) {
        mValues = items;;
    }

    //TODO: Обновить view, добавить новые для разных ситуаций отображения.

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
            case TYPE_ITEM3:
                holder.mSubject.setText(mValues.get(position).getSubject());
                holder.mTeachersName.setText(mValues.get(position).getTeacher());
                holder.mCabinet.setText(mValues.get(position).getAudience());
                holder.mTime.setText(mValues.get(position).getClassHour());
                holder.mTypeSub.setText(mValues.get(position).getType());
                holder.mSubgroup.setText(mValues.get(position).getSubGroup());
                break;
            case TYPE_ITEM2:
                holder.mSubject.setText(mValues.get(position).getSubject());
                holder.mTeachersName.setText(mValues.get(position).getTeacher());
                holder.mCabinet.setText(mValues.get(position).getAudience());
                holder.mTime.setText(mValues.get(position).getClassHour());
                holder.mTypeSub.setText(mValues.get(position).getType());
                holder.mSubgroup.setText(mValues.get(position).getSubGroup());
                holder.mDayWeek.setText(mValues.get(position).getDay());
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

        day = mValues.get(position).getDay();

        // В цикле проверяем совпадает ли переменная position с каким-то из записанных значений в List
        // nextDay, если да то возвращаем второй тип view
        for (int i = 0; i < MainActivity.nextDay.size(); i++) {
            if (MainActivity.nextDay.get(i) == position) {
                return TYPE_ITEM2;
            }
        }

        // Если элементы заканчиваются или же следющий элемент содержит "новый" день, то возращаем
        // третий тип view
        if ((position + 1 == getItemCount()) || (!day.equals(mValues.get(position + 1).getDay()))) {
            return TYPE_ITEM3;
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
        public TextView mSubgroup;
        public TextView mTypeSub;
        public TextView mDayWeek;
        public TextView today;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mSubject = (TextView) view.findViewById(R.id.subject);
            mTeachersName = (TextView) view.findViewById(R.id.teachers_name);
            mCabinet = (TextView) view.findViewById(R.id.number_class);
            mTime = (TextView) view.findViewById(R.id.time);
            mTypeSub = (TextView) view.findViewById(R.id.typeSubject);
            mSubgroup = (TextView) view.findViewById(R.id.subgroups);
            mDayWeek = (TextView) view.findViewById(R.id.dayWeek);
//            today = (TextView) view.findViewById(R.id.today);
        }
    }

}