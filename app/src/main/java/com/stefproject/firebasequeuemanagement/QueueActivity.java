package com.stefproject.firebasequeuemanagement;

import android.content.Intent;
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
import java.util.Queue;

public class QueueActivity extends AppCompatActivity {

    private ListView lstQueue;
    private ImageView imgAddQueue;
    ArrayList<String> array = new ArrayList<>();
    ArrayList<String> arrUid =new ArrayList<>();
    DatabaseReference rootRef;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        lstQueue = (ListView)findViewById(R.id.lstQueue);
        imgAddQueue = (ImageView)findViewById(R.id.imgAddQueue);
        firebaseAuth = FirebaseAuth.getInstance();
        imgAddQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(QueueActivity.this,AddQueueActivity.class);
                startActivity(intent);
            }
        });

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
                    if(companyUid.equals(firebaseAuth.getUid()))
                    {
                        array.add(name);
                        arrUid.add(uid);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(QueueActivity.this, android.R.layout.simple_list_item_1, array);
                lstQueue.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersdRef.addValueEventListener(eventListener);
        registerForContextMenu(lstQueue);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.lstQueue) {
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
                Intent intent = new Intent(QueueActivity.this,EditQueueActivity.class);
                intent.putExtra("queueId",arrUid.get(info.position));
                startActivity(intent);
                return true;
            case R.id.delete:
                // remove stuff here
                DatabaseReference databaseReference = rootRef.child("Company").child(arrUid.get(info.position));
                databaseReference.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                    }
                });
                Toast.makeText(QueueActivity.this, "I am here"+info.position, Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
