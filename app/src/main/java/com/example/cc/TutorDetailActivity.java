package com.example.cc;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.cc.databinding.ActivityTutorDetailBinding;

public class TutorDetailActivity extends AppCompatActivity {
    DatabaseHelper db;
    private TextView tvTutorName, tvFirstName, tvLastName, tvPhoneNumber, tvLat, tvLong, tvAddress;
    private Button btnMakeBooking;
    private ActivityTutorDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new DatabaseHelper(this);

        // Initialize TextViews
        tvTutorName = findViewById(R.id.tvTutorName);
        tvFirstName = findViewById(R.id.tvFirstName);
        tvLastName = findViewById(R.id.tvLastName);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvLat = findViewById(R.id.tvLat);   // TextView for Latitude
        tvLong = findViewById(R.id.tvLong); // TextView for Longitude
        tvAddress = findViewById(R.id.tvAddress); // TextView for Address
        btnMakeBooking = findViewById(R.id.btnMakeBooking);

        // Get tutor data from intent
        Intent intent = getIntent();
        String tutorName = intent.getStringExtra("TUTOR_NAME");
        String firstName = intent.getStringExtra("FIRST_NAME");
        String lastName = intent.getStringExtra("LAST_NAME");
        String phoneNumber = intent.getStringExtra("PHONE_NUMBER");

        // Display tutor details
        tvTutorName.setText(tutorName);
        tvFirstName.setText(firstName);
        tvLastName.setText(lastName);
        tvPhoneNumber.setText(phoneNumber);

        // Now that tutorName is initialized, we can call displayTutorLocation
        displayTutorLocation(tutorName);

        // Set click listener for booking button
        btnMakeBooking.setOnClickListener(view -> {
            Intent bookingIntent = new Intent(TutorDetailActivity.this, BookingActivity.class);
            startActivity(bookingIntent);
        });

        // Set up the bottom navigation and handle intents or fragment replacements
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                // Navigate to StudentHomeActivity
                Intent homeIntent = new Intent(TutorDetailActivity.this, StudentHomeActivity.class);
                startActivity(homeIntent);
            } else if (itemId == R.id.bookingRequests) {
                // Navigate to TutorBookingRequests
                Intent bookingRequestsIntent = new Intent(TutorDetailActivity.this, TutorBookingRequests.class);
                startActivity(bookingRequestsIntent);
            } else if (itemId == R.id.chat) {
                // Chat functionality (implementation pending)
            } else if (itemId == R.id.settings) {
                Intent settingsIntent = new Intent(TutorDetailActivity.this, TutorSettings.class);
                startActivity(settingsIntent);
            }

            return true;
        });
    }

    // Method to display the tutor's location
    private void displayTutorLocation(String tutorName) {
        Cursor cursor = db.getTutorLocation(tutorName);// Fetch location from database
        if (cursor != null && cursor.moveToFirst()) {
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("LATITUDE"));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("LONGITUDE"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("ADDRESS"));

            // Display location details in TextViews
            tvLat.setText("Latitude: " + latitude);
            tvLong.setText("Longitude: " + longitude);
            tvAddress.setText("Address: " + address);

            Log.d("TutorDetailActivity", "Tutor Name: " + tutorName);
            Log.d("TutorDetailActivity", "Latitude: " + latitude);
            Log.d("TutorDetailActivity", "Longitude: " + longitude);

        } else {
            // Display default message if location is not available
            tvLat.setText("Location not available");
            tvLong.setText("");
            tvAddress.setText("");
        }
        cursor.close();  // Close the cursor after use
    }
}

