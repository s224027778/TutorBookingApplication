package com.example.cc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.cc.databinding.ActivityTutorListBinding;
import java.util.ArrayList;

public class TutorList extends AppCompatActivity {

    DatabaseHelper dbHelper;
    ListView tutorsListView;
    private ActivityTutorListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tutorsListView = findViewById(R.id.tutorListView);
        dbHelper = new DatabaseHelper(this);

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

        // Set up the bottom navigation and handle intents or fragment replacements
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                // Avoid reloading the activity unnecessarily if already on it
                Intent intent = new Intent(TutorList.this, StudentHomeActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.bookingRequests) {
                Intent intent = new Intent(TutorList.this, TutorBookingRequests.class);
                startActivity(intent);
            } else if (itemId == R.id.chat) {

            } else if (itemId == R.id.settings) {

            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);  // Ensure the frame_layout exists in your layout
        fragmentTransaction.commit();
    }
}
