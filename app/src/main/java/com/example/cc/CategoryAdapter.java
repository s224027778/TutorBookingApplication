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

        Category category = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_list_item, parent, false);
        }

        TextView categoryName = convertView.findViewById(R.id.categoryName);
        ImageView categoryIcon = convertView.findViewById(R.id.categoryIcon);

        categoryName.setText(category.getName());

        categoryIcon.setImageResource(R.drawable.ic_category);

        return convertView;
    }
}
