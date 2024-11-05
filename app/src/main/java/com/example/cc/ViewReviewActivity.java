package com.example.cc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewReviewActivity extends AppCompatActivity {

    private RecyclerView recyclerViewReviews;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_review);

        ImageButton back = findViewById(R.id.back_button);

        db = new DatabaseHelper(this);

        SharedPreferences tutorPrefs = getSharedPreferences("TutorPrefs", MODE_PRIVATE);
        String tutorName = tutorPrefs.getString("LoggedInTutorUsername", null);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(ViewReviewActivity.this,TutorSettings.class);
            startActivity(intent);
        });

        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));

        displayReviews(tutorName);

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