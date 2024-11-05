package com.example.cc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TutorAvailability extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TimePicker startTimePicker, endTimePicker;
    private Button addAvailabilityButton;
    private CheckBox checkboxMonday, checkboxTuesday, checkboxWednesday, checkboxThursday, checkboxFriday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_availability);

        dbHelper = new DatabaseHelper(this);
        startTimePicker = findViewById(R.id.start_time_picker);
        endTimePicker = findViewById(R.id.end_time_picker);
        addAvailabilityButton = findViewById(R.id.add_availability_button);

        checkboxMonday = findViewById(R.id.checkbox_monday);
        checkboxTuesday = findViewById(R.id.checkbox_tuesday);
        checkboxWednesday = findViewById(R.id.checkbox_wednesday);
        checkboxThursday = findViewById(R.id.checkbox_thursday);
        checkboxFriday = findViewById(R.id.checkbox_friday);

        ImageView back = findViewById(R.id.back_button);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(TutorAvailability.this,TutorSettings.class);
            startActivity(intent);
        });

        addAvailabilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAvailability();
            }
        });
    }

    private void addAvailability() {
        SharedPreferences tutorPrefs = getSharedPreferences("TutorPrefs", MODE_PRIVATE);
        String tutorName = tutorPrefs.getString("LoggedInTutorUsername", null);

        if (tutorName == null) {
            Toast.makeText(this, "No tutor logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        int startHour = startTimePicker.getCurrentHour();
        startTimePicker.setIs24HourView(true);
        int startMinute = startTimePicker.getCurrentMinute();
        int endHour = endTimePicker.getCurrentHour();
        endTimePicker.setIs24HourView(true);
        int endMinute = endTimePicker.getCurrentMinute();

        if (startHour > endHour || (startHour == endHour && startMinute >= endMinute)) {
            Toast.makeText(this, "Enter a valid time range", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> selectedDays = new ArrayList<>();
        if (checkboxMonday.isChecked()) selectedDays.add("Monday");
        if (checkboxTuesday.isChecked()) selectedDays.add("Tuesday");
        if (checkboxWednesday.isChecked()) selectedDays.add("Wednesday");
        if (checkboxThursday.isChecked()) selectedDays.add("Thursday");
        if (checkboxFriday.isChecked()) selectedDays.add("Friday");

        for (String day : selectedDays) {
            dbHelper.addTutorAvailability(tutorName, day, startHour, startMinute, endHour, endMinute);
        }

        Toast.makeText(this, "Availability added for selected days", Toast.LENGTH_SHORT).show();
    }
}