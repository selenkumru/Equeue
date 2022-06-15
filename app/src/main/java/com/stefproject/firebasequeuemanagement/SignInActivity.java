package com.stefproject.firebasequeuemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignInActivity extends AppCompatActivity {

    private MaterialEditText SignInMail, SignInPass;
    private FirebaseAuth auth;
    private TextView btnSignin;
    private LinearLayout linSignup;
    private DatabaseReference databaseReference;
    String usertype="";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        sharedPreferences = getSharedPreferences(QueueApp.sharedName,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        // set the view now
        setContentView(R.layout.activity_signin);
        SignInMail = (MaterialEditText) findViewById(R.id.edUsername);
        SignInPass = (MaterialEditText) findViewById(R.id.edPassword);
        btnSignin = (TextView) findViewById(R.id.btnSignin);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        linSignup = (LinearLayout)findViewById(R.id.linSignup);

        linSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateSignUp();
            }
        });
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = SignInMail.getText().toString();
                final String password = SignInPass.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter your mail address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 8) {
                                        Toast.makeText(getApplicationContext(),"Password must be more than 8 digit",Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    if(SignInMail.getText().toString().trim().equals("bk@bk.com"))
                                    {
                                        editor.putString(QueueApp.sharedUserType,"admin");
                                        editor.commit();
                                        Intent intent = new Intent(SignInActivity.this, AdminDashboardActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        databaseReference = FirebaseDatabase.getInstance().getReference().child("TypeData");
                                        databaseReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot ds : snapshot.getChildren()) {
                                                    String email = ds.child("email").getValue(String.class);
                                                    String type = ds.child("usertype").getValue(String.class);
                                                    if(email.equals(SignInMail.getText().toString().trim()))
                                                    {
                                                        usertype= type;
                                                    }
                                                    Toast.makeText(SignInActivity.this, ""+email+"="+type, Toast.LENGTH_SHORT).show();
                                                }

                                                if(usertype.equals("manager"))
                                                {
                                                    editor.putString(QueueApp.sharedUserType,"manager");
                                                    editor.commit();
                                                    Intent intent =new Intent(SignInActivity.this,ManagerDashboardActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                if(usertype.equals("company"))
                                                {
                                                    editor.putString(QueueApp.sharedUserType,"company");
                                                    editor.commit();
                                                   DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Company");
                                                   databaseReference.addValueEventListener(new ValueEventListener() {
                                                       @Override
                                                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                           for (DataSnapshot ds : snapshot.getChildren()) {
                                                               String email = ds.child("email").getValue(String.class);
                                                               if(email.equals(SignInMail.getText().toString().trim()))
                                                               {
                                                                   String name = ds.child("name").getValue(String.class);
                                                                   editor.putString(QueueApp.companyName,name);
                                                                   editor.commit();
                                                               }
                                                           }
                                                           Intent intent =new Intent(SignInActivity.this,CompanyDashboardActivity.class);
                                                           startActivity(intent);
                                                           finish();
                                                       }

                                                       @Override
                                                       public void onCancelled(@NonNull DatabaseError error) {

                                                       }
                                                   });
                                                }

                                                if(usertype.equals("user"))
                                                {
                                                    editor.putString(QueueApp.sharedUserType,"user");
                                                    editor.commit();
                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                                String email = ds.child("email").getValue(String.class);
                                                                if(email.equals(SignInMail.getText().toString().trim()))
                                                                {
                                                                    String name = ds.child("name").getValue(String.class);
                                                                    String surname = ds.child("surname").getValue(String.class);
                                                                    editor.putString(QueueApp.userName,name+" "+surname);
                                                                    editor.commit();
                                                                }
                                                            }
                                                            Intent intent =new Intent(SignInActivity.this,UserDashboardActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    }

                                }
                            }
                        });
            }
        });
    }

    public void NavigateSignUp() {
        Intent inent = new Intent(this, SignupActivity.class);
        startActivity(inent);
    }
}
