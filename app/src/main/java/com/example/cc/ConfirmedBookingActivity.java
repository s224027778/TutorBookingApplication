package com.example.cc;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ConfirmedBookingActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listViewConfirmedBookings;
    private ConfirmedBookingAdapter adapter;
    private List<Booking> confirmedBookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed_booking);

        listViewConfirmedBookings = findViewById(R.id.listView_confirmed_bookings);
        dbHelper = new DatabaseHelper(this);
        confirmedBookings = new ArrayList<>();

        loadConfirmedBookings();  // Method to load bookings from the database
    }

    private void loadConfirmedBookings() {
        Cursor cursor = dbHelper.getConfirmedBooking("TutorName"); // Use the actual tutor name here

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String studentName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_STUDENTNAME));
                @SuppressLint("Range") String moduleName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_MODULENAME));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_DATE));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_TIME));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_DURATION));

            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new ConfirmedBookingAdapter(this, confirmedBookings);
        listViewConfirmedBookings.setAdapter(adapter);
    }
}
