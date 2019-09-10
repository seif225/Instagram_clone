package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText userName, fullName, email, password;
    private Button register;
    private TextView txt_login;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        intializeFields();
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLoginActivity();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setTitle("please wait..");
                progressDialog.setMessage("registering your account ..");
                progressDialog.setCancelable(false);
                progressDialog.show();

                String str_userName = userName.getText().toString();
                String str_fullName = fullName.getText().toString();
                String str_mail = email.getText().toString();
                String str_password = password.getText().toString();

                if (TextUtils.isEmpty(str_userName)) {
                    userName.setError("this username is invalid ");
                    progressDialog.dismiss();

                } else if (TextUtils.isEmpty(str_fullName)) {
                    fullName.setError("this name is invalid ");
                    progressDialog.dismiss();


                } else if (TextUtils.isEmpty(str_mail)) {
                    email.setError("this email is invalid ");
                    progressDialog.dismiss();


                } else if (TextUtils.isEmpty(str_password) || str_password.length() < 6) {
                    password.setError("this password is invalid ");
                    progressDialog.dismiss();


                } else {
                    registerUser(str_userName, str_fullName, str_mail, str_password);
                }

            }
        });


    }

    private void registerUser(final String str_userName, final String str_fullName, final String str_mail, String str_password) {


        mAuth.createUserWithEmailAndPassword(str_mail, str_password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String userId = firebaseUser.getUid();
                    userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id", userId);
                    hashMap.put("username", str_userName);
                    hashMap.put("fullname", str_fullName);
                    hashMap.put("email" , str_mail);
                    hashMap.put("bio", "");
                    hashMap.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/instagram-clone-ea473.appspot.com/o/user.jpg?alt=media&token=85502e79-8eba-462f-8cfb-6685b583eae8");

                    userRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent i = new Intent(RegisterActivity.this, HomeMain.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "something went wrong , try again", Toast.LENGTH_LONG).show();

                            }

                        }
                    });

                }


            }
        });


    }

    private void sendUserToLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));

    }

    private void intializeFields() {
        userName = findViewById(R.id.username);
        fullName = findViewById(R.id.full_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        txt_login = findViewById(R.id.text_login);
        progressDialog=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

    }
}
