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
        String moduleId = getIntent().getStringExtra("MODULE_ID");
        String moduleName = getIntent().getStringExtra("MODULE_NAME");

        ImageButton back = findViewById(R.id.back_button);

        tutorsListView = findViewById(R.id.tutorListView);
        dbHelper = new DatabaseHelper(this);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(TutorList.this,StudentHomeActivity.class);
            startActivity(intent);
        });

        Log.d("TutorList", "Received Module ID: " + moduleId);

        ArrayList<TutorProfile> tutorProfiles = dbHelper.getTutorsByModule(moduleId);

        Log.d("TutorList", "Number of Tutors Found: " + tutorProfiles.size());

        TutorProfileAdapter adapter = new TutorProfileAdapter(this, tutorProfiles);
        tutorsListView.setAdapter(adapter);

        tutorsListView.setOnItemClickListener((parent, view, position, id) -> {

            TutorProfile selectedTutor = tutorProfiles.get(position);

            Intent intent = new Intent(TutorList.this, TutorDetailActivity.class);

            intent.putExtra("TUTOR_NAME", selectedTutor.getName());
            intent.putExtra("FIRST_NAME", selectedTutor.getFirstName());
            intent.putExtra("LAST_NAME", selectedTutor.getLastName());
            intent.putExtra("PHONE_NUMBER", selectedTutor.getPhoneNumber());
            intent.putExtra("MODULE_NAME", moduleName);

            startActivity(intent);
        });
    }
}
