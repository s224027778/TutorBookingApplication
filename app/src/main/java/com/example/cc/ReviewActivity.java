package com.example.cc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReviewActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText editTextTutorName, editTextReview;
    Button btnSubmit;
    RatingBar ratingReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        String tutorName = getIntent().getStringExtra("TUTOR_NAME");

        db = new DatabaseHelper(this);
        editTextReview = findViewById(R.id.et_review_text);
        editTextTutorName = findViewById(R.id.et_tutor_name);
        ratingReview = findViewById(R.id.rb_star_rating);
        btnSubmit = findViewById(R.id.btn_submit_review);
        ImageButton back = findViewById(R.id.back_button);

        editTextTutorName.setText(tutorName);
        editTextTutorName.setEnabled(false);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(ReviewActivity.this,StudentHomeActivity.class);
            startActivity(intent);
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tutorName = editTextTutorName.getText().toString();
                String review = editTextReview.getText().toString();
                String rating = String.valueOf(ratingReview.getRating());

                if (review.isEmpty())
                {
                    Toast.makeText(ReviewActivity.this, "Review field cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (rating.isEmpty())
                {
                    Toast.makeText(ReviewActivity.this, "Rating field cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                boolean isInserted = db.insertReview(tutorName, review, rating);
                if (isInserted) {
                    Toast.makeText(ReviewActivity.this, "Review sent, sending you to home page", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ReviewActivity.this,StudentHomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ReviewActivity.this, "Review failed to send", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}