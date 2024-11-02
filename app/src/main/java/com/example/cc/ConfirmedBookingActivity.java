package com.example.cc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.cc.databinding.ActivityConfirmedBookingBinding;
import java.util.ArrayList;
import java.util.List;

public class ConfirmedBookingActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ConfirmedBookingAdapter adapter;
    private List<Booking> confirmedBookings;
    private ActivityConfirmedBookingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityConfirmedBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        confirmedBookings = new ArrayList<>();

        loadConfirmedBookings();

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                Intent intent = new Intent(ConfirmedBookingActivity.this, TutorBookingRequests.class);
                startActivity(intent);
            } else if (itemId == R.id.bookingRequests) {
                Intent intent = new Intent(ConfirmedBookingActivity.this, ConfirmedBookingActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.chat) {
                Intent intent = new Intent(ConfirmedBookingActivity.this, ChatActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.settings) {
                Intent intent = new Intent(ConfirmedBookingActivity.this, TutorSettings.class);
                startActivity(intent);
            }

            return true;
        });
    }

    private void loadConfirmedBookings() {

        SharedPreferences tutorPrefs = getSharedPreferences("TutorPrefs", MODE_PRIVATE);
        String loggedInUsername = tutorPrefs.getString("LoggedInTutorUsername", null);

        String tutorName = dbHelper.getLoggedInTutorName(loggedInUsername);
        Cursor cursor = dbHelper.getConfirmedBooking(tutorName);

        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No confirmed bookings found for tutor: " + tutorName, Toast.LENGTH_SHORT).show();
            return;
        }

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_ID));
                @SuppressLint("Range") String tutor = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_TUTORNAME));
                @SuppressLint("Range") String studentName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_STUDENTNAME));
                @SuppressLint("Range") String moduleName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_MODULENAME));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_DATE));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_TIME));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_DURATION));
                @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_STATUS));

                Booking booking = new Booking(id, status, duration, time, date, moduleName, studentName, tutor);
                confirmedBookings.add(booking);

            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new ConfirmedBookingAdapter(this, confirmedBookings);
        binding.listViewConfirmedBookings.setAdapter(adapter);
    }
}

