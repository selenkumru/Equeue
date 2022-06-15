package com.stefproject.firebasequeuemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    EditText SignUpMail,SignUpPass;
    private FirebaseAuth auth;
    private TextView txtLogin;
    private EditText EditTextName;
    private EditText EditTextSurname;
    private EditText EditTextPhoneNo;
    private DatabaseReference databaseReference;
    private TextView btnSignUp;
    private MaterialEditText edFirstName,edLastName,edUsername,edPassword,edPhone;
    private Spinner spiUserType;
    ArrayList<String> userTYpe = new ArrayList<>();
    String selectedUserType="User";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        auth=FirebaseAuth.getInstance();
        btnSignUp = (TextView) findViewById(R.id.btnSignUp);
   //     txtLogin = (TextView)findViewById(R.id.txtLogin);
        edFirstName = (MaterialEditText)findViewById(R.id.edFirstName);
        edLastName = (MaterialEditText)findViewById(R.id.edLastName);
        edPhone = (MaterialEditText)findViewById(R.id.edPhone);
        spiUserType = (Spinner)findViewById(R.id.spiUserType);

        userTYpe.add("User");
        userTYpe.add("Admin");
        spiUserType.setAdapter(new MySpinnerAdapter(SignupActivity.this,R.layout.textview_spinner,userTYpe));

        spiUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUserType = userTYpe.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                       Toast.makeText(SignupActivity.this, "Please Type First Name", Toast.LENGTH_SHORT).show();
                   }
                   if(edLastName.getText().toString().trim().length()==0)
                   {
                       Toast.makeText(SignupActivity.this, "Please Type Last Name", Toast.LENGTH_SHORT).show();
                   }
                   if(edPhone.getText().toString().trim().length()==0)
                   {
                       Toast.makeText(SignupActivity.this, "Please Type Phone Number", Toast.LENGTH_SHORT).show();
                   }
                else{
                    auth.createUserWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, "ERROR",Toast.LENGTH_LONG).show();
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
        String name = edFirstName.getText().toString().trim();
        String surname = edLastName.getText().toString().trim();
        String phoneno = edPhone.getText().toString().trim();
        Userinformation userinformation = new Userinformation(name,surname,phoneno,selectedUserType,edUsername.getText().toString().trim());
        FirebaseUser user = auth.getCurrentUser();
        databaseReference.child(user.getUid()).setValue(userinformation);
        Toast.makeText(getApplicationContext(),"User information updated",Toast.LENGTH_LONG).show();
    }


    public void navigate_sign_in(View v){
        Intent inent = new Intent(this, SignInActivity.class);
        startActivity(inent);
    }
}
