package com.example.cc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText editTextUsername, editTextPassword;
    Button buttonLogin;
    ImageView back;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        back = findViewById(R.id.back);
        firebaseAuth = FirebaseAuth.getInstance();

        back.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(firebaseTask -> {
                            // Local Database Authentication
                            Cursor res = db.getUser(username, password);

                            if (firebaseTask.isSuccessful() && (res != null && res.getCount() > 0)) {
                                // Both logins successful
                                Toast.makeText(LoginActivity.this, "Login successful for both Firebase and local database", Toast.LENGTH_SHORT).show();

                                // Handle user type from local database
                                res.moveToFirst();
                                String userType = res.getString(3);
                                Toast.makeText(LoginActivity.this, "User Type: " + userType, Toast.LENGTH_SHORT).show();

                                // Store username in SharedPreferences
                                SharedPreferences tutorPrefs = getSharedPreferences("TutorPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor tutorEditor = tutorPrefs.edit();
                                tutorEditor.putString("LoggedInTutorUsername", username); // Store the username
                                tutorEditor.apply(); // Apply changes

                                SharedPreferences userSession = getSharedPreferences("UserSession", MODE_PRIVATE);
                                SharedPreferences.Editor userEditor = userSession.edit();
                                userEditor.putString("LoggedInStudentUsername", username); // Store the username
                                userEditor.apply(); // Apply changes

                                // Redirect based on user type
                                switch (userType) {
                                    case "Admin":
                                        startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                                        break;
                                    case "Student":
                                        startActivity(new Intent(LoginActivity.this, StudentHomeActivity.class));
                                        break;
                                    case "Tutor":
                                        Intent intent = new Intent(LoginActivity.this, TutorBookingRequests.class);
                                        intent.putExtra("TUTOR_NAME", db.getLoggedInTutorName(username)); // Pass the tutor's name
                                        startActivity(intent);
                                        finish();
                                        break;
                                    default:
                                        Toast.makeText(LoginActivity.this, "Error identifying user type", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Handle login failures
                                if (!firebaseTask.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Firebase Login failed: " + firebaseTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                if (res == null || res.getCount() == 0) {
                                    Toast.makeText(LoginActivity.this, "Local Database Login failed: Invalid Credentials", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}