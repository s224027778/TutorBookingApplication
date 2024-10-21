package com.example.cc;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ModuleAdapter extends ArrayAdapter<Module> {

    public ModuleAdapter(@NonNull Context context, @NonNull List<Module> modules) {
        super(context, 0, modules);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Inflate the view if it doesn't exist
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Get the current module
        Module module = getItem(position);

        // Find the TextView in the list_item layout
        TextView textView = convertView.findViewById(android.R.id.text1); // Ensure this ID matches your list_item.xml

        // Set the module name to the TextView
        if (textView != null && module != null) {
            textView.setText(module.getName());
        } else {
            textView.setText("Error"); // For debugging purposes
        }

        return convertView;
    }
}
