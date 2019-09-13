package com.example.instagramclone.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramclone.Adapter.PostAdapter;
import com.example.instagramclone.Model.Post;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;

private List<String> followingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_home, container, false);

       recyclerView = view.findViewById(R.id.home_recycler_view);
       recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getActivity(),postList);
        recyclerView.setAdapter(postAdapter);
        checkFollowing();


        return view;
    }



        private void checkFollowing(){
        followingList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Follow")
                .
                child(FirebaseAuth.getInstance().getUid())
                .child("following");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            followingList.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                followingList.add(snapshot.getKey());


            }

            readPosts();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }



    private void readPosts () {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    Post post = dataSnapshot1.getValue(Post.class);
                    for (String id : followingList){

                        if (post.getPublisher().equals(id)){

                            postList.add(post);
                            Log.e("HomeFragment" , post.getPublisher() + "" + post.getPostimage());

                        }



                    }

                }
               postAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
