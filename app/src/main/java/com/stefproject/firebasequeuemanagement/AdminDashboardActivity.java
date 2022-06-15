package com.stefproject.firebasequeuemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button btnManager,btnUser,btnCompany;
    private Button btnLogout;
    private FirebaseAuth auth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        auth=FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(QueueApp.sharedName,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        btnUser = (Button)findViewById(R.id.btnUser);
        btnCompany = (Button)findViewById(R.id.btnCompany);
        btnManager =(Button)findViewById(R.id.btnManager);
        btnLogout = (Button)findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                editor.putString(QueueApp.sharedUserType,"");
                editor.commit();

                Intent intent = new Intent(AdminDashboardActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this,UserActivity.class);
                startActivity(intent);
            }
        });

        btnCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this,CompanyActivity.class);
                startActivity(intent);
            }
        });

        btnManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this,ManagerActivity.class);
                startActivity(intent);
            }
        });
    }
}
