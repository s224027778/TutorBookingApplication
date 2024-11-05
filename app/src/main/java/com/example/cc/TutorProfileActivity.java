package com.example.cc;

import static com.example.cc.R.id.back_button;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TutorProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseHelper db;
    EditText editTextUsername, editTextFirstName, editTextLastName, editTextPhoneNumber;
    private ImageButton back;
    CountryCodePicker countryCodePicker;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;
    ImageView mviewuserimageinimageview;
    StorageReference storageReference;
    private String ImageURIacessToken;
    FirebaseStorage firebaseStorage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);

        db = new DatabaseHelper(this);
        db.insertPrices();

        editTextUsername = findViewById(R.id.userName);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        back = findViewById(R.id.backButton);
        countryCodePicker = findViewById(R.id.tutorcountryCode);

        mviewuserimageinimageview=findViewById(R.id.viewuserimageinimageview);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        storageReference=firebaseStorage.getReference();
        storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageURIacessToken=uri.toString();
                Picasso.get().load(uri).into(mviewuserimageinimageview);

            }
        });

        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userprofile muserprofile=snapshot.getValue(userprofile.class);
                editTextUsername.setText(muserprofile.getUsername());
                editTextUsername.setEnabled(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TutorProfileActivity","Failed To Fetch");
            }
        });

        Spinner moduleSpinner = findViewById(R.id.moduleSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, db.getModuleNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moduleSpinner.setAdapter(adapter);
        moduleSpinner.setOnItemSelectedListener(this);

        Spinner priceSpinner = findViewById(R.id.priceSpinner);
        List<String> prices = db.getAllPrices();


        ArrayAdapter<String> priceAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, prices);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceSpinner.setAdapter(priceAdapter);

        priceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPrice = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button profileCreate = findViewById(R.id.createProfile);
        Button profileEdit = findViewById(R.id.editProfile);

        profileCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();

                if (firstName.isEmpty())
                {
                    Toast.makeText(TutorProfileActivity.this, "First name is required", Toast.LENGTH_LONG).show();
                    return;
                }
                if (firstName.matches(".[0-9]."))
                {
                    Toast.makeText(TutorProfileActivity.this, "First name cannot contain numbers", Toast.LENGTH_LONG).show();
                    return;
                }
                if (lastName.isEmpty())
                {
                    Toast.makeText(TutorProfileActivity.this, "First name cannot contain numbers", Toast.LENGTH_LONG).show();
                    return;
                }
                if (lastName.matches(".[0-9]."))
                {
                    Toast.makeText(TutorProfileActivity.this, "Last name cannot contain numbers", Toast.LENGTH_LONG).show();
                    return;
                }
                if (phoneNumber.isEmpty())
                {
                    Toast.makeText(TutorProfileActivity.this, "Phone number is required", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!validatePhoneNumber()){
                    return;
                }

                Cursor res = db.getUserProfile(username);
                if (res == null || res.getCount() == 0) {
                    Log.e("TutorProfileActivity", "User profile not found");
                    res.close();
                    return;
                }

                Cursor res1 = db.getTutorProfile(username);
                if (res1 != null && res1.getCount() > 0) {
                    Toast.makeText(TutorProfileActivity.this, "Tutor profile already exists", Toast.LENGTH_LONG).show();
                    res1.close();
                    return;
                }

                boolean isInserted = db.insertProfile(username, firstName, lastName, phoneNumber);
                if (isInserted) {
                    Toast.makeText(TutorProfileActivity.this, "Profile Created", Toast.LENGTH_LONG).show();

                    int tutorId = db.getTutorIdByUsername(username);

                    Spinner moduleSpinner = findViewById(R.id.moduleSpinner);
                    String selectedModule = moduleSpinner.getSelectedItem().toString();
                    int moduleId = db.getModuleIdByName(selectedModule);

                    db.assignTutorToModule(tutorId, moduleId);

                    Intent intent = new Intent(TutorProfileActivity.this, TutorAvailability.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(TutorProfileActivity.this, "Profile Not Created", Toast.LENGTH_SHORT).show();
                }
            }
        });


        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();

                if (firstName.isEmpty())
                {
                    Toast.makeText(TutorProfileActivity.this, "First name is required", Toast.LENGTH_LONG).show();
                    return;
                }
                if (firstName.matches(".[0-9]."))
                {
                    Toast.makeText(TutorProfileActivity.this, "First name cannot contain numbers", Toast.LENGTH_LONG).show();
                    return;
                }
                if (lastName.isEmpty())
                {
                    Toast.makeText(TutorProfileActivity.this, "First name cannot contain numbers", Toast.LENGTH_LONG).show();
                    return;
                }
                if (lastName.matches(".[0-9]."))
                {
                    Toast.makeText(TutorProfileActivity.this, "Last name cannot contain numbers", Toast.LENGTH_LONG).show();
                    return;
                }
                if (phoneNumber.isEmpty())
                {
                    Toast.makeText(TutorProfileActivity.this, "Phone number is required", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!validatePhoneNumber()){
                    return;
                }

                boolean isUpdated = db.updateProfile(username, firstName, lastName, phoneNumber);
                if (isUpdated) {
                    Toast.makeText(TutorProfileActivity.this, "Profile Updated", Toast.LENGTH_LONG).show();

                    int tutorId = db.getTutorIdByUsername(username);

                    Spinner moduleSpinner = findViewById(R.id.moduleSpinner);
                    String selectedModule = moduleSpinner.getSelectedItem().toString();
                    int moduleId = db.getModuleIdByName(selectedModule);
                    db.assignTutorToModule(tutorId, moduleId);

                } else {
                    Toast.makeText(TutorProfileActivity.this, "Profile Update Failed", Toast.LENGTH_LONG).show();
                }
            }
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(TutorProfileActivity.this,TutorSettings.class);
            startActivity(intent);
        });

    }
    private boolean validatePhoneNumber() {
        String countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();
        String phoneNumber = editTextPhoneNumber.getText().toString();

        if (countryCode.equals("+27")) {
            if (phoneNumber.length() != 10) {
                Toast.makeText(TutorProfileActivity.this,"Mobile number should contain 10 digits", Toast.LENGTH_LONG).show();
                return false;
            }
        } else{
            Toast.makeText(TutorProfileActivity.this, "South African numbers should have +27", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String moduleText = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}