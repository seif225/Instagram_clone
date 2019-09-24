package com.example.instagramclone.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.Fragments.PostDetailFragment;
import com.example.instagramclone.Fragments.ProfileFragment;
import com.example.instagramclone.Model.Notification;
import com.example.instagramclone.Model.Post;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {


    private Context context;
    private List<Notification> listOfNotifications;


    public NotificationAdapter(Context context, List<Notification> listOfNotifications) {
        this.context = context;
        this.listOfNotifications = listOfNotifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Notification notification = listOfNotifications.get(position);

        holder.text.setText(notification.getText());
        getUserInfo(holder.image_profile,holder.username,notification.getUserid());
        if(notification.isIspost()){

            holder.post_image.setVisibility(View.VISIBLE);
            getPostImage(holder.post_image,notification.getPostid());

        }
        else {

            holder.post_image.setVisibility(View.GONE);

        }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (notification.isIspost()) {

                        SharedPreferences.Editor editor =context.
                                getSharedPreferences("PREFS",context.MODE_PRIVATE).edit();
                        editor.putString("postid" , notification.getPostid());
                        editor.apply();


                        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container,new PostDetailFragment()).commit();

                    }
                    else {

                        SharedPreferences.Editor editor =context.
                                getSharedPreferences("PREFS",context.MODE_PRIVATE).edit();
                        editor.putString("profileid" , notification.getUserid());
                        editor.apply();


                        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new ProfileFragment()).commit();





                    }


                }
            });


    }

    @Override
    public int getItemCount() {
        if(listOfNotifications==null) return 0;
        return listOfNotifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image_profile, post_image;
        TextView username, text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            post_image = itemView.findViewById(R.id.post_image);
            username = itemView.findViewById(R.id.username);
            text = itemView.findViewById(R.id.comment);
        }
    }

    private void getUserInfo(final ImageView imageView , final TextView username , String publisherId){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                .child(publisherId);

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
        }) ;



    }

    private void getPostImage(final ImageView imageView, final String postid){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Posts").child(postid);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Post post = dataSnapshot.getValue(Post.class);
                    Glide.with(context).load(post.getPostimage()).into(imageView);



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



    }


}
