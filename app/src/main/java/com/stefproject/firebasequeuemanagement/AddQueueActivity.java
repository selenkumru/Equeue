package com.stefproject.firebasequeuemanagement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class AddQueueActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView txtLogin;
    private DatabaseReference databaseReference;
    private TextView btnSignUp;
    private MaterialEditText edQueueName;
    private Spinner spiSelectManager;
    ArrayList<String> managerName = new ArrayList<>();
    String selectedUserType="",selectedUid="";
    private DatabaseReference rootRef;
    SharedPreferences sharedPreferences;
    ArrayList<String> arrUid = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_queue);
        auth=FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(QueueApp.sharedName,MODE_PRIVATE);
        btnSignUp = (TextView) findViewById(R.id.btnSignUp);
        //     txtLogin = (TextView)findViewById(R.id.txtLogin);
        edQueueName = (MaterialEditText)findViewById(R.id.edQueueName);
        spiSelectManager = (Spinner)findViewById(R.id.spiSelectManager);

        rootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference usersdRef = rootRef.child("Manager");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                managerName.clear();
                arrUid.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    String uid = ds.child("uid").getValue(String.class);
                    String companyname = ds.child("companyname").getValue(String.class);
                    Log.d("TAG", name);
                    if(sharedPreferences.getString(QueueApp.companyName,"").equals(companyname))
                    {
                        managerName.add(name);
                        arrUid.add(uid);
                    }
                }
                spiSelectManager.setAdapter(new MySpinnerAdapter(AddQueueActivity.this,R.layout.textview_spinner,managerName));
                selectedUserType = managerName.get(0);
                selectedUid = arrUid.get(0);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersdRef.addValueEventListener(eventListener);

        spiSelectManager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUserType = managerName.get(position);
                selectedUid = arrUid.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userInformation();
            }
        });
    }
    private void userInformation(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String name =edQueueName.getText().toString().trim();
        FirebaseUser user = auth.getCurrentUser();
        DatabaseReference queueRef = rootRef.child("Queue");
        String key = queueRef.push().getKey();
        QueueInformation queueInformation = new QueueInformation(name,selectedUserType,selectedUid,key,user.getUid());
        databaseReference.child("Queue").child(key).setValue(queueInformation);
        Toast.makeText(getApplicationContext(),"Queue has been added",Toast.LENGTH_LONG).show();
        finish();
    }
}
