package com.example.cc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

public class SignupActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText editTextUsername, editTextPassword, editTextPasswordConfirm;
    RadioGroup radioGroupUserType;
    Button buttonSignup;
    AppCompatImageView backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = new DatabaseHelper(this);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);
        radioGroupUserType = findViewById(R.id.radioGroupUserType);
        buttonSignup = findViewById(R.id.buttonSignup);
        backButton = findViewById(R.id.back);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this,MainActivity.class);
            startActivity(intent);
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextPasswordConfirm.getText().toString();
                int selectedId = radioGroupUserType.getCheckedRadioButtonId();

                // Check if any EditText is empty and set an error message
                if (username.isEmpty()) {
                    editTextUsername.setError("Username is required");
                    return;
                }
                if (password.isEmpty()) {
                    editTextPassword.setError("Password is required");
                    return;
                }
                if (confirmPassword.isEmpty()) {
                    editTextPasswordConfirm.setError("Confirm your password");
                    return;
                }

                // Check if a user type is selected
                if (selectedId == -1) {
                    Toast.makeText(SignupActivity.this, "Please select a user type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (db.checkUser(username)) {
                    Toast.makeText(SignupActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Now that we know a radio button is selected, safely get the text
                RadioButton radioButton = findViewById(selectedId);
                String userType = radioButton.getText().toString();

                // Validate the password
                if (validatePassword(password, confirmPassword)) {
                    boolean isInserted = db.insertData(username, password, userType);
                    if (isInserted) {
                        Toast.makeText(SignupActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignupActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private boolean validatePassword(String password, String confirmPassword) {
        if (password.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.matches(".*[0-9].*")) {
            Toast.makeText(this, "Password must contain at least one number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*")) {
            Toast.makeText(this, "Password must contain both uppercase and lowercase letters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}