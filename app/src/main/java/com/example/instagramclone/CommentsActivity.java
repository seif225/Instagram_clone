package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.instagramclone.Adapter.CommentAdapter;
import com.example.instagramclone.Model.Comment;
import com.example.instagramclone.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {


    private EditText addComment;
    private ImageView image_profile;
    private TextView post;
    private String postId;
    private String publisherid;
    private FirebaseUser firebaseUser;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        addComment = findViewById(R.id.add_comment);
        image_profile = findViewById(R.id.user_image);
        post = findViewById(R.id.post);
        recyclerView = findViewById(R.id.comment_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentlist = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentlist);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView.setAdapter(commentAdapter);


        postId = getIntent().getStringExtra("postid");
        publisherid = getIntent().getStringExtra("publisherid");

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addComment.getText().equals("")) {
                    Toast.makeText(CommentsActivity.this, "you can't send empty comment", Toast.LENGTH_SHORT).show();

                } else addCommet();
            }
        });


        getImage();
        readComments();

    }

    private void addCommet() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Comments")
                .child(postId);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment", addComment.getText().toString());
        hashMap.put("publisher", firebaseUser.getUid());

        ref.push().setValue(hashMap);
        addComment.setText("");


    }

    private void getImage() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getImageUrl())
                        .into(image_profile);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void readComments() {

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Comments").child(postId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentlist.clear();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    Comment comment = dataSnapshot1.getValue(Comment.class);
                    commentlist.add(comment);




                }

                commentAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
