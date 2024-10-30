package com.example.cc;


import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class ChatActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder> chatAdapter;
    private RecyclerView mrecyclerview;
    private LinearLayoutManager linearLayoutManager;

    // Add TextViews for the logged-in user
    private TextView loggedInUsername;
    private TextView loggedInStatus;
    private ImageView loggedInImage;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat); // Use setContentView instead of onCreateView

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mrecyclerview = findViewById(R.id.recyclerview);

        // Initialize TextViews and ImageView for the logged-in user
        loggedInUsername = findViewById(R.id.logged_in_username);
        loggedInStatus = findViewById(R.id.logged_in_status);
        loggedInImage = findViewById(R.id.logged_in_image);

        // Retrieve logged-in user's information
        String currentUserUid = firebaseAuth.getUid();
        firebaseFirestore.collection("Users").document(currentUserUid)
                .get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        firebasemodel currentUser = documentSnapshot.toObject(firebasemodel.class);
                        if (currentUser != null) {
                            loggedInUsername.setText(currentUser.getName());
                            loggedInStatus.setText(currentUser.getStatus());
                            String uri = currentUser.getImage();
                            if (uri != null && !uri.isEmpty()) {
                                Picasso.get()
                                        .load(uri)
                                        .into(loggedInImage);
                            } else {
                                loggedInImage.setImageResource(R.drawable.defaultprofile);
                            }
                        }
                    }
                }).addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Error retrieving logged-in user data: " + e.getMessage());
                });

        // Query to get other users
        Query query = firebaseFirestore.collection("Users")
                .whereNotEqualTo("uid", currentUserUid);

        // Set up FirestoreRecyclerOptions
        FirestoreRecyclerOptions<firebasemodel> allusername = new FirestoreRecyclerOptions.Builder<firebasemodel>()
                .setQuery(query, firebasemodel.class)
                .build();

        chatAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allusername) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int position, @NonNull firebasemodel userModel) {
                noteViewHolder.particularusername.setText(userModel.getName());

                String uri = userModel.getImage();
                if (uri != null && !uri.isEmpty()) {
                    Picasso.get()
                            .load(uri)
                            .into(noteViewHolder.mimageviewofuser);
                } else {
                    noteViewHolder.mimageviewofuser.setImageResource(R.drawable.defaultprofile);
                }

                String status = userModel.getStatus();
                noteViewHolder.statusofuser.setText(status);
                noteViewHolder.statusofuser.setTextColor("Online".equals(status) ? Color.GREEN : Color.BLACK);

                noteViewHolder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(ChatActivity.this, specificchat.class);
                    intent.putExtra("name", userModel.getName());
                    intent.putExtra("receiveruid", userModel.getUid());
                    intent.putExtra("imageuri", userModel.getImage());
                    startActivity(intent);
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatviewlayout, parent, false);
                return new NoteViewHolder(view);
            }
        };

        mrecyclerview.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(linearLayoutManager);
        mrecyclerview.setAdapter(chatAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatAdapter.stopListening();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView particularusername;
        private TextView statusofuser;
        private ImageView mimageviewofuser;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            particularusername = itemView.findViewById(R.id.nameofuser);
            statusofuser = itemView.findViewById(R.id.statusofuser);
            mimageviewofuser = itemView.findViewById(R.id.imageviewofuser);
        }
    }
}

