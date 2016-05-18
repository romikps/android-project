package com.romanpr.seekhide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class SecondActivity extends AppCompatActivity {

    Firebase myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://seek-n-hide.firebaseio.com/");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView welcomeText = (TextView) findViewById(R.id.welcomeText);
        Intent i = getIntent(); // gets intent that ended us up in this activity, can extract from
        // Log.i("Developer's name", i.getStringExtra("developersName"));
        welcomeText.setText("Hello, " + i.getStringExtra("hidersName"));
        // You're looking for Rozerin now, but you never know who is looking for you.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getDistance(View view) {
        myFirebaseRef.child("location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView dist = (TextView) findViewById(R.id.distance);
                dist.setText(dataSnapshot.getValue().toString());
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }
}
