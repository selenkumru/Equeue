package com.stefproject.firebasequeuemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class CompanyDashboardActivity extends AppCompatActivity {

    private Button btnManager;
    private Button btnQueue;
    private Button btnLogout;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_dashboard);

        sharedPreferences = getSharedPreferences(QueueApp.sharedName,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        firebaseAuth = FirebaseAuth.getInstance();
        btnQueue = (Button)findViewById(R.id.btnQueue);
        btnManager = (Button)findViewById(R.id.btnManager);
        btnLogout = (Button)findViewById(R.id.btnLogout);

        btnQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyDashboardActivity.this,QueueActivity.class);
                startActivity(intent);
            }
        });

        btnManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyDashboardActivity.this,ManagerForCompanyActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                editor.putString(QueueApp.sharedUserType,"");
                editor.commit();

                Intent intent  =new Intent(CompanyDashboardActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
