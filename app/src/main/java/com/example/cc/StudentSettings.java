package com.example.cc;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.cc.databinding.ActivityStudentSettingsBinding;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentSettings extends AppCompatActivity {

    private MaterialCardView accountCard, logoutCard, StudentFaqCard, StudentAboutUsCard, DeleteCard, ReportCard;
    private ActivityStudentSettingsBinding binding;
    String studentName;
    private DatabaseHelper dbHelp;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        accountCard = findViewById(R.id.studentAccountCard);
        logoutCard = findViewById(R.id.studentLogoutCard);
        StudentFaqCard = findViewById(R.id.StudentFaqCard);
        StudentAboutUsCard = findViewById(R.id.StudentAboutUs);
        DeleteCard = findViewById(R.id.DeleteCard);
        ReportCard = findViewById(R.id.reportCard);

        SharedPreferences studentPrefs = getSharedPreferences("StudentPrefs", MODE_PRIVATE);
        String loggedInUsername = studentPrefs.getString("LoggedInStudentUsername", null);

        dbHelp = new DatabaseHelper(this);

        studentName = dbHelp.getLoggedInStudentName(loggedInUsername);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                Intent homeIntent = new Intent(StudentSettings.this, StudentHomeActivity.class);
                startActivity(homeIntent);
            } else if (itemId == R.id.bookingRequests) {
                Intent intent = new Intent(StudentSettings.this, StudentSessions.class);
                intent.putExtra("STUDENT_NAME", studentName);
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
                Intent intent = new Intent(StudentSettings.this, StudentAboutUs.class);
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
                        SharedPreferences preferences = getSharedPreferences("StudentPrefs", MODE_PRIVATE);
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
        DeleteCard.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Account")
                    .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                    .setPositiveButton("Yes", (dialog, which) -> deleteAccount())
                    .setNegativeButton("No", null)
                    .show();
        });

        ReportCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentSettings.this, ReportActivity.class);
                startActivity(intent);
            }
        });
    }

    private void deleteAccount() {
        firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();


        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users")
                .document(userId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                        databaseReference.child(userId).removeValue().addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {

                                firebaseAuth.getCurrentUser().delete().addOnCompleteListener(task3 -> {
                                    if (task3.isSuccessful()) {

                                        String tutorName = getSharedPreferences("StudentPrefs", MODE_PRIVATE).getString("LoggedInStudentUsername", null);
                                        if (studentName != null) {
                                            deleteAccountFromSQLite(studentName);
                                        }

                                        SharedPreferences.Editor editor = getSharedPreferences("StudentPrefs", MODE_PRIVATE).edit();
                                        editor.clear();
                                        editor.apply();

                                        Intent intent = new Intent(StudentSettings.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                        Toast.makeText(StudentSettings.this, "Account deleted successfully.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(StudentSettings.this, "Failed to delete account from Firebase Auth. Try again later.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                Toast.makeText(StudentSettings.this, "Failed to delete account from Realtime Database. Try again later.", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(StudentSettings.this, "Failed to delete account from Firestore. Try again later.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void deleteAccountFromSQLite(String tutorName) {
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        try {
            int deletedRows = db.delete(DatabaseHelper.TABLE_NAME_USERS, "USERNAME=?", new String[]{tutorName});
            int deletedRowsTutor = db.delete(DatabaseHelper.TABLE_NAME_STUDENTPROFILE, "FIRSTNAME=?", new String[]{tutorName});

            if (deletedRows > 0) {
                Log.d("StudentSettings", "Account deleted from SQLite UserTable.");
            } else {
                Log.d("StudentSettings", "Account deletion from SQLite UserTable failed.");
            }
            if (deletedRowsTutor > 0) {
                Log.d("StudentSettings", "Account deleted from SQLite TutorTable.");
            } else {
                Log.d("StudentSettings", "Account deletion from SQLite TutorTable failed.");
            }
        } finally {
            db.close();
        }
    }
}