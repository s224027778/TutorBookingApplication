package com.example.cc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;
import android.widget.Toast;
import android.util.Log;

public class ChatFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder> chatAdapter;
    private RecyclerView mrecyclerview;
    private LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mrecyclerview = v.findViewById(R.id.recyclerview);

        Query query = firebaseFirestore.collection("Users")
                .whereNotEqualTo("uid", firebaseAuth.getUid());

        // Error handling for Firestore query
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {

        }).addOnFailureListener(e -> {
            Log.e("FirestoreError", "Error retrieving data: " + e.getMessage());
            Log.e("FirestorePermissionError", "Permission error: " + e.getMessage());
        });

        FirestoreRecyclerOptions<firebasemodel> allusername = new FirestoreRecyclerOptions.Builder<firebasemodel>()
                .setQuery(query, firebasemodel.class)
                .build();

        chatAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allusername) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull firebasemodel firebasemodel) {
                noteViewHolder.particularusername.setText(firebasemodel.getName());
                String uri = firebasemodel.getImage();

                Picasso.get().load(uri).into(noteViewHolder.mimageviewofuser);
                if ("Online".equals(firebasemodel.getStatus())) {
                    noteViewHolder.statusofuser.setText(firebasemodel.getStatus());
                    noteViewHolder.statusofuser.setTextColor(Color.BLUE);
                } else {
                    noteViewHolder.statusofuser.setText(firebasemodel.getStatus());
                }

                noteViewHolder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(requireActivity(), specificchat.class);
                    intent.putExtra("name", firebasemodel.getName());
                    intent.putExtra("receiveruid", firebasemodel.getUid());
                    intent.putExtra("imageuri", firebasemodel.getImage());
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
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mrecyclerview.setLayoutManager(linearLayoutManager);
        mrecyclerview.setAdapter(chatAdapter);

        return v;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
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
