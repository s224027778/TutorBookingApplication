package com.example.cc;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class BookingActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText editTextTutorName, editTextStudentName, editTextModuleName, editTextDuration, editTextDate, editTextTime;
    Button buttonBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        String tutorName = getIntent().getStringExtra("TUTOR_NAME");
        String moduleName = getIntent().getStringExtra("MODULE_NAME");

        db = new DatabaseHelper(this);
        editTextTime = findViewById(R.id.editTextTime);
        editTextTutorName = findViewById(R.id.editTextTutorname);
        editTextStudentName = findViewById(R.id.editTextStudentname);
        editTextModuleName = findViewById(R.id.editTextModulename);
        editTextDate = findViewById(R.id.editTextDate);
        editTextDuration = findViewById(R.id.editTextDuration);
        buttonBook = findViewById(R.id.buttonBook);
        ImageButton back = findViewById(R.id.back_button);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = sharedPreferences.getString("LoggedInStudentUsername", "");

        SharedPreferences studentPrefs = getSharedPreferences("StudentPrefs", MODE_PRIVATE);
        String loggedInUsername = studentPrefs.getString("LoggedInStudentUsername", null);

        Cursor res = db.getStudentProfile(loggedInUsername);
        if (res == null || res.getCount() == 0) {
            Toast.makeText(this, "Please create a profile in settings before booking a tutor", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(BookingActivity.this, StudentSettings.class);
            startActivity(intent);
            return;
        }
        res.close();

        editTextStudentName.setText(loggedInUsername);
        editTextStudentName.setEnabled(false);
        editTextTutorName.setText(tutorName);
        editTextTutorName.setEnabled(false);
        editTextModuleName.setText(moduleName);
        editTextModuleName.setEnabled(false);

        editTextDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        Calendar selectedDateCalendar = Calendar.getInstance();
                        selectedDateCalendar.set(selectedYear, selectedMonth, selectedDay);
                        if (selectedDateCalendar.before(calendar)) {
                            Toast.makeText(this, "Selected date has already passed", Toast.LENGTH_LONG).show();
                        } else {
                            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                            editTextDate.setText(selectedDate);
                        }
                    }, year, month, day);
            datePickerDialog.show();
        });


        editTextTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, selectedHour, selectedMinute) -> {
                        String selectedTime = selectedHour + ":" + (selectedMinute < 10 ? "0" : "") + selectedMinute;
                        editTextTime.setText(selectedTime);
                    }, hour, minute, true);
            timePickerDialog.show();
        });

        editTextDuration.setOnClickListener(v -> {
            String[] durations = {"30 mins", "1 hour", "1 hour 30 mins", "2 hours"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select duration")
                    .setItems(durations, (dialog, which) -> {
                        editTextDuration.setText(durations[which]);
                    });
            builder.show();
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(BookingActivity.this,StudentHomeActivity.class);
            startActivity(intent);
        });

        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tutorName = editTextTutorName.getText().toString();
                String studentName = editTextStudentName.getText().toString();
                String moduleName = editTextModuleName.getText().toString();
                String date = editTextDate.getText().toString();
                String time = editTextTime.getText().toString();
                String duration = editTextDuration.getText().toString();

                if (date.isEmpty())
                {
                    Toast.makeText(BookingActivity.this, "Date field cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (time.isEmpty())
                {
                    Toast.makeText(BookingActivity.this, "Time field cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (duration.isEmpty())
                {
                    Toast.makeText(BookingActivity.this, "Duration field cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                boolean isInserted = db.insertBooking(tutorName, studentName, moduleName, date, time, duration);
                if (isInserted) {
                    Toast.makeText(BookingActivity.this, "Booking requested, sending you to home page", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(BookingActivity.this,StudentHomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(BookingActivity.this, "Booking request failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}