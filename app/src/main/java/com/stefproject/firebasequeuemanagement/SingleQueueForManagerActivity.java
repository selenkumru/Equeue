package com.stefproject.firebasequeuemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SingleQueueForManagerActivity extends AppCompatActivity {

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
    ArrayList<String> arrQueueListID = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_quote_for_manager);
        sharedPreferences =getSharedPreferences(QueueApp.sharedName,MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();
        intent = getIntent();
        queueId = intent.getExtras().getString("id");
        lstQueue = (ListView)findViewById(R.id.lstQueue);

        rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("QueueList");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                array.clear();
                arrUid.clear();
                arrUserId.clear();
                arrQueueListID.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    String uid = ds.child("queueId").getValue(String.class);
                    String userUid = ds.child("uid").getValue(String.class);
                    String queueListId =ds.child("queueListId").getValue(String.class);
                    if(uid.equals(queueId))
                    {
                        array.add(name);
                        arrUid.add(uid);
                        arrUserId.add(userUid);
                        arrQueueListID.add(queueListId);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(SingleQueueForManagerActivity.this, android.R.layout.simple_list_item_1, array);
                lstQueue.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersdRef.addValueEventListener(eventListener);

        rootRef = FirebaseDatabase.getInstance().getReference();
        registerForContextMenu(lstQueue);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.lstQueue) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.dmenu, menu);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.delete:
                // remove stuff here
                DatabaseReference databaseReference = rootRef.child("QueueList").child(arrQueueListID.get(info.position));
                databaseReference.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                    }
                });
                Toast.makeText(SingleQueueForManagerActivity.this, "I am here"+info.position, Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
