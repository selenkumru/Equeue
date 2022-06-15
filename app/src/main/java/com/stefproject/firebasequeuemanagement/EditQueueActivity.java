package com.stefproject.firebasequeuemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class EditQueueActivity extends AppCompatActivity {

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
    Intent intent;
    String queueId="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_queue);
        intent = getIntent();
        queueId = intent.getExtras().getString("queueId");
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
                spiSelectManager.setAdapter(new MySpinnerAdapter(EditQueueActivity.this,R.layout.textview_spinner,managerName));
                selectedUserType = managerName.get(0);
                selectedUid = arrUid.get(0);
                DatabaseReference usersdRef2 = rootRef.child("Queue");

                ValueEventListener eventListener2 = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String name = ds.child("name").getValue(String.class);
                            String uid = ds.child("queueId").getValue(String.class);
                            String companyUid = ds.child("companyUid").getValue(String.class);
                            String u = ds.child("uid").getValue(String.class);
                            Log.d("TAG", name);
                            if(uid.equals(queueId))
                            {
                                edQueueName.setText(name);
                                int index = arrUid.indexOf(u);
                                Toast.makeText(EditQueueActivity.this, ""+index, Toast.LENGTH_SHORT).show();
                                spiSelectManager.setSelection(index);
                                selectedUid = u;
                                selectedUserType = managerName.get(index);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                usersdRef2.addValueEventListener(eventListener2);
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
            @Override
            public void onClick(View v) {
                DatabaseReference updateData = FirebaseDatabase.getInstance()
                        .getReference("Queue")
                        .child(queueId);
                updateData.child("name").setValue(edQueueName.getText().toString().trim());
                updateData.child("managerName").setValue(selectedUserType);
                updateData.child("uid").setValue(selectedUid);
                Toast.makeText(EditQueueActivity.this, "Data has been updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
