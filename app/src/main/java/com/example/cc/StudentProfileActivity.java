package com.example.cc;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StudentProfileActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText editTextUsername, editTextFirstName, editTextLastName, editTextPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        db = new DatabaseHelper(this);
        editTextUsername = findViewById(R.id.userName);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);

        Button profileCreate = findViewById(R.id.createProfile);
        Button profileEdit = findViewById(R.id.editProfile);

        ImageButton back = findViewById(R.id.backButton);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(StudentProfileActivity.this,TutorSettings.class);
            startActivity(intent);
        });

        profileCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();

                Cursor res = db.getUserProfile(username);
                if (res == null || res.getCount() == 0) {
                    Toast.makeText(StudentProfileActivity.this, "User profile not found", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isInserted = db.insertStudentProfile(username, firstName, lastName, phoneNumber);
                if (isInserted) {
                    Toast.makeText(StudentProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                    // Get student ID by username
                    int studentId = db.getStudentIdByUsername(username);
                } else {
                    Toast.makeText(StudentProfileActivity.this, "Profile Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();

                Cursor res = db.getUserProfile(username);
                if (res == null || res.getCount() == 0) {
                    Toast.makeText(StudentProfileActivity.this, "User profile not found", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Call the updateStudentProfile method
                boolean isUpdated = db.updateStudentProfile(username, firstName, lastName, phoneNumber);
                if (isUpdated) {
                    Toast.makeText(StudentProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                    // Get tutor ID by username
                    int studentId = db.getStudentIdByUsername(username);
                } else {
                    Toast.makeText(StudentProfileActivity.this, "Profile Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}