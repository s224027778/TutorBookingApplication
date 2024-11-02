package com.example.cc;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Button getLocationButton, addLocation;
    private ImageButton back;
    private TextView locationTextView;
    private ProgressBar locationProgressBar;
    private FusedLocationProviderClient fusedLocationClient;
    private double currentLatitude;
    private double currentLongitude;
    private String currentAddress;
    private String tutorName;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        getLocationButton = findViewById(R.id.getLocationButton);
        locationTextView = findViewById(R.id.locationTextView);
        locationProgressBar = findViewById(R.id.locationProgressBar);
        back = findViewById(R.id.back_button);
        addLocation = findViewById(R.id.addLocationButton);

        db = new DatabaseHelper(this);

        SharedPreferences tutorPrefs = getSharedPreferences("TutorPrefs", MODE_PRIVATE);
        tutorName = tutorPrefs.getString("LoggedInTutorUsername", null);

        // Initialize fused location provider client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLocationButton.setOnClickListener(v -> {
            locationProgressBar.setVisibility(View.VISIBLE);
            getLocation();
        });

        addLocation.setOnClickListener(v -> {
            insertLocation(tutorName, currentLatitude, currentLongitude, currentAddress);
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(LocationActivity.this, TutorSettings.class);
            startActivity(intent);
        });
    }

    private void getLocation() {
        // Check location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // Get location
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    locationProgressBar.setVisibility(View.GONE);

                    if (location != null) {
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();

                        locationTextView.setText("Lat: " + currentLatitude + ", Long: " + currentLongitude);

                        // Convert latitude and longitude into an address using Geocoder
                        try {
                            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);

                            if (addresses != null && !addresses.isEmpty()) {
                                Address address = addresses.get(0);
                                currentAddress = address.getAddressLine(0);

                                // Display the address details
                                locationTextView.append("\nFull Address: " + currentAddress);
                            } else {
                                Log.e("LoginActivity","Unable to get address.");
                            }
                        } catch (IOException e) {
                            locationTextView.append("\nFailed to get address. " + e.getMessage());
                        }
                    } else {
                        Toast.makeText(LocationActivity.this, "Unable to retrieve location", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> {
                    locationProgressBar.setVisibility(View.GONE);
                    Toast.makeText(LocationActivity.this, "Failed to retrieve location", Toast.LENGTH_LONG).show();
                });
    }

    private void insertLocation(String tutorName, double latitude, double longitude, String address) {
        if (latitude != 0 && longitude != 0 && address != null) {
            boolean insertSuccess = db.insertLocationData(tutorName, latitude, longitude, address);
            if (insertSuccess) {
                Toast.makeText(this, "Location added to profile.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to add location to profile.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Location data is incomplete", Toast.LENGTH_LONG).show();
        }
    }
}

