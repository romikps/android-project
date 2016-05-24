package com.romanpr.seekhide;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("players");

        TextView welcomeMssg = (TextView) findViewById(R.id.welcomeMssg);
        Typeface typeFace= Typeface.createFromAsset(getAssets(), "fonts/blood_font.ttf");
        welcomeMssg.setTypeface(typeFace);

        playerName = getIntent().getStringExtra("playerName");

        final ListView listView = (ListView) findViewById(R.id.listView);
        final ArrayList<String> users = new ArrayList<String>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    users.add(user.getKey());
                }
                // link between a set of data and the AdapterView that displays the data
                ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this,
                        android.R.layout.simple_list_item_1, users);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), SecondActivity.class);
                i.putExtra("findName", users.get(position));
                i.putExtra("playerName", playerName);
                startActivity(i);
            }
        });

    }

}
