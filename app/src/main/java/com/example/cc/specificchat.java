package com.example.cc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
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

public class specificchat extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specificchat);

        mGetMessage = findViewById(R.id.getmessage);
        mSendMessageButton = findViewById(R.id.imageviewsendmessage);
        mMessageRecyclerView = findViewById(R.id.recyclerviewofspecific);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        senderUid = firebaseAuth.getUid();
        receiverUid = getIntent().getStringExtra("receiverUid");
        receiverName = getIntent().getStringExtra("name");

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        senderRoomRef = firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages");
        receiverRoomRef = firebaseDatabase.getReference().child("chats").child(receiverRoom).child("messages");

        messagesArrayList = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(linearLayoutManager);
        messagesAdapter = new MessagesAdapter(this, messagesArrayList);
        mMessageRecyclerView.setAdapter(messagesAdapter);

        loadMessages();

        mSendMessageButton.setOnClickListener(view -> sendMessage());
    }

    private void loadMessages() {
        senderRoomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Messages message = dataSnapshot.getValue(Messages.class);
                    if (message != null) {
                        messagesArrayList.add(message);
                    }
                }
                messagesAdapter.notifyDataSetChanged();
                mMessageRecyclerView.smoothScrollToPosition(messagesArrayList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(specificchat.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(specificchat.this, "Message not sent. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (messagesAdapter != null) {
            messagesAdapter.notifyDataSetChanged();
        }
    }
}

