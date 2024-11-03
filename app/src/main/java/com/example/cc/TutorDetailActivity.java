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
    private RecyclerView recyclerViewReviews;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_detail);
        String tutorName = getIntent().getStringExtra("TUTOR_NAME");
        String moduleName = getIntent().getStringExtra("MODULE_NAME");

        ImageButton back = findViewById(R.id.back_button);

        db = new DatabaseHelper(this);

        tvTutorName = findViewById(R.id.tvTutorName);
        tvFirstName = findViewById(R.id.tvFirstName);
        tvLastName = findViewById(R.id.tvLastName);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvLat = findViewById(R.id.tvLat);
        tvLong = findViewById(R.id.tvLong);
        tvAddress = findViewById(R.id.tvAddress);
        btnMakeBooking = findViewById(R.id.btnMakeBooking);
        btnAddReview = findViewById(R.id.btnAddReview);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(TutorDetailActivity.this,TutorList.class);
            startActivity(intent);
        });

        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String firstName = intent.getStringExtra("FIRST_NAME");
        String lastName = intent.getStringExtra("LAST_NAME");
        String phoneNumber = intent.getStringExtra("PHONE_NUMBER");

        tvTutorName.setText(tutorName);
        tvFirstName.setText(firstName);
        tvLastName.setText(lastName);
        tvPhoneNumber.setText(phoneNumber);

        displayReviews(tutorName);

        displayTutorLocation(tutorName);

        btnMakeBooking.setOnClickListener(view -> {
            Intent bookingIntent = new Intent(TutorDetailActivity.this, BookingActivity.class);
            bookingIntent.putExtra("TUTOR_NAME", tutorName);
            bookingIntent.putExtra("MODULE_NAME", moduleName);
            startActivity(bookingIntent);
        });

        btnAddReview.setOnClickListener(view -> {
            Intent reviewIntent = new Intent(TutorDetailActivity.this, ReviewActivity.class);
            reviewIntent.putExtra("TUTOR_NAME", tutorName);
            startActivity(reviewIntent);
        });

    }

    private void displayTutorLocation(String tutorName) {
        Cursor cursor = db.getTutorLocation(tutorName);
        if (cursor != null && cursor.moveToFirst()) {
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("LATITUDE"));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("LONGITUDE"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("ADDRESS"));

            tvLat.setText("Latitude: " + latitude);
            tvLong.setText("Longitude: " + longitude);
            tvAddress.setText("Address: " + address);

        } else {
            tvLat.setText("Location not available");
            tvLong.setText("");
            tvAddress.setText("");
        }
        cursor.close();
    }

    private void displayReviews(String tutorName) {
        reviewList = new ArrayList<>();
        Cursor cursor = db.getReviewsForTutor(tutorName);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int reviewId = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String reviewText = cursor.getString(cursor.getColumnIndexOrThrow("REVIEW_TEXT"));
                int starRating = cursor.getInt(cursor.getColumnIndexOrThrow("RATING"));

                reviewList.add(new Review(reviewId, tutorName, reviewText, starRating));
            }
            cursor.close();
        }

        reviewAdapter = new ReviewAdapter(reviewList, this);
        recyclerViewReviews.setAdapter(reviewAdapter);
    }
}

