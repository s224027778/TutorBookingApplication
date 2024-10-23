package com.example.cc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
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

        // Initialize the ImageButton (make sure the ID matches your XML layout)
        ImageButton back = findViewById(R.id.back_button);

        ListView listView = findViewById(R.id.moduleView);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(ModuleList.this,StudentHomeActivity.class);
            startActivity(intent);
        });

        // Get the module list from the intent
        ArrayList<Module> modules = (ArrayList<Module>) getIntent().getSerializableExtra("modules");

        if (modules != null) {
            List<String> moduleNames = new ArrayList<>();
            for (Module module : modules) {
                moduleNames.add(module.getName());
            }

            // Create an ArrayAdapter to display the modules
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, moduleNames);
            listView.setAdapter(adapter);

            // Set an item click listener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Get the selected module
                    Module selectedModule = modules.get(position);

                    // Log the selected module ID
                    Log.d("ModuleList", "Selected Module ID: " + selectedModule.getId());

                    // Create an intent to start TutorList activity
                    Intent intent = new Intent(ModuleList.this, TutorList.class);

                    // Pass the MODULE_ID to the TutorList activity
                    intent.putExtra("MODULE_ID", String.valueOf(selectedModule.getId()));

                    // Start the TutorList activity
                    startActivity(intent);
                }
            });


        } else {
            // Handle the case where modules are null
            Log.e("ModuleList", "Modules list is null");
        }
    }
}
