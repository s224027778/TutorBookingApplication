package com.example.cc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 123;

    private EditText editTextUsername, editTextPassword, editTextPasswordConfirm;
    private RadioGroup radioGroupUserType;
    private ProgressBar progressBar;
    private CardView mgetuserimage;
    private ImageView mgetuserimageinimageview;

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    private DatabaseHelper db;

    private Uri imagepath;
    private String ImageUriAccessToken;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = firebaseStorage.getReference();
        db = new DatabaseHelper(this);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);
        radioGroupUserType = findViewById(R.id.radioGroupUserType);
        progressBar = findViewById(R.id.progressbarofsetProfile);
        mgetuserimage = findViewById(R.id.getuserimage);
        mgetuserimageinimageview = findViewById(R.id.getuserimageinimageview);
        Button buttonSignup = findViewById(R.id.buttonSignup);
        AppCompatImageView backButton = findViewById(R.id.back);

        // Set up image selection
        mgetuserimage.setOnClickListener(view -> openImagePicker());

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        });

        buttonSignup.setOnClickListener(v -> handleSignup());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void handleSignup() {
        username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextPasswordConfirm.getText().toString().trim();
        int selectedId = radioGroupUserType.getCheckedRadioButtonId();

        if (!validateInput(username, password, confirmPassword, selectedId)) return;

        progressBar.setVisibility(View.VISIBLE);

        if (db.checkUser(username)) {
            Toast.makeText(this, "User already exists", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        storeUserDataLocally(username, password, selectedId);
                        saveUserDataToFirebase();
                    } else {
                        Toast.makeText(SignupActivity.this, "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                });
    }

    private boolean validateInput(String username, String password, String confirmPassword, int selectedId) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || imagepath == null || selectedId == -1) {
            Toast.makeText(this, "All fields are required, including an image", Toast.LENGTH_LONG).show();
            return false;
        }

        if (username.matches("^[a-zA-Z0-9._%+-]+@mandela\\.ac\\.za$")) {
            Log.d("Signup", "Username format is correct.");
        } else {
            Toast.makeText(this, "Username must be in the format @mandela.ac.za.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return false;
        }

        if (password.length() < 8 )
        {
            Toast.makeText(this, "Password must contain more than 8 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!password.matches(".*[A-Z].*"))
        {
            Toast.makeText(this, "Password must include atleast 1 uppercase letter", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!password.matches(".*[a-z].*"))
        {
            Toast.makeText(this, "Password must include atleast 1 lowercase letter", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!password.matches(".*[0-9].*"))
        {
            Toast.makeText(this, "Password be contain atleast one number", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void storeUserDataLocally(String username, String password, int selectedId) {
        RadioButton radioButton = findViewById(selectedId);
        String userType = radioButton.getText().toString();
        boolean isInserted = db.insertData(username, password, userType);
        if (!isInserted) {
            Log.e("SignupActivity", "Failed to store user locally");
        }
    }

    private void saveUserDataToFirebase() {
        String uid = firebaseAuth.getUid();
        if (uid != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(uid);
            userprofile profile = new userprofile(username, uid);
            databaseReference.setValue(profile).addOnSuccessListener(aVoid -> {
                uploadProfileImage(uid);
            }).addOnFailureListener(e -> {
                Log.e("SignupActivity", "Failed to add profile: ");
            });
        }
    }

    private void uploadProfileImage(String uid) {
        StorageReference imageref = storageReference.child("Images").child(uid).child("Profile Pic");
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagepath);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();

            UploadTask uploadTask = imageref.putBytes(data);
            uploadTask.addOnSuccessListener(taskSnapshot -> imageref.getDownloadUrl().addOnSuccessListener(uri -> {
                ImageUriAccessToken = uri.toString();
                saveToFirestore(uid);
            })).addOnFailureListener(e -> Log.e("SignupActivity","Image Upload Failed"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFirestore(String uid) {
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(uid);
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("name", username);
        userdata.put("image", ImageUriAccessToken);
        userdata.put("uid", uid);
        userdata.put("status", "Online");

        documentReference.set(userdata).addOnSuccessListener(aVoid ->
                Log.d("SignupActivity", "User successfully registered")
        ).addOnFailureListener(e ->
                Log.e("SignupActivity", "Error saving to Firestore")
        );

        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imagepath = data.getData();
            mgetuserimageinimageview.setImageURI(imagepath);
        }
    }
}
