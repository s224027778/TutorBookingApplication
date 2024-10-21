package com.example.cc;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class TutorSettings extends AppCompatActivity {

    private MaterialCardView accountCard, logoutCard, TutorFaqCard, AboutUsCard, locationCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_settings);

        // Initialize the Account Card
        accountCard = findViewById(R.id.accountCard);
        logoutCard = findViewById(R.id.logoutCard);
        TutorFaqCard = findViewById(R.id.TutorFaqCard);
        AboutUsCard = findViewById(R.id.TutorAboutUs);
        locationCard = findViewById(R.id.locationCard);

        AboutUsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorSettings.this, AboutUs.class);
                startActivity(intent);
            }
        });

        TutorFaqCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorSettings.this, FAQs.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener to navigate to AccountActivity
        accountCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorSettings.this, TutorProfileActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener to navigate to AccountActivity
        locationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorSettings.this, LocationActivity.class);
                startActivity(intent);
            }
        });


        logoutCard.setOnClickListener(v -> {
            new AlertDialog.Builder(TutorSettings.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Clear session data and redirect
                        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(TutorSettings.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Similarly, you can set listeners for other settings cards
        // For example:
        // MaterialCardView locationCard = findViewById(R.id.locationCard);
        // locationCard.setOnClickListener(...);
    }
}