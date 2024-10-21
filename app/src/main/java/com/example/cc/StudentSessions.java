package com.example.cc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cc.databinding.ActivityStudentSessionsBinding;

import java.util.ArrayList;
import java.util.List;

public class StudentSessions extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookingsAdapter bookingsAdapter;
    private DatabaseHelper databaseHelper;
    private String studentName;
    private ActivityStudentSessionsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sessions);

        recyclerView = findViewById(R.id.recyclerViewBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Retrieve the student's name from the intent
        studentName = getIntent().getStringExtra("STUDENT_NAME");

        if (studentName != null && !studentName.isEmpty()) {
            loadBookings();  // Load bookings for this student
        } else {
            Toast.makeText(this, "Student name is missing!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void loadBookings() {
        // Fetch bookings for the logged-in student
        Cursor cursor = databaseHelper.getStudentBookings(studentName);

        if (cursor != null && cursor.getCount() > 0) {
            List<Booking> bookingList = new ArrayList<>();

            while (cursor.moveToNext()) {
                @SuppressLint("Range") int bookingId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_ID));
                @SuppressLint("Range") String tutorName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_TUTORNAME));
                @SuppressLint("Range") String studentName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_STUDENTNAME));
                @SuppressLint("Range") String moduleName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_MODULENAME));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_DATE));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_TIME));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_DURATION));
                @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_STATUS));

                // Create a Booking object and add it to the list
                Booking booking = new Booking(bookingId, status, duration, time, date, moduleName, studentName, tutorName);
                bookingList.add(booking);
            }

            // Initialize the adapter and set it to the RecyclerView
            bookingsAdapter = new BookingsAdapter(bookingList);
            recyclerView.setAdapter(bookingsAdapter);
        }
    }
}
