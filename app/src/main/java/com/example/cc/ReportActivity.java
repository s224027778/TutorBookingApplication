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

public class ReportActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText editTextUsername, editTextReport;
    Button btnSubmit;
    private ImageButton back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        db = new DatabaseHelper(this);

        back = findViewById(R.id.back_button);
        editTextUsername = findViewById(R.id.et_username);
        editTextReport = findViewById(R.id.et_report_text);
        btnSubmit = findViewById(R.id.btn_submit_report);

        SharedPreferences studentPrefs = getSharedPreferences("StudentPrefs", MODE_PRIVATE);
        String username = studentPrefs.getString("LoggedInStudentUsername", null);

        editTextUsername.setText(username);
        editTextUsername.setEnabled(false);
        //btnSubmit = findViewById(R.id.btn_submit_report);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(ReportActivity.this,StudentSettings.class);
            startActivity(intent);
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String report = editTextReport.getText().toString();

                if (report.isEmpty())
                {
                    Toast.makeText(ReportActivity.this, "Report description field cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                boolean isInserted = db.insertReport(username, report);

                if (isInserted) {
                    Toast.makeText(ReportActivity.this, "Report sent", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ReportActivity.this, "Report failed to send", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}