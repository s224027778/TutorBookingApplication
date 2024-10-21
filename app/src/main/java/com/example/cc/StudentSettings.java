package com.example.cc;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class StudentSettings extends AppCompatActivity {

    private MaterialCardView accountCard, logoutCard, StudentFaqCard, StudentAboutUsCard, locationCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_settings);

        accountCard = findViewById(R.id.studentAccountCard);
        logoutCard = findViewById(R.id.studentLogoutCard);
        StudentFaqCard = findViewById(R.id.StudentFaqCard);
        StudentAboutUsCard = findViewById(R.id.StudentAboutUs);
        locationCard = findViewById(R.id.locationCard);

        StudentAboutUsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent= new Intent(StudentSettings.this, AboutUs.class);
                 startActivity(intent);
            }
        });

        // Set OnClickListener to navigate to Location Activity
        locationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentSettings.this, LocationActivity.class);
                startActivity(intent);
            }
        });

        StudentFaqCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentSettings.this, FAQs.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener to navigate to AccountActivity
        accountCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentSettings.this, StudentProfileActivity.class);
                startActivity(intent);
            }
        });

        logoutCard.setOnClickListener(v -> {
            new AlertDialog.Builder(StudentSettings.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Clear session data and redirect
                        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(StudentSettings.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}