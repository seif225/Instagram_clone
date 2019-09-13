package com.example.instagramclone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.CommentsActivity;
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

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {


    public Context context;
    public List<Post> mPost;

    private FirebaseUser firebaseUser;

    public PostAdapter(Context context, List<Post> mPost) {
        this.context = context;
        this.mPost = mPost;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Post post = mPost.get(position);
        Log.e("PostAdapter", post.getPublisher());

        if (post != null) {
            if (post.getPostimage() != null) {
                Log.e("Context check adapter", context + "");
                Glide.with(context).load(post.getPostimage()).into(holder.post_iamge);
            }
            if (post.getDescription().equals("")) holder.description.setVisibility(View.GONE);


            else {
                holder.description.setVisibility(View.VISIBLE);
                holder.description.setText(post.getDescription());

            }

            publisherInfo(holder.image_profile, holder.username, holder.publisher, post.getPublisher());
            isLikes(post.getPostid(),holder.like);
            nrLikes(holder.likes,post.getPostid());
            getComments(post.getPostid(),holder.comments);

            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(holder.like.getTag().equals("like")){
                        FirebaseDatabase.getInstance().getReference()
                                .child("Likes").child(post.getPostid())
                                .child(firebaseUser.getUid()).setValue(true);

                    }
                    else {

                        FirebaseDatabase.getInstance().getReference()
                                .child("Likes").child(post.getPostid())
                                .child(firebaseUser.getUid()).removeValue();


                    }

                }
            });
        }


        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CommentsActivity.class);
                i.putExtra("postid",post.getPostid());
                i.putExtra("publisherid" , post.getPublisher());
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        if (mPost == null) return 0;
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_profile, post_iamge, like, comment, save;
        public TextView username, likes, publisher, description, comments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            post_iamge = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            username = itemView.findViewById(R.id.username);
            likes = itemView.findViewById(R.id.likes);
            publisher = itemView.findViewById(R.id.publisher);
            description = itemView.findViewById(R.id.description);
            comments = itemView.findViewById(R.id.comments);


        }
    }

    private void isLikes(String postid, final ImageView imageView) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance()
                .getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_like_ref);
                    imageView.setTag("liked");

                } else {
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("like");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void nrLikes(final TextView likes, String postid) {

        DatabaseReference reference= FirebaseDatabase.getInstance()
                .getReference().child("Likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                likes.setText(dataSnapshot.getChildrenCount()+" likes");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void publisherInfo(final ImageView image_profile, final TextView username, final TextView publisher, String userId) {

        DatabaseReference reference = FirebaseDatabase
                .getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                User user = dataSnapshot.getValue(User.class);
                Glide.with(context).load(user.getImageUrl()).into(image_profile);
                username.setText(user.getUserName());
                publisher.setText(user.getUserName());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void  getComments(String postid, final TextView comments)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference().child("Comments").child(postid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                comments.setText( "View All " + dataSnapshot.getChildrenCount()+" Comments");



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
