package com.example.cc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cc.databinding.ActivityConfirmedBookingBinding;
import java.util.ArrayList;
import java.util.List;

public class ConfirmedBookingActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listViewConfirmedBookings;
    private ConfirmedBookingAdapter adapter;
    private List<Booking> confirmedBookings;
    private ActivityConfirmedBookingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize binding first
        binding = ActivityConfirmedBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());  // Set the correct root view with binding

        listViewConfirmedBookings = findViewById(R.id.listView_confirmed_bookings);
        dbHelper = new DatabaseHelper(this);
        confirmedBookings = new ArrayList<>();

        loadConfirmedBookings();  // Method to load bookings from the database

        // Set up the bottom navigation and handle intents or fragment replacements
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                Intent intent = new Intent(ConfirmedBookingActivity.this, TutorBookingRequests.class);
                startActivity(intent);
            } else if (itemId == R.id.bookingRequests) {
                Intent intent = new Intent(ConfirmedBookingActivity.this, ConfirmedBookingActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.chat) {
                // Handle chat logic
            } else if (itemId == R.id.settings) {
                Intent intent = new Intent(ConfirmedBookingActivity.this, TutorSettings.class);
                startActivity(intent);
            }

            return true;
        });
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
