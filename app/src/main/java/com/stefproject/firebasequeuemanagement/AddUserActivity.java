package com.stefproject.firebasequeuemanagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

public class AddUserActivity extends AppCompatActivity {

    private MaterialEditText edUsername;
    private MaterialEditText edPassword;
    private FirebaseAuth auth;
    private TextView btnSignUp;
    private MaterialEditText edFirstName;
    private MaterialEditText edLastName;
    private MaterialEditText edPhone;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        auth= FirebaseAuth.getInstance();
        btnSignUp = (TextView) findViewById(R.id.btnSignUp);
        //     txtLogin = (TextView)findViewById(R.id.txtLogin);
        edFirstName = (MaterialEditText)findViewById(R.id.edFirstName);
        edLastName = (MaterialEditText)findViewById(R.id.edLastName);
        edPhone = (MaterialEditText)findViewById(R.id.edPhone);


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
                if(edFirstName.getText().toString().trim().length()==0)
                {
                    Toast.makeText(AddUserActivity.this, "Please Type First Name", Toast.LENGTH_SHORT).show();
                }
                if(edLastName.getText().toString().trim().length()==0)
                {
                    Toast.makeText(AddUserActivity.this, "Please Type Last Name", Toast.LENGTH_SHORT).show();
                }
                if(edPhone.getText().toString().trim().length()==0)
                {
                    Toast.makeText(AddUserActivity.this, "Please Type Phone Number", Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth2.createUserWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(AddUserActivity.this, new OnCompleteListener<AuthResult>() {
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    task.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Exception",e);
                                        }
                                    });
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(AddUserActivity.this, "ERROR",Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        /*startActivity(new Intent(SignupActivity.this, EditProfileActivity.class));
                                        finish();*/
                                        userInformation();
                                    }
                                }
                            });}
            }
        });
    }
    private void userInformation(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String name = edFirstName.getText().toString().trim();
        String surname = edLastName.getText().toString().trim();
        String phoneno = edPhone.getText().toString().trim();
        Userinformation userinformation = new Userinformation(name,surname,phoneno, mAuth2.getUid(),edUsername.getText().toString().trim());
        FirebaseUser user = mAuth2.getCurrentUser();
        UserData userData = new UserData(edUsername.getText().toString().trim(),"user");
        databaseReference.child("TypeData").child(user.getUid()).setValue(userData);
        databaseReference.child("Users").child(user.getUid()).setValue(userinformation);
        mAuth2.signOut();
        Toast.makeText(getApplicationContext(),"User information updated",Toast.LENGTH_LONG).show();
        finish();
    }


}
