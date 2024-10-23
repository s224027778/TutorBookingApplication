package com.example.cc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class CategoryList extends AppCompatActivity {

    private DatabaseHelper dbHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        ListView listView = findViewById(R.id.listView);
        dbHelp = new DatabaseHelper(this);

        // Get categories from the database
        List<Category> categories = dbHelp.getAllCategories();

        // Create and set the custom adapter
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        listView.setAdapter(adapter);

        // Set an item click listener
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Category selectedCategory = categories.get(position);

            // Fetch the modules for the selected category
            List<Module> modules = dbHelp.getModulesByCategory(selectedCategory.getId());

            // Create an intent to start the ModuleListActivity
            Intent intent = new Intent(CategoryList.this, ModuleList.class);
            intent.putExtra("modules", new ArrayList<>(modules)); // Convert List to ArrayList
            startActivity(intent);
        });
    }
}
