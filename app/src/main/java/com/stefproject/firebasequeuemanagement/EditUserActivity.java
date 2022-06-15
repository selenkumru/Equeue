package com.stefproject.firebasequeuemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class EditUserActivity extends AppCompatActivity {

    private MaterialEditText edUsername;
    private FirebaseAuth auth;
    private TextView btnSignUp;
    private MaterialEditText edFirstName;
    private MaterialEditText edLastName;
    private MaterialEditText edPhone;
    private DatabaseReference databaseReference;
    Intent intent;
    String userId="";
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        intent=  getIntent();
        userId = intent.getExtras().getString("userId");
        edUsername = findViewById(R.id.edUsername);
        auth= FirebaseAuth.getInstance();
        btnSignUp = (TextView) findViewById(R.id.btnSignUp);
        //     txtLogin = (TextView)findViewById(R.id.txtLogin);
        edFirstName = (MaterialEditText)findViewById(R.id.edFirstName);
        edLastName = (MaterialEditText)findViewById(R.id.edLastName);
        edPhone = (MaterialEditText)findViewById(R.id.edPhone);

        rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("Users");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    String uid = ds.child("uid").getValue(String.class);
                    String surname = ds.child("surname").getValue(String.class);
                    String phoneno = ds.child("phoneno").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);

                    Log.d("TAG", name);
                    if(uid.equals(userId))
                    {
                        edUsername.setText(email);
                        edPhone.setText(phoneno);
                        edFirstName.setText(name);
                        edLastName.setText(surname);
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersdRef.addValueEventListener(eventListener);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference updateData = FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(userId);
                updateData.child("name").setValue(edFirstName.getText().toString().trim());
                updateData.child("surname").setValue(edLastName.getText().toString().trim());
                updateData.child("phoneno").setValue(edPhone.getText().toString().trim());
                Toast.makeText(EditUserActivity.this, "Data has been updated", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }
}
