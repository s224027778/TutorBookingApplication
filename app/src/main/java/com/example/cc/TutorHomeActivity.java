package com.example.cc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cc.databinding.ActivityTutorHomeBinding;
import java.util.ArrayList;
import java.util.List;

public class TutorHomeActivity extends AppCompatActivity {

    private DatabaseHelper dbHelp;
    private ActivityTutorHomeBinding binding;
    private String tutorName; // Variable to hold the tutor's name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the logged-in tutor's username from shared preferences
        SharedPreferences tutorPrefs = getSharedPreferences("TutorPrefs", MODE_PRIVATE);
        String loggedInUsername = tutorPrefs.getString("LoggedInTutorUsername", null);
        if (loggedInUsername == null) {
            // Handle the error, e.g., show a message or redirect to login
            Toast.makeText(this, "User not logged in. Please log in again.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TutorHomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the TutorHomeActivity
            return; // Exit onCreate
        }

        // Initialize the DatabaseHelper
        dbHelp = new DatabaseHelper(this);

        // Retrieve the logged-in tutor's name from the database using the method
        tutorName = dbHelp.getLoggedInTutorName(loggedInUsername);

        // Set up the navigation and intents
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                Intent intent = new Intent(TutorHomeActivity.this, TutorHomeActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.bookingRequests) {
                // Pass the tutorName via the intent
                Intent intent = new Intent(TutorHomeActivity.this, TutorBookingRequests.class);
                intent.putExtra("TUTOR_NAME", tutorName);  // Pass the tutor's name
                startActivity(intent);
            } else if (itemId == R.id.chat) {

            } else if (itemId == R.id.settings) {

                Intent intent = new Intent(TutorHomeActivity.this, TutorSettings.class);
                startActivity(intent);
            }

            return true;
        });

        // Other code related to categories and modules
        ListView listView = findViewById(R.id.listView);
        List<Category> categories = dbHelp.getAllCategories();
        final List<String> categoryNames = new ArrayList<>();

        for (Category category : categories) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, categoryNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Category selectedCategory = categories.get(position);

            List<Module> modules = dbHelp.getModulesByCategory(selectedCategory.getId());

            Intent intent = new Intent(TutorHomeActivity.this, ModuleList.class);
            intent.putExtra("modules", new ArrayList<>(modules));
            startActivity(intent);
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
