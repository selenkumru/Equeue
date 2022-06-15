package com.stefproject.firebasequeuemanagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

public class AddCompanyActivity extends AppCompatActivity {

    private MaterialEditText edUsername;
    private MaterialEditText edPassword;
    private FirebaseAuth auth;
    private TextView btnSignUp;
    private MaterialEditText edCompanyName;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_company);

        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        auth= FirebaseAuth.getInstance();
        btnSignUp = (TextView) findViewById(R.id.btnSignUp);
        //     txtLogin = (TextView)findViewById(R.id.txtLogin);
        edCompanyName = (MaterialEditText)findViewById(R.id.edCompanyName);



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
                if(edCompanyName.getText().toString().trim().length()==0)
                {
                    Toast.makeText(AddCompanyActivity.this, "Please Type Company Name", Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth2.createUserWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(AddCompanyActivity.this, new OnCompleteListener<AuthResult>() {
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(AddCompanyActivity.this, "ERROR",Toast.LENGTH_LONG).show();
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
        CompanyInformation companyInformation= new CompanyInformation(edCompanyName.getText().toString().trim(),mAuth2.getUid(),edUsername.getText().toString().trim());
        FirebaseUser user = mAuth2.getCurrentUser();
        databaseReference.child("Company").child(user.getUid()).setValue(companyInformation);
        databaseReference.child("TypeData").child(user.getUid()).setValue(new UserData(edUsername.getText().toString().trim(),"company"));
        mAuth2.signOut();
        Toast.makeText(getApplicationContext(),"Company Successfully Added",Toast.LENGTH_LONG).show();
        finish();
    }
}
