package com.example.cc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cc.databinding.ActivityStudentHomeBinding;
import java.util.ArrayList;
import java.util.List;

public class StudentHomeActivity extends AppCompatActivity {
    private DatabaseHelper dbHelp;
    private ActivityStudentHomeBinding binding;
    String studentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setBackground(null);

        SharedPreferences userSession = getSharedPreferences("UserSession", MODE_PRIVATE);
        String loggedInUsername = userSession.getString("LoggedInStudentUsername", null);

        // Initialize the DatabaseHelper
        dbHelp = new DatabaseHelper(this);

        // Retrieve the logged-in student's name from the database using the method
        studentName = dbHelp.getLoggedInStudentName(loggedInUsername);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                Intent homeIntent = new Intent(StudentHomeActivity.this, StudentHomeActivity.class);
                startActivity(homeIntent);
            } else if (itemId == R.id.bookingRequests) {
                Intent intent = new Intent(StudentHomeActivity.this, StudentSessions.class);
                intent.putExtra("STUDENT_NAME", studentName);
                startActivity(intent);
            } else if (itemId == R.id.chat) {
               Intent intent = new Intent(StudentHomeActivity.this, StudentChatActivity.class);
               startActivity(intent);
            } else if (itemId == R.id.settings) {
                Intent intent = new Intent(StudentHomeActivity.this, StudentSettings.class);
                startActivity(intent);
            }

            return true;
        });

        ListView listView = findViewById(R.id.listView);
        dbHelp = new DatabaseHelper(this);

        // Get categories from the database
        List<Category> categories = dbHelp.getAllCategories();

        // Create a custom CategoryAdapter
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        listView.setAdapter(adapter);

        // Set an item click listener
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Category selectedCategory = categories.get(position);

            // Fetch the modules for the selected category
            List<Module> modules = dbHelp.getModulesByCategory(selectedCategory.getId());

            // Create an intent to start the ModuleListActivity
            Intent intent = new Intent(StudentHomeActivity.this, ModuleList.class);
            intent.putExtra("modules", new ArrayList<>(modules)); // Convert List to ArrayList
            startActivity(intent);
        });
    }
}
