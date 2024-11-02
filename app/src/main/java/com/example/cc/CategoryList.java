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

        List<Category> categories = dbHelp.getAllCategories();

        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Category selectedCategory = categories.get(position);

            List<Module> modules = dbHelp.getModulesByCategory(selectedCategory.getId());

            Intent intent = new Intent(CategoryList.this, ModuleList.class);
            intent.putExtra("modules", new ArrayList<>(modules));
            startActivity(intent);
        });
    }
}
