package com.stefproject.firebasequeuemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

public class AddManagerActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView txtLogin;
    private DatabaseReference databaseReference;
    private TextView btnSignUp;
    private MaterialEditText edManagerName,edUsername,edPassword,edPhone;
    private Spinner spiSelectCompany;
    ArrayList<String> companyName = new ArrayList<>();
    ArrayList<String> companyUid = new ArrayList<>();
    String selectedUserType="",selectedCompanyUid="";
    private FirebaseAuth mAuth2;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manager);
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        auth=FirebaseAuth.getInstance();
        btnSignUp = (TextView) findViewById(R.id.btnSignUp);
   //     txtLogin = (TextView)findViewById(R.id.txtLogin);
        edManagerName = (MaterialEditText)findViewById(R.id.edManagerName);
        spiSelectCompany = (Spinner)findViewById(R.id.spiSelectCompany);

        rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("Company");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                companyName.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    String uid = ds.child("uid").getValue(String.class);
                    Log.d("TAG", name);
                    companyName.add(name);
                    companyUid.add(uid);
                }
                spiSelectCompany.setAdapter(new MySpinnerAdapter(AddManagerActivity.this,R.layout.textview_spinner,companyName));
                selectedUserType = companyName.get(0);
                selectedCompanyUid = companyUid.get(0);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersdRef.addValueEventListener(eventListener);

        spiSelectCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUserType = companyName.get(position);
                selectedCompanyUid = companyUid.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("[https://fir-queue-management-default-rtdb.firebaseio.com/]")
                .setApiKey("AIzaSyDQ1lzEG1XbcZzY1nDyjDBTPhFoixtpfQk")
                .setApplicationId("fir-queue-management").build();

        try { FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions,"Firebase Queue Management");
            mAuth2 = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e){
            mAuth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance("Firebase Queue Management"));
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = edUsername.getText().toString();
                String pass = edPassword.getText().toString();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Please enter your E-mail address",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(getApplicationContext(),"Please enter your Password",Toast.LENGTH_LONG).show();
                }
                if (pass.length() == 0){
                    Toast.makeText(getApplicationContext(),"Please enter your Password",Toast.LENGTH_LONG).show();
                }
                   if (pass.length()<8){
                    Toast.makeText(getApplicationContext(),"Password must be more than 8 digit",Toast.LENGTH_LONG).show();
                }
                else{
                    mAuth2.createUserWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(AddManagerActivity.this, new OnCompleteListener<AuthResult>() {
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(AddManagerActivity.this, "ERROR",Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        /*startActivity(new Intent(SignupActivity.this, EditProfileActivity.class));
                                        finish();*/
                                        auth =FirebaseAuth.getInstance();
                                        userInformation();
                                    }
                                }
                            });}
            }
        });
    }
    private void userInformation(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String name =edManagerName.getText().toString().trim();
        ManagerInformation userinformation = new ManagerInformation(name,mAuth2.getUid(),selectedUserType,edUsername.getText().toString().trim(),selectedCompanyUid);
        FirebaseUser user = mAuth2.getCurrentUser();
        databaseReference.child("Manager").child(user.getUid()).setValue(userinformation);
        databaseReference.child("TypeData").child(user.getUid()).setValue(new UserData(edUsername.getText().toString().trim(),"manager"));
        mAuth2.signOut();
        Toast.makeText(getApplicationContext(),"Manager Successfully Added",Toast.LENGTH_LONG).show();
        finish();
    }
}
