package com.example.cc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
    private FragmentChatBinding binding;

    private TextView loggedInUsername;
    private TextView loggedInStatus;
    private ImageView loggedInImage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mrecyclerview = findViewById(R.id.recyclerview);


        String currentUserUid = firebaseAuth.getUid();

        Query query = firebaseFirestore.collection("Users")
                .whereNotEqualTo("uid", currentUserUid);

        FirestoreRecyclerOptions<firebasemodel> allUsernames = new FirestoreRecyclerOptions.Builder<firebasemodel>()
                .setQuery(query, firebasemodel.class)
                .build();

        chatAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allUsernames) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int position, @NonNull firebasemodel userModel) {
                noteViewHolder.particularUsername.setText(userModel.getName());

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
                    Intent intent = new Intent(ChatActivity.this, specificchat.class);
                    intent.putExtra("name", userModel.getName());
                    intent.putExtra("receiverUid", userModel.getUid());
                    intent.putExtra("imageUri", userModel.getImage());
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

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                startActivity(new Intent(ChatActivity.this, TutorBookingRequests.class));
            } else if (itemId == R.id.bookingRequests) {
                startActivity(new Intent(ChatActivity.this, ConfirmedBookingActivity.class));
            } else if (itemId == R.id.chat) {
                startActivity(new Intent(ChatActivity.this, ChatActivity.class));
            } else if (itemId == R.id.settings) {
                startActivity(new Intent(ChatActivity.this, TutorSettings.class));
            }
            return true;
        });
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


