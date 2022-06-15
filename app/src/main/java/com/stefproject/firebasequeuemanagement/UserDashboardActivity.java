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

public class UserDashboardActivity  extends AppCompatActivity {

    private ListView lstQueue;
    FirebaseAuth firebaseAuth;
    ArrayList<String> array = new ArrayList<>();
    ArrayList<String> arrUid =new ArrayList<>();
    DatabaseReference rootRef;
    private Button btnLogout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        sharedPreferences = getSharedPreferences(QueueApp.sharedName,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        lstQueue = (ListView)findViewById(R.id.lstQueue);
        firebaseAuth = FirebaseAuth.getInstance();

        rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("Queue");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                array.clear();
                arrUid.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    String uid = ds.child("queueId").getValue(String.class);
                    String companyUid = ds.child("companyUid").getValue(String.class);
                    Log.d("TAG", name);
                        array.add(name);
                        arrUid.add(uid);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(UserDashboardActivity.this, android.R.layout.simple_list_item_1, array);
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
                Intent intent  = new Intent(UserDashboardActivity.this,SingleQueueActivity.class);
                intent.putExtra("id",arrUid.get(position));
                startActivity(intent);
            }
        });

        btnLogout = (Button)findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                editor.putString(QueueApp.sharedUserType,"");
                editor.commit();
                Intent intent = new Intent(UserDashboardActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
