package com.example.instagramclone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.HomeMain;
import com.example.instagramclone.Model.Comment;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context context;
    private List<Comment> commentList;
    private FirebaseUser firebaseUser ;
    public CommentAdapter(Context context , List<Comment> commentList ){

            this.context = context;
            this.commentList = commentList;

    }



    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item
        ,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            final Comment comment = commentList.get(position);

            holder.comment.setText(comment.getComment());
            getUserInfo(holder.image_profile,holder.username,comment.getPublisher());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent (context, HomeMain.class);
                    intent.putExtra("publsiherid",comment.getPublisher());
                    context.startActivity(intent);

                }
            });


    }

    @Override
    public int getItemCount() {
        if (commentList==null) return 0;
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         ImageView image_profile;
         TextView username,comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        image_profile = itemView.findViewById(R.id.user_image);
        username=itemView.findViewById(R.id.username);
        comment = itemView.findViewById(R.id.comment_text_view);


        }
    }

    private void getUserInfo(final ImageView imageView , final TextView username , String publishersId)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(publishersId);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                Glide.with(context).load(user.getImageUrl()).into(imageView);
                username.setText(user.getUserName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
