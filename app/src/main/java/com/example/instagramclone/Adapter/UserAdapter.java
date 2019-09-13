package com.example.instagramclone.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.Fragments.ProfileFragment;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<User> mUsers;
    private FirebaseUser firebaseUser;

    public UserAdapter(Context context, List<User> mUsers) {

        this.context = context;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final User user = mUsers.get(position);

        holder.btnFollow.setVisibility(View.VISIBLE);
        holder.userName.setText(user.getUserName());
        holder.fullName.setText(user.getFullName());
        Log.e("adapter" , user.getId().toString() + "user name :  " + user.getUserName() );

        Glide.with(context).load(user.getImageUrl()).into(holder.imageProfile);
        isFollowing(user.getId(),holder.btnFollow);
        if (user.getId().equals(firebaseUser.getUid())) {

            holder.btnFollow.setVisibility(View.GONE);


        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS" , Context.MODE_PRIVATE).edit();
                editor.putString("profileid",user.getId());
                editor.apply();

                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container , new ProfileFragment())
                        .commit();

            }
        });


        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.btnFollow.getText().toString().equals("follow")){

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").child(user.getId()).setValue(true);


                    FirebaseDatabase.getInstance().getReference()
                            .child("Follow").child(user.getId())
                            .child("followers").child(firebaseUser.getUid())
                            .setValue(true);


                }

                else {

                    FirebaseDatabase.getInstance()
                            .getReference().child("Follow").
                            child(firebaseUser.getUid())
                            .child("following")
                            .child(user.getId()).removeValue();

                    FirebaseDatabase.getInstance().getReference()
                            .child("Follow").child(user.getId())
                            .child("followers").child(firebaseUser.getUid())
                            .removeValue();




                }



            }
        });


    }

    @Override
    public int getItemCount() {
        if (mUsers==null) return 0;
        return mUsers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, fullName;
        public CircleImageView imageProfile;
        public Button btnFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            fullName = itemView.findViewById(R.id.full_name);
            imageProfile = itemView.findViewById(R.id.image_profile);
            btnFollow = itemView.findViewById(R.id.btn_follow);


        }
    }

    private void isFollowing(final String userid, final Button button) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(userid).exists()) {

                    button.setText("following");


                } else button.setText("follow");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
