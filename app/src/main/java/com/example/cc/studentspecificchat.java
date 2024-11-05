package com.example.cc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class studentspecificchat extends AppCompatActivity {

    private EditText mGetMessage;
    private ImageButton mSendMessageButton;
    private RecyclerView mMessageRecyclerView;
    private MessagesAdapter messagesAdapter;
    private ArrayList<Messages> messagesArrayList;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference senderRoomRef, receiverRoomRef;

    private String receiverUid, senderUid, receiverName, senderRoom, receiverRoom;
    private SimpleDateFormat simpleDateFormat;

    private ImageView mImageViewOfSpecificUser;
    private TextView mNameOfSpecificUser;
    private ValueEventListener messagesListener;

    ImageButton mbackbuttonofspecificchat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentspecificchat);

        initializeUI();
        setupFirebase();
        setupRecyclerView();
        loadMessages();

        mSendMessageButton.setOnClickListener(view -> sendMessage());

        mbackbuttonofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(studentspecificchat.this, StudentChatActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeUI() {
        mGetMessage = findViewById(R.id.getmessage);
        mSendMessageButton = findViewById(R.id.imageviewsendmessage);
        mMessageRecyclerView = findViewById(R.id.recyclerviewofspecific);
        mNameOfSpecificUser = findViewById(R.id.mnameofspecificuser);
        mImageViewOfSpecificUser = findViewById(R.id.mimageviewofspecificuser);
        mbackbuttonofspecificchat=findViewById(R.id.backbuttonofspecificchat);


        Intent intent = getIntent();
        receiverUid = intent.getStringExtra("receiverUid");
        receiverName = intent.getStringExtra("name");
        String uri = intent.getStringExtra("imageUri");

        if (receiverName != null) {
            mNameOfSpecificUser.setText(receiverName);
        }

        if (uri != null && !uri.isEmpty()) {
            Picasso.get().load(uri).into(mImageViewOfSpecificUser);
        } else {
            Toast.makeText(this, "No profile image found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        senderUid = firebaseAuth.getUid();

        if (senderUid != null && receiverUid != null) {
            senderRoom = senderUid + receiverUid;
            receiverRoom = receiverUid + senderUid;
            senderRoomRef = firebaseDatabase.getReference("chats").child(senderRoom).child("messages");
            receiverRoomRef = firebaseDatabase.getReference("chats").child(receiverRoom).child("messages");
        } else {
            Toast.makeText(this, "Error: Unable to set up chat rooms", Toast.LENGTH_SHORT).show();
            finish();
        }

        simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        messagesArrayList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(linearLayoutManager);
        messagesAdapter = new MessagesAdapter(this, messagesArrayList);
        mMessageRecyclerView.setAdapter(messagesAdapter);
    }

    private void loadMessages() {
        if (senderRoomRef == null) {
            Log.e("studentspecificchat", "senderRoomRef is null. Check Firebase initialization.");
            return;
        }

        messagesListener = senderRoomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Messages message = dataSnapshot.getValue(Messages.class);
                    if (message != null) {
                        messagesArrayList.add(message);
                    } else {
                        Log.e("studentspecificchat", "Message is null for snapshot: " + dataSnapshot);
                    }
                }
                messagesAdapter.notifyDataSetChanged();
                mMessageRecyclerView.smoothScrollToPosition(messagesArrayList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(studentspecificchat.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
                Log.e("specificchat", "Error loading messages: " + error.getMessage());
            }
        });
    }

    private void sendMessage() {
        String enteredMessage = mGetMessage.getText().toString().trim();
        if (enteredMessage.isEmpty()) {
            Toast.makeText(this, "Enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        long timestamp = System.currentTimeMillis();
        String currentTime = simpleDateFormat.format(new Date());
        Messages message = new Messages(enteredMessage, senderUid, timestamp, currentTime);

        senderRoomRef.push().setValue(message).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                receiverRoomRef.push().setValue(message);
                mGetMessage.setText("");
            } else {
                Toast.makeText(studentspecificchat.this, "Message not sent. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (messagesListener != null) {
            senderRoomRef.removeEventListener(messagesListener);
        }
    }
}
