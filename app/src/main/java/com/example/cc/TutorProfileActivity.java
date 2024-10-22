package com.example.cc;

import static com.example.cc.R.id.back_button;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class TutorProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseHelper db;
    EditText editTextUsername, editTextFirstName, editTextLastName, editTextPhoneNumber;
    private ImageButton back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);

        db = new DatabaseHelper(this);
        db.insertPrices(); // This populates the database

        editTextUsername = findViewById(R.id.userName);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        back = findViewById(R.id.backButton);

        Spinner moduleSpinner = findViewById(R.id.moduleSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, db.getModuleNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moduleSpinner.setAdapter(adapter);
        moduleSpinner.setOnItemSelectedListener(this);

        Spinner priceSpinner = findViewById(R.id.priceSpinner);
        List<String> prices = db.getAllPrices();


        ArrayAdapter<String> priceAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, prices);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceSpinner.setAdapter(priceAdapter);

        priceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPrice = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + selectedPrice, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        Button profileCreate = findViewById(R.id.createProfile);
        Button profileEdit = findViewById(R.id.editProfile);

        profileCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();

                Cursor res = db.getUserProfile(username);
                if (res == null || res.getCount() == 0) {
                    Toast.makeText(TutorProfileActivity.this, "User profile not found", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isInserted = db.insertProfile(username, firstName, lastName, phoneNumber);
                if (isInserted) {
                    Toast.makeText(TutorProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                    // Get tutor ID by username
                    int tutorId = db.getTutorIdByUsername(username);

                    // Assign modules to the tutor
                    Spinner moduleSpinner = findViewById(R.id.moduleSpinner);
                    String selectedModule = moduleSpinner.getSelectedItem().toString();
                    int moduleId = db.getModuleIdByName(selectedModule);

                    db.assignTutorToModule(tutorId, moduleId);

                    // Hide the create profile button
                    v.setVisibility(View.GONE);

                    Intent intent = new Intent(TutorProfileActivity.this, TutorBookingRequests.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(TutorProfileActivity.this, "Profile Update Failed", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(TutorProfileActivity.this, "User profile not found", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Call the updateProfile method
                boolean isUpdated = db.updateProfile(username, firstName, lastName, phoneNumber);
                if (isUpdated) {
                    Toast.makeText(TutorProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                    // Get tutor ID by username
                    int tutorId = db.getTutorIdByUsername(username);

                    // Assign modules to the tutor
                    Spinner moduleSpinner = findViewById(R.id.moduleSpinner);
                    String selectedModule = moduleSpinner.getSelectedItem().toString();
                    int moduleId = db.getModuleIdByName(selectedModule);

                    db.assignTutorToModule(tutorId, moduleId);

                    Intent intent = new Intent(TutorProfileActivity.this, TutorBookingRequests.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(TutorProfileActivity.this, "Profile Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(TutorProfileActivity.this,TutorSettings.class);
            startActivity(intent);
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String moduleText = parent.getItemAtPosition(position).toString();
        // toast msg
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}