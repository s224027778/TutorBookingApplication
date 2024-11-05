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
import com.example.cc.databinding.ActivityTutorSettingsBinding;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class TutorSettings extends AppCompatActivity {

    private MaterialCardView accountCard, logoutCard, TutorFaqCard, AboutUsCard, locationCard, reviewCard, DeleteCard;
    private ActivityTutorSettingsBinding binding;
    private DatabaseHelper dbHelp;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        dbHelp = new DatabaseHelper(this);


        binding = ActivityTutorSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        accountCard = findViewById(R.id.accountCard);
        logoutCard = findViewById(R.id.logoutCard);
        TutorFaqCard = findViewById(R.id.TutorFaqCard);
        AboutUsCard = findViewById(R.id.TutorAboutUs);
        locationCard = findViewById(R.id.locationCard);
        reviewCard = findViewById(R.id.reviewsCard);
        DeleteCard = findViewById(R.id.DeleteCard);

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

        reviewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorSettings.this, ViewReviewActivity.class);
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

        DeleteCard.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Account")
                    .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                    .setPositiveButton("Yes", (dialog, which) -> deleteAccount())
                    .setNegativeButton("No", null)
                    .show();
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

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users"); // Replace with your Realtime Database path
                        databaseReference.child(userId).removeValue().addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {

                                firebaseAuth.getCurrentUser().delete().addOnCompleteListener(task3 -> {
                                    if (task3.isSuccessful()) {

                                        String tutorName = getSharedPreferences("TutorPrefs", MODE_PRIVATE).getString("LoggedInTutorUsername", null);
                                        if (tutorName != null) {
                                            deleteAccountFromSQLite(tutorName);
                                        }

                                        SharedPreferences.Editor editor = getSharedPreferences("TutorPrefs", MODE_PRIVATE).edit();
                                        editor.clear();
                                        editor.apply();

                                        Intent intent = new Intent(TutorSettings.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                        Toast.makeText(TutorSettings.this, "Account deleted successfully.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(TutorSettings.this, "Failed to delete account from Firebase Auth. Try again later.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                Toast.makeText(TutorSettings.this, "Failed to delete account from Realtime Database. Try again later.", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(TutorSettings.this, "Failed to delete account from Firestore. Try again later.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void deleteAccountFromSQLite(String tutorName) {
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        try {
            int deletedRows = db.delete(DatabaseHelper.TABLE_NAME_USERS, "USERNAME=?", new String[]{tutorName});
            int deletedRowsTutor = db.delete(DatabaseHelper.TABLE_NAME_TUTORPROFILE, "FIRSTNAME=?", new String[]{tutorName});

            if (deletedRows > 0) {
                Log.d("TutorSettings", "Account deleted from SQLite UserTable.");
            } else {
                Log.d("TutorSettings", "Account deletion from SQLite UserTable failed.");
            }
            if (deletedRowsTutor > 0) {
                Log.d("TutorSettings", "Account deleted from SQLite TutorTable.");
            } else {
                Log.d("TutorSettings", "Account deletion from SQLite TutorTable failed.");
            }
        } finally {
            db.close(); // Ensure the database is closed
        }
    }
}
