package com.example.instagramclone.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.Adapter.MyPhotoAdapter;
import com.example.instagramclone.Model.Post;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileFragment extends Fragment {


    private ImageView image_profile, options;
    private TextView posts, followers, followings, fullname, bio, username;
    private Button edit_profile;
    private FirebaseUser firebaseUser;
    private String profileId;
    private ImageButton my_photos, saved_photos;
    private View view;
    private RecyclerView recyclerView;
    private MyPhotoAdapter adapter;
    private List<Post> listOfPosts;
    private List<String> mySaves;
    private RecyclerView recyclerView_saved;
    private MyPhotoAdapter adapter_saves;
    private List<Post> listOfSaves;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        initializeFields();


        return view;
    }

    private void initializeFields() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences pref = getContext()
                .getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        profileId = pref.getString("profileid", "none");
        Log.e("profile id", profileId + " ...");
        image_profile = view.findViewById(R.id.image_profile);
        options = view.findViewById(R.id.options);
        posts = view.findViewById(R.id.posts);
        username = view.findViewById(R.id.username);
        followers = view.findViewById(R.id.followers);
        followings = view.findViewById(R.id.following);
        fullname = view.findViewById(R.id.fullname);
        bio = view.findViewById(R.id.bio);
        edit_profile = view.findViewById(R.id.edit_profile);
        my_photos = view.findViewById(R.id.my_fotos);
        saved_photos = view.findViewById(R.id.saved_fotos);


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(
                getContext(), 3);
        recyclerView.setLayoutManager(linearLayoutManager);
        listOfPosts = new ArrayList();
        adapter = new MyPhotoAdapter(getContext(), listOfPosts);
        recyclerView.setAdapter(adapter);


        recyclerView_saved = view.findViewById(R.id.recycler_view_save);
        recyclerView_saved.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_saves = new GridLayoutManager(
                getContext(), 3);
        recyclerView_saved.setLayoutManager(linearLayoutManager_saves);
        listOfSaves = new ArrayList();
        adapter_saves = new MyPhotoAdapter(getContext(), listOfSaves);
        recyclerView_saved.setAdapter(adapter_saves);

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView_saved.setVisibility(View.GONE);


        userInfo();
        getFollowers();
        getNrPosts();
        getPhotos();
        mySaves();

        if (profileId.equals(firebaseUser.getUid())) edit_profile.setText("Edit Profile");
        else {
            checkFollow();
            saved_photos.setVisibility(View.GONE);
        }

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = edit_profile.getText().toString();
                if (btn.equals("Edit Profile")) {
                    //TODO: go to the edit profile activity , it's not created yet.


                } else if (btn.equals("follow")) {

                    FirebaseDatabase.getInstance()
                            .getReference().child("Follow").
                            child(firebaseUser.getUid()).child("following")
                            .child(profileId).setValue(true);


                    FirebaseDatabase.getInstance().getReference()
                            .child("Follow").child(profileId)
                            .child("followers").child(firebaseUser.getUid())
                            .setValue(true);


                } else if (btn.equals("following")) {


                    FirebaseDatabase.getInstance()
                            .getReference().child("Follow").
                            child(firebaseUser.getUid())
                            .child("following")
                            .child(profileId).removeValue();

                    FirebaseDatabase.getInstance().getReference()
                            .child("Follow").child(profileId)
                            .child("followers").child(firebaseUser.getUid())
                            .removeValue();
                }
            }
        });

        my_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerView.setVisibility(View.VISIBLE);
                recyclerView_saved.setVisibility(View.GONE);

            }
        });

        saved_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                recyclerView_saved.setVisibility(View.VISIBLE);


            }
        });


    }

    private void userInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Users").child(profileId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (getContext() == null) return;

                User user = dataSnapshot.getValue(User.class);
                if (user.getImageUrl() != null && image_profile != null)
                    Glide.with(getContext()).load(user.getImageUrl()).into(image_profile);
                username.setText(user.getUserName());
                fullname.setText(user.getFullName());
                bio.setText(user.getBio());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void checkFollow() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid())
                .child("following");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(profileId).exists()) {
                    edit_profile.setText("following");
                } else {
                    edit_profile.setText("follow");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void getFollowers() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileId).child("followers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                followers.setText("" + dataSnapshot.getChildrenCount());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileId).child("following");

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                followings.setText("" + dataSnapshot.getChildrenCount());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getNrPosts() {


        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Posts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileId)) {

                        i++;

                    }


                }

                posts.setText("" + i);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getPhotos() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listOfPosts.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileId)) listOfPosts.add(post);

                }

                Collections.reverse(listOfPosts);
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void mySaves() {

        mySaves = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("saves").child(firebaseUser.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    mySaves.add(snapshot.getKey());

                }

                readSaves();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void readSaves() {

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listOfSaves.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Post post = snapshot.getValue(Post.class);
                    for (String id : mySaves) {

                        if (post.getPostid().equals(id)) {

                            listOfSaves.add(post);


                        }


                    }


                }
                if(listOfSaves!=null && listOfSaves.size()>=0)
                Log.e("readSaves",listOfSaves.size() +" ...");
                adapter_saves.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
