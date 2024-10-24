package com.example.cc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class StudentAboutUs extends AppCompatActivity {
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        back = findViewById(R.id.back_button);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(StudentAboutUs.this,StudentSettings.class);
            startActivity(intent);
        });
    }
}