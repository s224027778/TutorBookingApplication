package com.example.cc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

        SharedPreferences studentPrefs = getSharedPreferences("StudentPrefs", MODE_PRIVATE);
        String loggedInUsername = studentPrefs.getString("LoggedInStudentUsername", null);

        dbHelp = new DatabaseHelper(this);

        if (loggedInUsername != null) {
            studentName = dbHelp.getLoggedInStudentName(loggedInUsername);
        } else {
            Log.e("StudentHomeActivity", "Logged-in username is null");
            Intent loginIntent = new Intent(StudentHomeActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

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

        List<Category> categories = dbHelp.getAllCategories();
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Category selectedCategory = categories.get(position);

            List<Module> modules = dbHelp.getModulesByCategory(selectedCategory.getId());

            Intent intent = new Intent(StudentHomeActivity.this, ModuleList.class);
            intent.putExtra("modules", new ArrayList<>(modules));
            startActivity(intent);
        });
    }
}

