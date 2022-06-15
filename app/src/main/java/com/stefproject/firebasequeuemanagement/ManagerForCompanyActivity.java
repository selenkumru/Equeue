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
import android.widget.ImageView;
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

public class ManagerForCompanyActivity extends AppCompatActivity {

    private ImageView imgAddManager;
    private ListView lstManager;
    ArrayList<String> array = new ArrayList<>();
    ArrayList<String> arrUid =new ArrayList<>();
    DatabaseReference rootRef;
    SharedPreferences sharedPreferences;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_for_company);
        sharedPreferences = getSharedPreferences(QueueApp.sharedName,MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();
        lstManager = (ListView)findViewById(R.id.lstManager);
        imgAddManager = (ImageView)findViewById(R.id.imgAddManager);

        imgAddManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerForCompanyActivity.this,AddManagerForCompanyActivity.class);
                startActivity(intent);
            }
        });

        rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("Manager");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                array.clear();
                arrUid.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    String uid = ds.child("uid").getValue(String.class);
                    String companyname = ds.child("companyname").getValue(String.class);
                    String companyUid = ds.child("companyUid").getValue(String.class);
                    Log.d("TAG",companyUid+" ==  "+firebaseAuth.getUid());
                    if(companyUid.equals(firebaseAuth.getUid()))
                    {
                        array.add(name);
                        arrUid.add(uid);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(ManagerForCompanyActivity.this, android.R.layout.simple_list_item_1, array);
                lstManager.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersdRef.addValueEventListener(eventListener);
        registerForContextMenu(lstManager);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.lstManager) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.edit:
                // edit stuff here
                Intent intent = new Intent(ManagerForCompanyActivity.this,EditManagerForCompanyActivity.class);
                intent.putExtra("userId",arrUid.get(info.position));
                startActivity(intent);
                return true;
            case R.id.delete:
                // remove stuff here
                DatabaseReference databaseReference = rootRef.child("Manager").child(arrUid.get(info.position));
                databaseReference.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                    }
                });
                Toast.makeText(ManagerForCompanyActivity.this, "I am here"+info.position, Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
