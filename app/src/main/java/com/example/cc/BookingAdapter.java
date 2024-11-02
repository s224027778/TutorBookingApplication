package com.example.cc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

public class BookingAdapter extends BaseAdapter {
    private Context context;
    private List<String> bookings;
    private TutorBookingRequests activity;

    public BookingAdapter(TutorBookingRequests activity, List<String> bookings) {
        this.context = activity;
        this.bookings = bookings;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return bookings.size();
    }

    @Override
    public Object getItem(int position) {
        return bookings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        }

        TextView bookingInfo = convertView.findViewById(R.id.booking_info);
        Button confirmButton = convertView.findViewById(R.id.confirm_button);
        Button declineButton = convertView.findViewById(R.id.decline_button);

        String bookingDetails = bookings.get(position);
        bookingInfo.setText(bookingDetails);

        confirmButton.setOnClickListener(v -> {
            activity.confirmBooking(bookingDetails);
            bookings.remove(position);
            notifyDataSetChanged();
        });

        declineButton.setOnClickListener(v -> {
            activity.declineBooking(bookingDetails);
            bookings.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }
}
