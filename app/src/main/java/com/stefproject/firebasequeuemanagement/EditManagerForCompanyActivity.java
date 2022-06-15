package com.stefproject.firebasequeuemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
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

import java.util.ArrayList;

public class EditManagerForCompanyActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView txtLogin;
    private DatabaseReference databaseReference;
    private TextView btnSignUp;
    private MaterialEditText edManagerName,edUsername;
    private FirebaseAuth mAuth2;
    private DatabaseReference rootRef;
    Intent intent;
    String userId = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_manager_for_company);
        intent= getIntent();
        userId = intent.getExtras().getString("userId");

        edUsername = findViewById(R.id.edUsername);
        auth=FirebaseAuth.getInstance();
        btnSignUp = (TextView) findViewById(R.id.btnSignUp);
        //     txtLogin = (TextView)findViewById(R.id.txtLogin);
        edManagerName = (MaterialEditText)findViewById(R.id.edManagerName);

        rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef2 = rootRef.child("Manager");

        ValueEventListener eventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    String uid = ds.child("uid").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    String companyUID = ds.child("companyUid").getValue(String.class);
                    Log.d("TAG", name);
                    if(uid.equals(userId))
                    {
                        edUsername.setText(email);
                        edManagerName.setText(name);
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersdRef2.addValueEventListener(eventListener2);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference updateData = FirebaseDatabase.getInstance()
                        .getReference("Manager")
                        .child(userId);
                updateData.child("name").setValue(edManagerName.getText().toString().trim());
                Toast.makeText(EditManagerForCompanyActivity.this, "Data has been updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
