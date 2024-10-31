package com.example.cc;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
        String username = getIntent().getStringExtra("USERNAME");

        db = new DatabaseHelper(this);
        editTextTime = findViewById(R.id.editTextTime);
        editTextTutorName = findViewById(R.id.editTextTutorname);
        editTextStudentName = findViewById(R.id.editTextStudentname);
        editTextModuleName = findViewById(R.id.editTextModulename);
        editTextDate = findViewById(R.id.editTextDate);
        editTextDuration = findViewById(R.id.editTextDuration);
        buttonBook = findViewById(R.id.buttonBook);

        editTextTutorName.setText(tutorName);
        editTextTutorName.setEnabled(false);

        editTextStudentName.setText(username);
        editTextStudentName.setEnabled(false);

        editTextModuleName.setText(moduleName);
        editTextModuleName.setEnabled(false);

        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final String[] durationOptions = {
                "30 mins", "1 hr", "1 hr 30 mins", "2 hrs", "2 hrs 30 mins", "3 hrs"
        };
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current time
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Create a TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(BookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Format the time as needed (e.g., HH:mm or h:mm a)
                        String time = String.format("%02d:%02d", hourOfDay, minute);
                        editTextTime.setText(time);
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(BookingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1; // Month is zero-based
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month - 1, dayOfMonth);

                        Calendar today = Calendar.getInstance();
                        today.set(Calendar.HOUR_OF_DAY, 0);
                        today.set(Calendar.MINUTE, 0);
                        today.set(Calendar.SECOND, 0);
                        today.set(Calendar.MILLISECOND, 0);

                        if (selectedDate.before(today)) {
                            Toast.makeText(BookingActivity.this, "Please select a future date.", Toast.LENGTH_SHORT).show();
                        } else {
                            String date = dayOfMonth + "/" + month + "/" + year;
                            editTextDate.setText(date);
                        }
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        editTextDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this);
                builder.setTitle("Select Duration")
                        .setItems(durationOptions, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Set the selected duration in the EditText
                                editTextDuration.setText(durationOptions[which]);
                            }
                        });
                builder.show();
            }
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

                boolean isInserted = db.insertBooking(tutorName, studentName, moduleName, date, time, duration);
                if (isInserted) {
                    Toast.makeText(BookingActivity.this, "Booking requested, sending you to home page", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BookingActivity.this,StudentHomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(BookingActivity.this, "Booking request failed, no student profile found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}