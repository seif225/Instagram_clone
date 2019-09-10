package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button login , register;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeFields();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLogin();}
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToRegister();
            }
        });

    }

    private void sendUserToRegister() {
        Intent i = new Intent (this , RegisterActivity.class);
        startActivity(i);
    }

    private void sendUserToLogin() {
        Intent i = new Intent (this , LoginActivity.class);
        startActivity(i);
    }

    private void initializeFields() {


        login = findViewById(R.id.login);
        register=findViewById(R.id.register);
    }




    @Override
    protected void onStart() {
        super.onStart();

        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser !=null) {
            startActivity(new Intent( MainActivity.this , HomeMain.class));
            finish();
        }

    }
}
