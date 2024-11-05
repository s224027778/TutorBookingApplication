package com.example.cc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TutorReportActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText editTextUsername, editTextReport;
    Button btnSubmit;
    private ImageButton back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_report);

        db = new DatabaseHelper(this);

        back = findViewById(R.id.back_button);
        editTextUsername = findViewById(R.id.et_username);
        editTextReport = findViewById(R.id.et_review_text);
        btnSubmit = findViewById(R.id.btn_submit_review);


        SharedPreferences tutorPrefs = getSharedPreferences("TutorPrefs", MODE_PRIVATE);
        String tutorName = tutorPrefs.getString("LoggedInTutorUsername", null);

        editTextUsername.setText(tutorName);
        editTextUsername.setEnabled(false);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(TutorReportActivity.this, TutorSettings.class);
            startActivity(intent);
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tutorName = editTextUsername.getText().toString();
                String report = editTextReport.getText().toString();

                if (report.isEmpty()) {
                    Toast.makeText(TutorReportActivity.this, "Report description field cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                boolean isInserted = db.insertReport(tutorName, report);

                if (isInserted) {
                    Toast.makeText(TutorReportActivity.this, "Report sent", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TutorReportActivity.this, "Report failed to send", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}