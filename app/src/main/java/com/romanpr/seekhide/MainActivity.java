package com.romanpr.seekhide;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Firebase players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        players = new Firebase("https://seek-n-hide.firebaseio.com/players/");

        TextView welcomeMssg = (TextView) findViewById(R.id.welcomeMssg);

        Typeface typeFace= Typeface.createFromAsset(getAssets(), "fonts/blood_font.ttf");
        String playersName = getIntent().getStringExtra("playersName");
        welcomeMssg.setTypeface(typeFace);



        final ListView listView = (ListView) findViewById(R.id.listView);
        final ArrayList<String> friends = new ArrayList<String>();
        players.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot player : dataSnapshot.getChildren()) {
                    Log.i("player", player.getKey());
                    friends.add(player.getKey());
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this,
                        android.R.layout.simple_list_item_1, friends);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

            // link between a set of data and the AdapterView that displays the data
            /*ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                    friends);
            listView.setAdapter(arrayAdapter);*/
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(getApplicationContext(), SecondActivity.class);
                    i.putExtra("hidersName", friends.get(position));
                    startActivity(i);
                }
            });

    }




    public void changeActivity(View view) {
        // description of an operation
        Intent i = new Intent(getApplicationContext(), SecondActivity.class);
        i.putExtra("developersName", "Roman Priscepov");
        startActivity(i);
    }

}
