package com.example.cc;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cc.databinding.ActivityTutorDetailBinding;

import java.util.ArrayList;
import java.util.List;

public class TutorDetailActivity extends AppCompatActivity {
    DatabaseHelper db;
    private TextView tvTutorName, tvFirstName, tvLastName, tvPhoneNumber, tvLat, tvLong, tvAddress;
    private Button btnMakeBooking, btnAddReview;
    private ActivityTutorDetailBinding binding;
    private RecyclerView recyclerViewReviews; // RecyclerView for reviews
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_detail);

        // Initialize the ImageButton (make sure the ID matches your XML layout)
        ImageButton back = findViewById(R.id.back_button);

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
        btnAddReview = findViewById(R.id.btnAddReview);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(TutorDetailActivity.this,TutorList.class);
            startActivity(intent);
        });

        recyclerViewReviews = findViewById(R.id.recyclerViewReviews); // Initialize RecyclerView
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));

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

        displayReviews(tutorName);

        // Now that tutorName is initialized, we can call displayTutorLocation
        displayTutorLocation(tutorName);

        // Set click listener for booking button
        btnMakeBooking.setOnClickListener(view -> {
            Intent bookingIntent = new Intent(TutorDetailActivity.this, BookingActivity.class);
            startActivity(bookingIntent);
        });

        btnAddReview.setOnClickListener(view -> {
            Intent reviewIntent = new Intent(TutorDetailActivity.this, ReviewActivity.class);
            startActivity(reviewIntent);
        });

    }

    // Method to display the tutor's location
    private void displayTutorLocation(String tutorName) {
        Cursor cursor = db.getTutorLocation(tutorName); // Fetch location from database
        if (cursor != null && cursor.moveToFirst()) {
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("LATITUDE"));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("LONGITUDE"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("ADDRESS"));

            // Display location details in TextViews
            tvLat.setText("Latitude: " + latitude);
            tvLong.setText("Longitude: " + longitude);
            tvAddress.setText("Address: " + address);

        } else {
            tvLat.setText("Location not available");
            tvLong.setText("");
            tvAddress.setText("");
        }
        cursor.close();  // Close the cursor after use
    }

    // Method to fetch reviews and display them in RecyclerView
    private void displayReviews(String tutorName) {
        reviewList = new ArrayList<>();
        Cursor cursor = db.getReviewsForTutor(tutorName); // Fetch reviews from database

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int reviewId = cursor.getInt(cursor.getColumnIndexOrThrow("ID")); // Fetch ID
                String reviewText = cursor.getString(cursor.getColumnIndexOrThrow("REVIEW_TEXT"));
                int starRating = cursor.getInt(cursor.getColumnIndexOrThrow("RATING"));

                // Create a new Review object and add it to the list
                reviewList.add(new Review(reviewId, tutorName, reviewText, starRating));
            }
            cursor.close();
        } else {
            // Handle no reviews case (optional)
        }

        // Set the adapter with the list of reviews
        reviewAdapter = new ReviewAdapter(reviewList, this);
        recyclerViewReviews.setAdapter(reviewAdapter);
    }
}

