package com.example.cc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cc.databinding.ActivityStudentHomeBinding;
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
        binding = ActivityStudentSessionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.recyclerViewBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);

        studentName = getIntent().getStringExtra("STUDENT_NAME");

        if (studentName != null && !studentName.isEmpty()) {
            loadBookings();
        } else {
            Log.d("StudentSessions", "Student name is missing");
            finish();
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                Intent intent = new Intent(StudentSessions.this, StudentHomeActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.bookingRequests) {
                Intent intent = new Intent(StudentSessions.this, StudentSessions.class);
                intent.putExtra("STUDENT_NAME", studentName);
                startActivity(intent);
            } else if (itemId == R.id.chat) {
               Intent intent = new Intent(StudentSessions.this, StudentChatActivity.class);
               startActivity(intent);
            } else if (itemId == R.id.settings) {
                Intent intent = new Intent(StudentSessions.this, StudentSettings.class);
                startActivity(intent);
            }

            return true;
        });

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

                Booking booking = new Booking(bookingId, status, duration, time, date, moduleName, studentName, tutorName);
                bookingList.add(booking);
            }

            bookingsAdapter = new BookingsAdapter(bookingList);
            recyclerView.setAdapter(bookingsAdapter);
        }
    }
}
