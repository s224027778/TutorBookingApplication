package com.example.cc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText editTextUsername, editTextPassword;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor res = db.getUser(username, password);
                if (res == null || res.getCount() == 0) {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    return;
                }

                res.moveToFirst();
                String userType = res.getString(3);
                Toast.makeText(LoginActivity.this, "User Type: " + userType, Toast.LENGTH_SHORT).show();
                SharedPreferences tutorPrefs = getSharedPreferences("TutorPrefs", MODE_PRIVATE);
                SharedPreferences.Editor tutorEditor = tutorPrefs.edit();
                tutorEditor.putString("LoggedInTutorUsername", username); // Store the username
                tutorEditor.apply(); // Apply changes

                SharedPreferences userSession = getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor userEditor = userSession.edit();
                userEditor.putString("LoggedInStudentUsername", username); // Store the username
                userEditor.apply(); // Apply changes


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
                finish();
                // res.close(); // Close the cursor to avoid memory leaks
            }
        });
    }
}