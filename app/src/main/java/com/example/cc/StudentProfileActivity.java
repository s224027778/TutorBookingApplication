package com.example.cc;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class StudentProfileActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText editTextUsername, editTextFirstName, editTextLastName, editTextPhoneNumber;
    CountryCodePicker countryCodePicker;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;
    ImageView mviewuserimageinimageview;
    StorageReference storageReference;
    private String ImageURIacessToken;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        db = new DatabaseHelper(this);
        editTextUsername = findViewById(R.id.userName);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        countryCodePicker = findViewById(R.id.studentcountryCode);

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
                Toast.makeText(getApplicationContext(),"Failed To Fetch",Toast.LENGTH_SHORT).show();
            }
        });


        Button profileCreate = findViewById(R.id.createProfile);
        Button profileEdit = findViewById(R.id.editProfile);

        ImageButton back = findViewById(R.id.backButton);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(StudentProfileActivity.this,StudentSettings.class);
            startActivity(intent);
        });

        profileCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();

                if (firstName.isEmpty())
                {
                    Toast.makeText(StudentProfileActivity.this, "First name is required", Toast.LENGTH_LONG).show();
                    return;
                }
                if (firstName.matches(".[0-9]."))
                {
                    Toast.makeText(StudentProfileActivity.this, "First name cannot contain numbers", Toast.LENGTH_LONG).show();
                    return;
                }
                if (lastName.isEmpty())
                {
                    Toast.makeText(StudentProfileActivity.this, "First name cannot contain numbers", Toast.LENGTH_LONG).show();
                    return;
                }
                if (lastName.matches(".[0-9]."))
                {
                    Toast.makeText(StudentProfileActivity.this, "Last name cannot contain numbers", Toast.LENGTH_LONG).show();
                    return;
                }
                if (phoneNumber.isEmpty())
                {
                    Toast.makeText(StudentProfileActivity.this, "Phone number is required", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!validatePhoneNumber()){
                    return;
                }

                Cursor res1 = db.getStudentProfile(username);
                if (res1 != null && res1.getCount() > 0) {
                    Toast.makeText(StudentProfileActivity.this, "Student profile already exists", Toast.LENGTH_LONG).show();
                    res1.close();
                    return;
                }

                boolean isInserted = db.insertStudentProfile(username, firstName, lastName, phoneNumber);
                if (isInserted) {
                    Toast.makeText(StudentProfileActivity.this, "Profile Created", Toast.LENGTH_SHORT).show();

                    int studentId = db.getStudentIdByUsername(username);
                } else {
                    Toast.makeText(StudentProfileActivity.this, "Profile not created", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(StudentProfileActivity.this, "First name is required", Toast.LENGTH_LONG).show();
                    return;
                }
                if (firstName.matches(".[0-9]."))
                {
                    Toast.makeText(StudentProfileActivity.this, "First name cannot contain numbers", Toast.LENGTH_LONG).show();
                    return;
                }
                if (lastName.isEmpty())
                {
                    Toast.makeText(StudentProfileActivity.this, "First name cannot contain numbers", Toast.LENGTH_LONG).show();
                    return;
                }
                if (lastName.matches(".[0-9]."))
                {
                    Toast.makeText(StudentProfileActivity.this, "Last name cannot contain numbers", Toast.LENGTH_LONG).show();
                    return;
                }
                if (phoneNumber.isEmpty())
                {
                    Toast.makeText(StudentProfileActivity.this, "Phone number is required", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!validatePhoneNumber()){
                    return;
                }


                boolean isUpdated = db.updateStudentProfile(username, firstName, lastName, phoneNumber);
                if (isUpdated) {
                    Toast.makeText(StudentProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                    int studentId = db.getStudentIdByUsername(username);
                } else {
                    Toast.makeText(StudentProfileActivity.this, "Profile Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean validatePhoneNumber() {
        String countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();
        String phoneNumber = editTextPhoneNumber.getText().toString();

        if (countryCode.equals("+27")) {
            if (phoneNumber.length() != 10) {
                Toast.makeText(StudentProfileActivity.this,"Mobile number should contain 10 digits", Toast.LENGTH_LONG).show();
                return false;
            }
        } else{
            Toast.makeText(StudentProfileActivity.this, "South African numbers should have +27", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}