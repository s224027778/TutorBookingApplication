package com.example.cc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cc.databinding.ActivityTutorBookingRequestsBinding;
import java.util.ArrayList;
import java.util.List;

public class TutorBookingRequests extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView bookingsListView;
    private String TutorName;  // Tutor name retrieved from shared preferences
    private ActivityTutorBookingRequestsBinding binding;
    private TextView noBookingsMessage;  // Reference to the "no bookings" message TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorBookingRequestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Set the correct root view
        dbHelper = new DatabaseHelper(this);

        // Retrieve the logged-in tutor's name from shared preferences
        SharedPreferences tutorPrefs = getSharedPreferences("TutorPrefs", MODE_PRIVATE);
        TutorName = tutorPrefs.getString("LoggedInTutorUsername", null);
        if (TutorName == null) {
            // Handle the error, e.g., show a message or redirect to login
            Toast.makeText(this, "User not logged in. Please log in again.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TutorBookingRequests.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the activity
            return; // Exit onCreate
        }

        // Log the retrieved tutor name for debugging purposes
        Log.d("TutorBookingRequests", "TutorName from SharedPreferences: " + TutorName);

        bookingsListView = findViewById(R.id.listViewBookingRequests);
        noBookingsMessage = findViewById(R.id.noBookingsMessage);  // Initialize the message TextView

        // Load bookings for the tutor retrieved from SharedPreferences
        loadBookingsForTutor(TutorName);

        // Set up the bottom navigation and handle intents or fragment replacements
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                Intent intent = new Intent(TutorBookingRequests.this, TutorBookingRequests.class);
                startActivity(intent);
            } else if (itemId == R.id.bookingRequests) {
                Intent intent = new Intent(TutorBookingRequests.this, ConfirmedBookingActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.chat) {
                // Handle chat logic
            } else if (itemId == R.id.settings) {
                Intent intent = new Intent(TutorBookingRequests.this, TutorSettings.class);
                startActivity(intent);
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

        cursor.close();

        if (bookings.isEmpty()) {
            noBookingsMessage.setVisibility(View.VISIBLE);
            bookingsListView.setVisibility(View.GONE);
        } else {
            noBookingsMessage.setVisibility(View.GONE);
            bookingsListView.setVisibility(View.VISIBLE);

            // Use the custom adapter
            BookingAdapter adapter = new BookingAdapter(this, bookings);
            bookingsListView.setAdapter(adapter);
        }
    }

    // Method to confirm the booking (already present)
    public void confirmBooking(String bookingDetails) {
        String[] details = bookingDetails.split("\n");
        String studentName = details[0].split(": ")[1];
        String moduleName = details[1].split(": ")[1];

        // Use the tutor name from SharedPreferences
        dbHelper.confirmBooking(TutorName, studentName, moduleName);

        Toast.makeText(this, "Booking confirmed for " + studentName, Toast.LENGTH_SHORT).show();
    }

    // New method to decline the booking
    public void declineBooking(String bookingDetails) {
        String[] details = bookingDetails.split("\n");
        String studentName = details[0].split(": ")[1];
        String moduleName = details[1].split(": ")[1];

        // Use the tutor name from SharedPreferences
        dbHelper.declineBooking(TutorName, studentName, moduleName);

        // Notify the user that the booking has been declined
        Toast.makeText(this, "Booking declined for " + studentName, Toast.LENGTH_SHORT).show();
    }
}
