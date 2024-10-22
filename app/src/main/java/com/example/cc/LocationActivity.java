package com.example.cc;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    private Button getLocationButton;
    private ImageButton back;
    private TextView locationTextView;
    private ProgressBar locationProgressBar;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        getLocationButton = findViewById(R.id.getLocationButton);
        locationTextView = findViewById(R.id.locationTextView);
        locationProgressBar = findViewById(R.id.locationProgressBar);
        back = findViewById(R.id.back_button);

        // Initialize Fused Location Provider Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLocationButton.setOnClickListener(v -> {
            // Show progress bar while retrieving location
            locationProgressBar.setVisibility(View.VISIBLE);
            getLocation();
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(LocationActivity.this,TutorSettings.class);
            startActivity(intent);
        });
    }

    private void getLocation() {
        // Check if location permissions are granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // Get last known location
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    locationProgressBar.setVisibility(View.GONE);  // Hide progress bar

                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        // Update the TextView with latitude and longitude
                        locationTextView.setText("Lat: " + latitude + ", Long: " + longitude);

                        // Ensure Geocoder is available on the device
                        if (!Geocoder.isPresent()) {
                            locationTextView.append("\nGeocoder not available on this device.");
                            return;
                        }

                        // Convert latitude and longitude into an address using Geocoder
                        try {
                            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                            if (addresses != null && !addresses.isEmpty()) {
                                Address address = addresses.get(0);
                                String street = address.getThoroughfare();   // Street name
                                String city = address.getLocality();         // City
                                String postalCode = address.getPostalCode(); // Postal code
                                String suburb = address.getSubLocality();    // Suburb
                                String fullAddress = address.getAddressLine(0); // Full address

                                // Display the address details
                                locationTextView.append("\nStreet: " + street);
                                locationTextView.append("\nCity: " + city);
                                locationTextView.append("\nSuburb: " + suburb);
                                locationTextView.append("\nPostal Code: " + postalCode);
                                locationTextView.append("\nFull Address: " + fullAddress);
                            } else {
                                locationTextView.append("\nUnable to get address.");
                            }
                        } catch (IOException e) {
                            locationTextView.append("\nFailed to get address. " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        locationTextView.setText("Unable to retrieve location.");
                    }
                })
                .addOnFailureListener(e -> {
                    locationProgressBar.setVisibility(View.GONE);  // Hide progress bar
                    locationTextView.setText("Failed to retrieve location: " + e.getMessage());
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, re-call getLocation
                getLocation();
            } else {
                // Permission denied
                locationProgressBar.setVisibility(View.GONE);
                locationTextView.setText("Permission denied to retrieve location.");
            }
        }
    }
}

