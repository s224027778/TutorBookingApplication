package com.example.cc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ConfirmedBookingAdapter extends ArrayAdapter<Booking> {

    public ConfirmedBookingAdapter(Context context, List<Booking> bookings) {
        super(context, 0, bookings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Booking booking = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.confirmed_booking_item, parent, false);
        }

        TextView tvStudentName = convertView.findViewById(R.id.tv_student_name);
        TextView tvModuleName = convertView.findViewById(R.id.tv_module_name);
        TextView tvBookingDate = convertView.findViewById(R.id.tv_booking_date);
        TextView tvBookingTime = convertView.findViewById(R.id.tv_booking_time);
        TextView tvBookingDuration = convertView.findViewById(R.id.tv_booking_duration);

        if (booking != null) {
            tvStudentName.setText(booking.getStudentName());
            tvModuleName.setText(booking.getModuleName());
            tvBookingDate.setText(booking.getDate());
            tvBookingTime.setText(booking.getTime());
            tvBookingDuration.setText(booking.getDuration());
        }

        return convertView;
    }
}

