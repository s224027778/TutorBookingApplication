package com.example.cc;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ModuleGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Module> modules;

    public ModuleGridAdapter(Context context, ArrayList<Module> modules) {
        this.context = context;
        this.modules = modules;
    }

    @Override
    public int getCount() {
        return modules.size();
    }

    @Override
    public Object getItem(int position) {
        return modules.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_module, parent, false);
        }

        Module module = modules.get(position);

        ImageView moduleIcon = convertView.findViewById(R.id.moduleIcon);
        TextView moduleName = convertView.findViewById(R.id.moduleName);

        // Set the data
        moduleName.setText(module.getName());

        // Set a default icon or use an icon related to the module
        moduleIcon.setImageResource(R.drawable.ic_module_icon); // Change this to actual module icons

        return convertView;
    }
}
