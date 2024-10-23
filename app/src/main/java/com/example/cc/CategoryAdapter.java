package com.example.cc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {

    public CategoryAdapter(Context context, List<Category> categories) {
        super(context, 0, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Category category = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_list_item, parent, false);
        }

        // Lookup view for data population
        TextView categoryName = convertView.findViewById(R.id.categoryName);
        ImageView categoryIcon = convertView.findViewById(R.id.categoryIcon);

        // Populate the data into the template view
        categoryName.setText(category.getName());

        // Set an appropriate icon for the category (use logic based on category)
        categoryIcon.setImageResource(R.drawable.ic_category); // Update icon as necessary

        // Return the completed view to render on screen
        return convertView;
    }
}
