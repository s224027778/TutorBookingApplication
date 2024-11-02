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

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText editTextUsername, editTextPassword;
    Button buttonLogin;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        back = findViewById(R.id.back);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter both email and password", Toast.LENGTH_LONG).show();
                    return;
                }

                Cursor res = db.getUser(username, password);
                if (res == null || res.getCount() == 0) {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                    return;
                }

                res.moveToFirst();
                String userType = res.getString(3);
                SharedPreferences tutorPrefs = getSharedPreferences("TutorPrefs", MODE_PRIVATE);
                SharedPreferences.Editor tutorEditor = tutorPrefs.edit();
                tutorEditor.putString("LoggedInTutorUsername", username);
                tutorEditor.apply();

                SharedPreferences userSession = getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor userEditor = userSession.edit();
                userEditor.putString("LoggedInStudentUsername", username);
                userEditor.apply();


                switch (userType) {
                    case "Admin":
                        startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                        break;
                    case "Student":
                        startActivity(new Intent(LoginActivity.this, StudentHomeActivity.class));
                        break;
                    case "Tutor":
                        Intent intent = new Intent(LoginActivity.this, TutorBookingRequests.class);
                        intent.putExtra("TUTOR_NAME", db.getLoggedInTutorName(username));
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        Toast.makeText(LoginActivity.this, "Error identifying user type", Toast.LENGTH_SHORT).show();
                }
                finish();

            }
        });
    }
}