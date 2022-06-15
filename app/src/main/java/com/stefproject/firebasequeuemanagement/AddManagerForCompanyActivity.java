package com.stefproject.firebasequeuemanagement;

import android.content.SharedPreferences;
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

public class AddManagerForCompanyActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView txtLogin;
    private DatabaseReference databaseReference;
    private TextView btnSignUp;
    private MaterialEditText edManagerName,edUsername,edPassword,edPhone;
    private FirebaseAuth mAuth2;
    private DatabaseReference rootRef;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manager_for_company);

        sharedPreferences = getSharedPreferences(QueueApp.sharedName,MODE_PRIVATE);

        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        auth=FirebaseAuth.getInstance();
        btnSignUp = (TextView) findViewById(R.id.btnSignUp);
   //     txtLogin = (TextView)findViewById(R.id.txtLogin);
        edManagerName = (MaterialEditText)findViewById(R.id.edManagerName);

        rootRef = FirebaseDatabase.getInstance().getReference();

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
                            .addOnCompleteListener(AddManagerForCompanyActivity.this, new OnCompleteListener<AuthResult>() {
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(AddManagerForCompanyActivity.this, "ERROR",Toast.LENGTH_LONG).show();
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
        ManagerInformation userinformation = new ManagerInformation(name,mAuth2.getUid(),sharedPreferences.getString(QueueApp.companyName,""),edUsername.getText().toString().trim(),auth.getUid());
        FirebaseUser user = mAuth2.getCurrentUser();
        databaseReference.child("Manager").child(user.getUid()).setValue(userinformation);
        databaseReference.child("TypeData").child(user.getUid()).setValue(new UserData(edUsername.getText().toString().trim(),"manager"));
        mAuth2.signOut();
        Toast.makeText(getApplicationContext(),"Manager has been added",Toast.LENGTH_LONG).show();
        finish();
    }
}
