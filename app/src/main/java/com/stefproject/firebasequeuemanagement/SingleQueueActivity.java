package com.stefproject.firebasequeuemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SingleQueueActivity  extends AppCompatActivity {

    private Button btnAdd;
    private ListView lstQueue;
    Intent intent;
    String queueId="";
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private DatabaseReference rootRef;
    SharedPreferences sharedPreferences;
    ArrayList<String> array = new ArrayList<>();
    ArrayList<String> arrUid = new ArrayList<>();
    ArrayList<String> arrUserId= new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_quote);
        sharedPreferences =getSharedPreferences(QueueApp.sharedName,MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();
        intent = getIntent();
        queueId = intent.getExtras().getString("id");
        btnAdd = (Button)findViewById(R.id.btnAdd);
        lstQueue = (ListView)findViewById(R.id.lstQueue);

        rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("QueueList");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                array.clear();
                arrUid.clear();
                arrUserId.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    String uid = ds.child("queueId").getValue(String.class);
                    String userUid = ds.child("uid").getValue(String.class);
                    if(uid.equals(queueId))
                    {
                        array.add(name);
                        arrUid.add(uid);
                        arrUserId.add(userUid);
                    }
                }
                if(array.size()==0)
                {
                    btnAdd.setVisibility(View.VISIBLE);
                }
                for (int i=0;i<array.size();i++)
                {

                    Log.w("Data","arrUid-> "+arrUid.get(i)+" -  Queue ID-> "+queueId +" -  arrUserID-> "+arrUserId.get(i)+" - User ID->"+auth.getUid());
                    if(arrUid.get(i).equals(queueId) && arrUserId.get(i).equals(auth.getUid()))
                    {
                        btnAdd.setVisibility(View.GONE);
                    }
                    else
                    {
                        btnAdd.setVisibility(View.VISIBLE);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(SingleQueueActivity.this, android.R.layout.simple_list_item_1, array);
                lstQueue.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersdRef.addValueEventListener(eventListener);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    userInformation();
                    btnAdd.setVisibility(View.GONE);
            }
        });
        rootRef = FirebaseDatabase.getInstance().getReference();
    }
    private void userInformation(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference queueRef = rootRef.child("QueueList");
        String key = queueRef.push().getKey();
        QueueListInformation queueListInformation = new QueueListInformation(auth.getUid(),queueId,key,sharedPreferences.getString(QueueApp.userName,""));
        databaseReference.child("QueueList").child(key).setValue(queueListInformation);
        Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_LONG).show();
    }
}
