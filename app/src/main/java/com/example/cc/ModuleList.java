package com.example.cc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.cc.databinding.ActivityModuleListBinding;
import java.util.ArrayList;
import java.util.List;

public class ModuleList extends AppCompatActivity {

    private ActivityModuleListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_list);

        String username = getIntent().getStringExtra("USERNAME");
        Toast.makeText(this, "Welcome, " + username, Toast.LENGTH_SHORT).show();

        // Initialize the ImageButton (make sure the ID matches your XML layout)
        ImageButton back = findViewById(R.id.back_button);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(ModuleList.this,StudentHomeActivity.class);
            startActivity(intent);
        });

        GridView gridView = findViewById(R.id.moduleGridView);

        // Get the module list from the intent
        ArrayList<Module> modules = (ArrayList<Module>) getIntent().getSerializableExtra("modules");

        if (modules != null) {
            ModuleGridAdapter adapter = new ModuleGridAdapter(this, modules);
            gridView.setAdapter(adapter);

            // Set an item click listener
            gridView.setOnItemClickListener((parent, view, position, id) -> {
                // Get the selected module
                Module selectedModule = modules.get(position);

                // Log the selected module details
                Log.d("ModuleList", "Selected Module ID: " + selectedModule.getId());
                Log.d("ModuleList", "Selected Module Name: " + selectedModule.getName());

                // Create an intent to start TutorList activity
                Intent intent = new Intent(ModuleList.this, TutorList.class);

                // Pass MODULE_ID, MODULE_NAME, and USERNAME to the TutorList activity
                intent.putExtra("MODULE_ID", String.valueOf(selectedModule.getId()));
                intent.putExtra("MODULE_NAME", selectedModule.getName());
                intent.putExtra("USERNAME", username);

                // Start the TutorList activity
                startActivity(intent);
            });

        } else {
            Log.e("ModuleList", "Modules list is null");
        }

    }
}
