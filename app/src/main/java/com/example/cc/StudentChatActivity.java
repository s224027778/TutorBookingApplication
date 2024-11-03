package com.example.cc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cc.databinding.FragmentChatBinding;
import com.example.cc.databinding.FragmentStudentChatBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class StudentChatActivity extends AppCompatActivity {

    private DatabaseHelper dbHelp;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder> chatAdapter;
    private RecyclerView mrecyclerview;
    private LinearLayoutManager linearLayoutManager;
    private FragmentStudentChatBinding binding;
    String studentName;;

    private TextView loggedInUsername;
    private TextView loggedInStatus;
    private ImageView loggedInImage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentStudentChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mrecyclerview = findViewById(R.id.recyclerview);

        String currentUserUid = firebaseAuth.getUid();

        SharedPreferences studentPrefs = getSharedPreferences("StudentPrefs", MODE_PRIVATE);
        String loggedInUsername = studentPrefs.getString("LoggedInStudentUsername", null);

        dbHelp = new DatabaseHelper(this);

        studentName = dbHelp.getLoggedInStudentName(loggedInUsername);

        // Query to exclude the current user from the chat list
        Query query = firebaseFirestore.collection("Users")
                .whereNotEqualTo("uid", currentUserUid);

        FirestoreRecyclerOptions<firebasemodel> allUsernames = new FirestoreRecyclerOptions.Builder<firebasemodel>()
                .setQuery(query, firebasemodel.class)
                .build();

        // Firestore adapter setup
        chatAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allUsernames) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int position, @NonNull firebasemodel userModel) {
                noteViewHolder.particularUsername.setText(userModel.getName());

                // Load user profile image or set default
                String uri = userModel.getImage();
                if (uri != null && !uri.isEmpty()) {
                    Picasso.get().load(uri).into(noteViewHolder.imageViewOfUser);
                } else {
                    noteViewHolder.imageViewOfUser.setImageResource(R.drawable.defaultprofile);
                }

                String status = userModel.getStatus();
                noteViewHolder.statusOfUser.setText(status);
                noteViewHolder.statusOfUser.setTextColor("Online".equals(status) ? Color.BLUE : Color.BLACK);


                noteViewHolder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(StudentChatActivity.this, studentspecificchat.class);
                    intent.putExtra("name", userModel.getName());
                    intent.putExtra("receiverUid", userModel.getUid());
                    intent.putExtra("imageUri", userModel.getImage());
                    startActivity(intent);
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.studentchatviewlayout, parent, false);
                return new NoteViewHolder(view);
            }
        };

        mrecyclerview.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(linearLayoutManager);
        mrecyclerview.setAdapter(chatAdapter);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                startActivity(new Intent(StudentChatActivity.this, StudentHomeActivity.class));
                finish();
            } else if (itemId == R.id.bookingRequests) {;
                Intent intent = new Intent(StudentChatActivity.this, StudentSessions.class);
                intent.putExtra("STUDENT_NAME", studentName);
                startActivity(intent);
            } else if (itemId == R.id.chat) {
                startActivity(new Intent(StudentChatActivity.this, StudentChatActivity.class));
            } else if (itemId == R.id.settings) {
                startActivity(new Intent(StudentChatActivity.this, StudentSettings.class));
                finish();
            }
            return true;
        });
        Cursor cursor = null;
        try {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

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
        private final TextView particularUsername;
        private final TextView statusOfUser;
        private final ImageView imageViewOfUser;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            particularUsername = itemView.findViewById(R.id.nameofuser);
            statusOfUser = itemView.findViewById(R.id.statusofuser);
            imageViewOfUser = itemView.findViewById(R.id.imageviewofuser);
        }
    }

}


