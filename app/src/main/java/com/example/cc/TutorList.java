package com.example.cc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class TutorList extends AppCompatActivity {

    DatabaseHelper dbHelper;
    ListView tutorsListView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_list);

        // Initialize the ImageButton (make sure the ID matches your XML layout)
        ImageButton back = findViewById(R.id.back_button);

        tutorsListView = findViewById(R.id.tutorListView);
        dbHelper = new DatabaseHelper(this);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(TutorList.this,StudentHomeActivity.class);
            startActivity(intent);
        });

        // Get the module ID passed from the previous activity
        String moduleId = getIntent().getStringExtra("MODULE_ID");

        // Log the received module ID
        Log.d("TutorList", "Received Module ID: " + moduleId);

        // Fetch tutors associated with the module
        ArrayList<TutorProfile> tutorProfiles = dbHelper.getTutorsByModule(moduleId);

        // Log the number of tutors found
        Log.d("TutorList", "Number of Tutors Found: " + tutorProfiles.size());

        // Create an adapter and set it to the ListView
        TutorProfileAdapter adapter = new TutorProfileAdapter(this, tutorProfiles);
        tutorsListView.setAdapter(adapter);

        tutorsListView.setOnItemClickListener((parent, view, position, id) -> {
            // Get selected tutor profile
            TutorProfile selectedTutor = tutorProfiles.get(position);

            // Create an intent to start TutorDetailActivity
            Intent intent = new Intent(TutorList.this, TutorDetailActivity.class);

            // Pass tutor details to TutorDetailActivity
            intent.putExtra("TUTOR_NAME", selectedTutor.getName());
            intent.putExtra("FIRST_NAME", selectedTutor.getFirstName());
            intent.putExtra("LAST_NAME", selectedTutor.getLastName());
            intent.putExtra("PHONE_NUMBER", selectedTutor.getPhoneNumber());

            // Start TutorDetailActivity
            startActivity(intent);
        });
    }
}
