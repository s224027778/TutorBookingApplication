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
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();

        db = new DatabaseHelper(this);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        back = findViewById(R.id.back);

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

                SharedPreferences studentPrefs = getSharedPreferences("StudentPrefs", MODE_PRIVATE);
                SharedPreferences.Editor studentEditor = studentPrefs.edit();
                studentEditor.clear();

                firebaseAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                switch (userType) {
                                    case "Admin":
                                        startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                                        break;

                                    case "Student":
                                        studentEditor.putString("LoggedInStudentUsername", username);
                                        studentEditor.apply();
                                        startActivity(new Intent(LoginActivity.this, StudentHomeActivity.class));
                                        break;

                                    case "Tutor":
                                        tutorEditor.putString("LoggedInTutorUsername", username);
                                        tutorEditor.apply();
                                        Intent intent = new Intent(LoginActivity.this, TutorBookingRequests.class);
                                        intent.putExtra("TUTOR_NAME", db.getLoggedInTutorName(username)); // Adjust if needed
                                        startActivity(intent);
                                        break;

                                    default:
                                        Toast.makeText(LoginActivity.this, "Error identifying user type", Toast.LENGTH_SHORT).show();
                                        return;
                                }
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Firebase login failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}
