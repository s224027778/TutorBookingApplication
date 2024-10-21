package com.example.cc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingViewHolder> {

    private List<Booking> bookingList;

    public BookingsAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.tutorName.setText(booking.getTutorName());
        holder.moduleName.setText(booking.getModuleName());
        holder.date.setText(booking.getDate());
        holder.time.setText(booking.getTime());
        holder.duration.setText(booking.getDuration());
        holder.status.setText(booking.getStatus() == 0 ? "Pending" : "Confirmed");
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tutorName, moduleName, date, time, duration, status;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tutorName = itemView.findViewById(R.id.tutorName);
            moduleName = itemView.findViewById(R.id.moduleName);
            date = itemView.findViewById(R.id.bookingDate);
            time = itemView.findViewById(R.id.bookingTime);
            duration = itemView.findViewById(R.id.bookingDuration);
            status = itemView.findViewById(R.id.bookingStatus);
        }
    }
}
