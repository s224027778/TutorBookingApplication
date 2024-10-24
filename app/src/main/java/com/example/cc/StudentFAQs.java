package com.example.cc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class StudentFAQs extends AppCompatActivity {

    private ListView faqListView;
    private DatabaseHelper dbHelper;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        // Initialize the ListView
        faqListView = findViewById(R.id.faqListView);
        back = findViewById(R.id.back_button);

        // Initialize the database helper
        dbHelper = new DatabaseHelper(this);

        // Insert FAQs (Optional: Do this only once or conditionally, e.g., during app setup)
        insertDefaultFAQs();

        // Load FAQ data from the database
        loadFAQData();

        back.setOnClickListener(v -> {
            Intent intent = new Intent(StudentFAQs.this,StudentSettings.class);
            startActivity(intent);
        });
    }

    private void insertDefaultFAQs() {
        // Example: Insert default FAQs only if none exist (prevents duplication)
        if (dbHelper.getAllFAQs().isEmpty()) {
            dbHelper.insertFAQ("Is there a fee for using the app?", "The app is free to download and use. However, individual tutors may set their own rates for tutoring sessions.");
            dbHelper.insertFAQ("How do I book a tutoring session?", "Once logged in as a student, browse through the list of available tutors, view their profiles, and select the one that matches your needs. You can book a session by clicking the \"Book a Tutor\" button on their profile.");
            dbHelper.insertFAQ("Can I view my booking history?", "Yes, students can view all their previous and upcoming bookings in the \"My Bookings\" section, accessible from the app menu.");
            dbHelper.insertFAQ("Can I reschedule or cancel a session?", "No, you can not reschedule or cancel a booked session");
            dbHelper.insertFAQ("How do I report a problem with a tutor or a session?", "If you encounter any issues, you can report them by sending an email to our support team. Our team will investigate and assist you accordingly.");
            dbHelper.insertFAQ("What happens if a tutor does not show up for a session?", "If a tutor fails to show up, you can report the issue, and we will follow up with the tutor. Depending on the situation, you may be entitled to a refund or rescheduling.");
        }
    }

    private void loadFAQData() {
        // Fetch the FAQ data from the database
        List<FAQ> faqList = dbHelper.getAllFAQs();

        // If there are no FAQs, display a message
        if (faqList.isEmpty()) {
            Toast.makeText(this, "No FAQs available at the moment.", Toast.LENGTH_LONG).show();
        } else {
            // Set up the adapter to display FAQs in the ListView
            FAQAdapter faqAdapter = new FAQAdapter(this, faqList);
            faqListView.setAdapter(faqAdapter);
        }
    }
}
