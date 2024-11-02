package com.example.cc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cc.databinding.ActivityTutorBookingRequestsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TutorBookingRequests extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView bookingsListView;
    private String TutorName;  // Tutor name retrieved from shared preferences
    private ActivityTutorBookingRequestsBinding binding;
    private TextView noBookingsMessage;  // Reference to the "no bookings" message TextView

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorBookingRequestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Set the correct root view
        dbHelper = new DatabaseHelper(this);

        // Retrieve the logged-in tutor's name from shared preferences
        SharedPreferences tutorPrefs = getSharedPreferences("TutorPrefs", MODE_PRIVATE);
        TutorName = tutorPrefs.getString("LoggedInTutorUsername", null);
        if (TutorName == null) {
            // Handle the error, e.g., show a message or redirect to login
            Log.e("TutorBookingRequests", "User not logged in. Please log in again.");
            Intent intent = new Intent(TutorBookingRequests.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the activity
            return; // Exit onCreate
        }

        // Log the retrieved tutor name for debugging purposes
        Log.d("TutorBookingRequests", "TutorName from SharedPreferences: " + TutorName);

        bookingsListView = findViewById(R.id.listViewBookingRequests);
        noBookingsMessage = findViewById(R.id.noBookingsMessage);  // Initialize the message TextView

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        // Load bookings for the tutor retrieved from SharedPreferences
        loadBookingsForTutor(TutorName);

        // Set up the bottom navigation and handle intents or fragment replacements
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                Intent intent = new Intent(TutorBookingRequests.this, TutorBookingRequests.class);
                startActivity(intent);
            } else if (itemId == R.id.bookingRequests) {
                Intent intent = new Intent(TutorBookingRequests.this, ConfirmedBookingActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.chat) {
                Intent intent = new Intent(TutorBookingRequests.this, ChatActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.settings) {
                Intent intent = new Intent(TutorBookingRequests.this, TutorSettings.class);
                startActivity(intent);
            }
            return true;
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.update("status","Offline").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TutorBookingRequests", "Now User is Offline");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.update("status","Online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TutorBookingRequests", "Now User is Online");
            }
        });

    }

    private void loadBookingsForTutor(String tutorName) {
        Cursor cursor = dbHelper.getBookingsByTutorName(tutorName);

        List<String> bookings = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String bookingDetails = "Student: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_STUDENTNAME)) +
                        "\nModule: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_MODULENAME)) +
                        "\nDate: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_DATE)) +
                        "\nTime: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_TIME));
                bookings.add(bookingDetails);
            } while (cursor.moveToNext());
        }

        cursor.close();

        if (bookings.isEmpty()) {
            noBookingsMessage.setVisibility(View.VISIBLE);
            bookingsListView.setVisibility(View.GONE);
        } else {
            noBookingsMessage.setVisibility(View.GONE);
            bookingsListView.setVisibility(View.VISIBLE);

            // Use the custom adapter
            BookingAdapter adapter = new BookingAdapter(this, bookings);
            bookingsListView.setAdapter(adapter);
        }
    }

    // Inside TutorBookingRequests

    public void confirmBooking(String bookingDetails) {
        int bookingId = getBookingIdFromDetails(bookingDetails);
        dbHelper.updateBookingStatusInDatabase(bookingId, 1);
        Toast.makeText(this, "Booking confirmed.", Toast.LENGTH_LONG).show();
    }

    public void declineBooking(String bookingDetails) {
        int bookingId = getBookingIdFromDetails(bookingDetails);
        dbHelper.updateBookingStatusInDatabase(bookingId, 2);
        Toast.makeText(this, "Booking declined.", Toast.LENGTH_LONG).show();
    }

    @SuppressLint("Range")
    private int getBookingIdFromDetails(String bookingDetails) {
        // Assuming bookingDetails has lines structured as:
        // "Student: <studentName>\nModule: <moduleName>\nDate: <date>\nTime: <time>"
        String[] details = bookingDetails.split("\n");
        String studentName = details[0].split(": ")[1];
        String moduleName = details[1].split(": ")[1];
        String date = details[2].split(": ")[1];
        String time = details[3].split(": ")[1];

        // Query the database to find the matching bookingId
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COL_BOOKING_ID + " FROM " + DatabaseHelper.TABLE_NAME_BOOKING +
                " WHERE " + DatabaseHelper.COL_BOOKING_STUDENTNAME + " = ? AND " +
                DatabaseHelper.COL_BOOKING_MODULENAME + " = ? AND " +
                DatabaseHelper.COL_BOOKING_DATE + " = ? AND " +
                DatabaseHelper.COL_BOOKING_TIME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{studentName, moduleName, date, time});

        int bookingId = -1;
        if (cursor.moveToFirst()) {
            bookingId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_BOOKING_ID));
        }
        cursor.close();
        return bookingId;
    }


}
