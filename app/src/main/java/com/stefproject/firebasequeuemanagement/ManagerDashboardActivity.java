package com.stefproject.firebasequeuemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagerDashboardActivity extends AppCompatActivity {

    private ListView lstQueue;
    private Button btnLogout;
    FirebaseAuth firebaseAuth;
    ArrayList<String> array = new ArrayList<>();
    ArrayList<String> arrUid =new ArrayList<>();
    ArrayList<String> arrQueueId =new ArrayList<>();
    DatabaseReference rootRef;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_dashboard);
        sharedPreferences = getSharedPreferences(QueueApp.sharedName,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        lstQueue = (ListView)findViewById(R.id.lstQueue);
        btnLogout = (Button)findViewById(R.id.btnLogout);
        firebaseAuth = FirebaseAuth.getInstance();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(QueueApp.sharedUserType,"");
                editor.commit();
                firebaseAuth.signOut();
                Intent  intent = new Intent(ManagerDashboardActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("Queue");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                array.clear();
                arrUid.clear();
                arrQueueId.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    String uid = ds.child("uid").getValue(String.class);
                    String companyUid = ds.child("companyUid").getValue(String.class);
                    String queueId = ds.child("queueId").getValue(String.class);
                    Log.d("TAG", name);
                    if(uid.equals(firebaseAuth.getUid()))
                    {
                        array.add(name);
                        arrUid.add(uid);
                        arrQueueId.add(queueId);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(ManagerDashboardActivity.this, android.R.layout.simple_list_item_1, array);
                lstQueue.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersdRef.addValueEventListener(eventListener);

        lstQueue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ManagerDashboardActivity.this,SingleQueueForManagerActivity.class);
                intent.putExtra("id",arrQueueId.get(position));
                startActivity(intent);
            }
        });
    }
}
