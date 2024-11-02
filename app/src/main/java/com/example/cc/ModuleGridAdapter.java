package com.example.cc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_module, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Module module = modules.get(position);
        holder.moduleName.setText(module.getName());
        holder.moduleIcon.setImageResource(R.drawable.ic_category);

        return convertView;
    }

    private static class ViewHolder {
        final ImageView moduleIcon;
        final TextView moduleName;

        ViewHolder(View view) {
            moduleIcon = view.findViewById(R.id.moduleIcon);
            moduleName = view.findViewById(R.id.moduleName);
        }
    }
}

