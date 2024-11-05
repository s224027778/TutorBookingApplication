package com.example.cc;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {

    DatabaseHelper db;
    TextView textViewUsers, textViewBooking, textViewModules, textViewTutorProfile, textViewStudentProfile, textViewTutorModule, textViewLocation, textViewReviews, textViewPrice, textViewFrequentlyAskedQuestions, textViewTutorAvailability;
    Button buttonViewUsers, buttonViewBooking, buttonViewTutorProfile, buttonViewStudentProfile, buttonViewTutorModule, buttonViewModules, buttonViewLocation, buttonViewReviews, buttonViewPrices, buttonAdminLogout, buttonViewFAQ, buttonViewTutorAvailability;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        db = new DatabaseHelper(this);
        textViewUsers = findViewById(R.id.textViewUsers);
        textViewBooking = findViewById(R.id.textViewBooking);
        textViewModules = findViewById(R.id.textViewModules);
        textViewTutorModule = findViewById(R.id.textViewTutorModule);
        textViewTutorProfile = findViewById(R.id.textViewTutorProfile);
        textViewStudentProfile = findViewById(R.id.textViewStudentProfile);
        textViewLocation = findViewById(R.id.textViewLocation);
        textViewPrice = findViewById(R.id.textViewPrices);
        textViewFrequentlyAskedQuestions = findViewById(R.id.textViewFrequentlyAskedQuestions);
        textViewTutorAvailability = findViewById(R.id.textViewTutorAvailability);
        textViewReviews = findViewById(R.id.textViewReviews);
        buttonViewUsers = findViewById(R.id.buttonViewUsers);
        buttonViewBooking = findViewById(R.id.buttonViewBooking);
        buttonViewModules = findViewById(R.id.buttonViewModules);
        buttonViewTutorProfile = findViewById(R.id.buttonViewTutorProfile);
        buttonViewStudentProfile = findViewById(R.id.buttonViewStudentProfile);
        buttonViewTutorModule = findViewById(R.id.buttonViewTutorModules);
        buttonViewLocation = findViewById(R.id.buttonViewLocation);
        buttonViewPrices = findViewById(R.id.buttonViewPrices);
        buttonViewFAQ = findViewById(R.id.buttonViewFAQ);
        buttonViewTutorAvailability = findViewById(R.id.buttonViewTutorAvailability);
        buttonViewReviews = findViewById(R.id.buttonViewReviews);
        buttonAdminLogout = findViewById(R.id.buttonAdminLogout);

        buttonViewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewUsers();
            }
        });

        buttonViewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewBooking();
            }
        });

        buttonViewTutorProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewTutorProfiles();
            }
        });

        buttonViewStudentProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewStudentProfiles();
            }
        });

        buttonViewModules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModules();
            }
        });

        buttonViewTutorModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewTutorModules();
            }
        });

        buttonViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLocation();
            }
        });

        buttonViewPrices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPrices();
            }
        });

        buttonViewFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminFAQs.class);
                startActivity(intent);
            }
        });

        buttonViewTutorAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAvailabilities();
            }
        });

        buttonViewReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewReviews();
            }
        });

        buttonAdminLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new android.app.AlertDialog.Builder(AdminHomeActivity.this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            SharedPreferences preferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();

                            FirebaseAuth.getInstance().signOut();

                            Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

    }

    private void viewPrices(){
        List<String> prices = db.getAllPrices();
        StringBuilder sb = new StringBuilder();
        for (String record : prices) {
            sb.append(record).append("\n");
        }
        textViewPrice.setText(sb.toString());
        textViewPrice.setVisibility(View.VISIBLE);
    }

    private void viewBooking() {
        Cursor res = db.getAllBookings();
        StringBuilder sb = new StringBuilder();
        while (res.moveToNext()) {
            sb.append("ID: ").append(res.getString(0)).append(", Tutorname: ").append(res.getString(1)).append(", Studentname: ").append(res.getString(2)).append(", Modulename: ").append(res.getString(3)).append(", Date: ").append(res.getString(4)).append(", Time: ").append(res.getString(5)).append(", Duration: ").append(res.getString(6)).append(", Status: ").append(res.getString(7)).append("\n");
        }
        textViewBooking.setText(sb.toString());
        textViewBooking.setVisibility(View.VISIBLE);
    }

    private void viewUsers() {
        Cursor res = db.getAllUsers();
        StringBuilder sb = new StringBuilder();
        while (res.moveToNext()) {
            sb.append("ID: ").append(res.getString(0)).append(", Username: ").append(res.getString(1)).append(", UserType: ").append(res.getString(3)).append("\n");
        }
        textViewUsers.setText(sb.toString());
        textViewUsers.setVisibility(View.VISIBLE);
    }

    private void viewModules() {
        Cursor res = db.getAllModules();
        StringBuilder sb = new StringBuilder();
        while (res.moveToNext()) {
            sb.append("ID: ").append(res.getString(0)).append(", Name: ").append(res.getString(1)).append(", CategoryId: ").append("\n");
        }
        textViewModules.setText(sb.toString());
        textViewModules.setVisibility(View.VISIBLE);
    }

    private void viewTutorProfiles() {
        Cursor res = db.getAllTutorProfiles();
        StringBuilder sb = new StringBuilder();
        while (res.moveToNext()) {
            sb.append("Name: ").append(res.getString(0)).append(", FirstName: ").append(res.getString(1)).append(", LastName: ").append(res.getString(2)).append(", PhoneNumber: ").append(res.getString(3)).append("\n");
        }
        textViewTutorProfile.setText(sb.toString());
        textViewTutorProfile.setVisibility(View.VISIBLE);
    }

    private void viewStudentProfiles() {
        Cursor res = db.getAllStudentProfiles();
        StringBuilder sb = new StringBuilder();
        while (res.moveToNext()) {
            sb.append("Name: ").append(res.getString(0)).append(", FirstName: ").append(res.getString(1)).append(", LastName: ").append(res.getString(2)).append(", PhoneNumber: ").append(res.getString(3)).append("\n");
        }
        textViewStudentProfile.setText(sb.toString());
        textViewStudentProfile.setVisibility(View.VISIBLE);
    }

    // not working
    private void viewTutorModules() {
        Cursor res = db.getAllTutorModules();
        StringBuilder sb = new StringBuilder();
        while (res.moveToNext()) {
            sb.append("TutorId: ").append(res.getString(0))
                    .append(", ModuleId: ").append(res.getString(1)).append("\n");
        }
        textViewTutorModule.setText(sb.toString());
        textViewTutorModule.setVisibility(View.VISIBLE);
    }

    private void viewAvailabilities() {
        Cursor res = db.getAllAvailabilities();
        StringBuilder sb = new StringBuilder();

        while (res.moveToNext()) {
            sb.append("Tutor Name: ").append(res.getString(0))
                    .append(", Day of Week: ").append(res.getString(1))
                    .append(", Start Time: ").append(res.getString(2))
                    .append(", End Time: ").append(res.getString(3))
                    .append("\n\n");
        }

        if (sb.length() > 0) {
            textViewTutorAvailability.setText(sb.toString());
            textViewTutorAvailability.setVisibility(View.VISIBLE);
        } else {
            textViewTutorAvailability.setText("No availability data found.");
            textViewTutorAvailability.setVisibility(View.VISIBLE);
        }


    }


    private void viewReviews() {
        Cursor res = db.getAllReviews();
        StringBuilder sb = new StringBuilder();
        while (res.moveToNext()) {
            sb.append("ID: ").append(res.getString(0)).append(", TutorName: ").append(res.getString(1)).append(", Review: ").append(res.getString(2)).append(", Star Rating: ").append(res.getString(3)).append("\n");
        }
        textViewReviews.setText(sb.toString());
        textViewReviews.setVisibility(View.VISIBLE);
    }

    private void viewLocation() {
        Cursor res = db.getAllLocations();
        StringBuilder sb = new StringBuilder();
        while (res.moveToNext()) {
            sb.append("TutorName: ").append(res.getString(0)).append(", Latitude: ").append(res.getString(1)).append(", Longitude: ").append(res.getString(2)).append(", Address: ").append(res.getString(3)).append("\n");
        }
        textViewLocation.setText(sb.toString());
        textViewLocation.setVisibility(View.VISIBLE);
    }
}



