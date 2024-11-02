package com.example.cc;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cc.databinding.ActivityStudentSettingsBinding;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

public class StudentSettings extends AppCompatActivity {

    private MaterialCardView accountCard, logoutCard, StudentFaqCard, StudentAboutUsCard;
    private ActivityStudentSettingsBinding binding;
    String studentName;
    private DatabaseHelper dbHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        accountCard = findViewById(R.id.studentAccountCard);
        logoutCard = findViewById(R.id.studentLogoutCard);
        StudentFaqCard = findViewById(R.id.StudentFaqCard);
        StudentAboutUsCard = findViewById(R.id.StudentAboutUs);

        SharedPreferences userSession = getSharedPreferences("UserSession", MODE_PRIVATE);
        String loggedInUsername = userSession.getString("LoggedInStudentUsername", null);

        // Initialize the DatabaseHelper
        dbHelp = new DatabaseHelper(this);

        // Retrieve the logged-in student's name from the database using the method
        studentName = dbHelp.getLoggedInStudentName(loggedInUsername);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                Intent homeIntent = new Intent(StudentSettings.this, StudentHomeActivity.class);
                startActivity(homeIntent);
            } else if (itemId == R.id.bookingRequests) {
                Intent intent = new Intent(StudentSettings.this, StudentSessions.class);
               // intent.putExtra("STUDENT_NAME", studentName);
                startActivity(intent);
            } else if (itemId == R.id.chat) {
                Intent intent = new Intent(StudentSettings.this, StudentChatActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.settings) {
                Intent intent = new Intent(StudentSettings.this, StudentSettings.class);
                startActivity(intent);
            }

            return true;
        });

        StudentAboutUsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent= new Intent(StudentSettings.this, StudentAboutUs.class);
                 startActivity(intent);
            }
        });

        StudentFaqCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentSettings.this, StudentFAQs.class);
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

                        FirebaseAuth.getInstance().signOut();

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