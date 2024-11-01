package com.example.cc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cc.databinding.ActivityStudentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class StudentHomeActivity extends AppCompatActivity {
    private DatabaseHelper dbHelp;
    private ActivityStudentHomeBinding binding;
    private String studentName;
    private String loggedInUsername;  // Ensure this is defined as an instance variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve username from Intent and SharedPreferences
        String username = getIntent().getStringExtra("USERNAME");
        SharedPreferences userSession = getSharedPreferences("UserSession", MODE_PRIVATE);
        loggedInUsername = userSession.getString("LoggedInStudentUsername", username);

        // Initialize DatabaseHelper and retrieve student name
        dbHelp = new DatabaseHelper(this);
        studentName = dbHelp.getLoggedInStudentName(loggedInUsername);

        // Display a Toast to verify `studentName` retrieval
        if (studentName != null) {
            Toast.makeText(this, "Welcome, " + studentName, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Student name not found", Toast.LENGTH_SHORT).show();
            Log.d("StudentHomeActivity", "studentName is null, using loggedInUsername instead.");
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                startActivity(new Intent(StudentHomeActivity.this, StudentHomeActivity.class));
            } else if (itemId == R.id.bookingRequests) {
                Intent intent = new Intent(StudentHomeActivity.this, StudentSessions.class);
                intent.putExtra("STUDENT_NAME", studentName != null ? studentName : loggedInUsername); // Fall back to `loggedInUsername` if needed
                startActivity(intent);
            } else if (itemId == R.id.chat) {
                startActivity(new Intent(StudentHomeActivity.this, StudentChatActivity.class));
            } else if (itemId == R.id.settings) {
                startActivity(new Intent(StudentHomeActivity.this, StudentSettings.class));
            }

            return true;
        });

        ListView listView = findViewById(R.id.listView);

        // Get categories from the database and set up the adapter
        List<Category> categories = dbHelp.getAllCategories();
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        listView.setAdapter(adapter);

        // Set an item click listener for category selection
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Category selectedCategory = categories.get(position);

            // Fetch modules for the selected category
            List<Module> modules = dbHelp.getModulesByCategory(selectedCategory.getId());

            // Start ModuleListActivity with modules and student name or loggedInUsername if studentName is null
            Intent moduleIntent = new Intent(StudentHomeActivity.this, ModuleList.class);
            moduleIntent.putExtra("modules", new ArrayList<>(modules));
            moduleIntent.putExtra("STUDENT_NAME", studentName != null ? studentName : loggedInUsername);
            startActivity(moduleIntent);
        });
    }
}

