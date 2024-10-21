package com.example.cc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cc.databinding.ActivityTutorBookingRequestsBinding;

import java.util.ArrayList;
import java.util.List;

public class TutorBookingRequests extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView bookingsListView;
    private String tutorName;
    private ActivityTutorBookingRequestsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorBookingRequestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());  // Set the correct root view

        dbHelper = new DatabaseHelper(this);
        bookingsListView = findViewById(R.id.listViewBookingRequests);

        // Retrieve the tutor's name from the intent
        tutorName = getIntent().getStringExtra("TUTOR_NAME");

        if (tutorName != null && !tutorName.isEmpty()) {
            loadBookingsForTutor(tutorName);
        } else {
            Toast.makeText(this, "Tutor name is missing!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Set up the bottom navigation and handle intents or fragment replacements
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                // Avoid reloading the activity unnecessarily if already on it
                Intent intent = new Intent(TutorBookingRequests.this, TutorHomeActivity.class);
                intent.putExtra("TUTOR_NAME", tutorName);
                startActivity(intent);
            } else if (itemId == R.id.bookingRequests) {
                // We are already on the TutorBookingRequests activity, so do nothing
            } else if (itemId == R.id.chat) {

            } else if (itemId == R.id.settings) {

            }

            return true;
        });
    }

    private void loadBookingsForTutor(String tutorName) {
        Cursor cursor = dbHelper.getBookingsByTutorName(tutorName);

        List<String> bookings = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String bookingDetails = "Student: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_STUDENTNAME)) +
                        "\nModule: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_MODULENAME)) +
                        "\nDate: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_DATE)) +
                        "\nTime: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_TIME));
                bookings.add(bookingDetails);
            } while (cursor.moveToNext());
        }

        // Set up the adapter for the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookings);
        bookingsListView.setAdapter(adapter);

        bookingsListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedBooking = bookings.get(position);
            confirmBooking(selectedBooking);
        });

        cursor.close();
    }

    private void confirmBooking(String bookingDetails) {
        // Extract relevant information from the bookingDetails string (e.g., student name, module name)
        String[] details = bookingDetails.split("\n");
        String studentName = details[0].split(": ")[1];
        String moduleName = details[1].split(": ")[1];

        // Update the booking status to 'confirmed' in the database
        dbHelper.confirmBooking(tutorName, studentName, moduleName);

        // Notify the user that the booking has been confirmed
        Toast.makeText(this, "Booking confirmed for " + studentName, Toast.LENGTH_SHORT).show();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);  // Ensure the frame_layout exists in your layout
        fragmentTransaction.commit();
    }
}
