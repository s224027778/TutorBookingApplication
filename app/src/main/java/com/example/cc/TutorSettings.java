package com.example.cc;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cc.databinding.ActivityTutorSettingsBinding;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

public class TutorSettings extends AppCompatActivity {

    private MaterialCardView accountCard, logoutCard, TutorFaqCard, AboutUsCard, locationCard;
    private ActivityTutorSettingsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTutorSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        accountCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorSettings.this, TutorProfileActivity.class);
                startActivity(intent);
            }
        });

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
                        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();

                        SharedPreferences tutorPrefs = getSharedPreferences("TutorPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor tutorEditor = tutorPrefs.edit();
                        tutorEditor.clear();
                        tutorEditor.apply();

                        FirebaseAuth.getInstance().signOut();

                        Intent intent = new Intent(TutorSettings.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                Intent intent = new Intent(TutorSettings.this, TutorBookingRequests.class);
                startActivity(intent);
            } else if (itemId == R.id.bookingRequests) {
                Intent intent = new Intent(TutorSettings.this, ConfirmedBookingActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.chat) {
                Intent intent = new Intent(TutorSettings.this, ChatActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.settings) {
                Intent intent = new Intent(TutorSettings.this, TutorSettings.class);
                startActivity(intent);
            }

            return true;
        });
    }
}
