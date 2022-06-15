package com.stefproject.firebasequeuemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences  =getSharedPreferences(QueueApp.sharedName,MODE_PRIVATE);
        if(sharedPreferences.getString(QueueApp.sharedUserType,"").equals(""))
        {
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            if(sharedPreferences.getString(QueueApp.sharedUserType,"").equals("manager"))
            {
                Intent intent = new Intent(MainActivity.this, ManagerDashboardActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                if(sharedPreferences.getString(QueueApp.sharedUserType,"").equals("user"))
                {
                    Intent intent = new Intent(MainActivity.this, UserDashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    if(sharedPreferences.getString(QueueApp.sharedUserType,"").equals("company"))
                    {
                        Intent intent = new Intent(MainActivity.this, CompanyDashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }


      /*  FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(authStateListener);*/
    }

   /* FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            if (firebaseUser == null) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
            if (firebaseUser != null) {

              *//*  Intent intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
                startActivity(intent);
                finish();*//*
            }
        }
    };*/
}