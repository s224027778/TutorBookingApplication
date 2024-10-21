package com.example.cc;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class BookingAdapter extends ArrayAdapter<Booking> {
    private Context context;
    private List<Booking> bookings;
    private DatabaseHelper databaseHelper;

    public BookingAdapter(Context context, List<Booking> bookings, DatabaseHelper db) {
        super(context, 0, bookings);
        this.context = context;
        this.bookings = bookings;
        this.databaseHelper = db;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        }

        Booking booking = bookings.get(position);

        TextView bookingInfo = convertView.findViewById(R.id.booking_info);
        Button confirmButton = convertView.findViewById(R.id.confirm_button);

        bookingInfo.setText("Student: " + booking.getStudentName() + "\nModule: " + booking.getModuleName() + "\nDate: " + booking.getDate());

        confirmButton.setOnClickListener(v -> {
            // Handle confirmation action
            confirmBooking(booking.getId());
        });

        return convertView;
    }

    private void confirmBooking(int bookingId) {
        // Update the booking status in the database
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_BOOKING_STATUS, 1); // Mark as confirmed
        db.update(DatabaseHelper.TABLE_NAME_BOOKING, contentValues, DatabaseHelper.COL_BOOKING_ID + " = ?", new String[]{String.valueOf(bookingId)});

        Toast.makeText(context, "Booking confirmed!", Toast.LENGTH_SHORT).show();
    }
}
