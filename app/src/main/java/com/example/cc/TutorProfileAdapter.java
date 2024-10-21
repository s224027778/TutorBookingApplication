package com.example.cc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TutorProfileAdapter extends ArrayAdapter<TutorProfile> {
    public TutorProfileAdapter(Context context, ArrayList<TutorProfile> tutorProfiles) {
        super(context, 0, tutorProfiles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TutorProfile tutorProfile = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tutor_profile, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView firstNameTextView = convertView.findViewById(R.id.firstNameTextView);
        TextView lastNameTextView = convertView.findViewById(R.id.lastNameTextView);
        TextView phoneNumberTextView = convertView.findViewById(R.id.phoneNumberTextView);

        nameTextView.setText(tutorProfile.getName());
        firstNameTextView.setText(tutorProfile.getFirstName());
        lastNameTextView.setText(tutorProfile.getLastName());
        phoneNumberTextView.setText(tutorProfile.getPhoneNumber());

        return convertView;
    }
}
