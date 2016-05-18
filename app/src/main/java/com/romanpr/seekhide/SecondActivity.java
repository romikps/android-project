package com.romanpr.seekhide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SecondActivity extends AppCompatActivity implements LocationListener {

    Firebase players;
    LocationManager locationManager;
    Location location;
    String provider;
    String playersName, hidersName;
    TextView distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Firebase.setAndroidContext(this);
        players = new Firebase("https://seek-n-hide.firebaseio.com/players/");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // false not to check if provider available
        provider = locationManager.getBestProvider(new Criteria(), false);
        location = locationManager.getLastKnownLocation(provider);

        TextView welcomeText = (TextView) findViewById(R.id.welcomeText);
        Typeface typeFace= Typeface.createFromAsset(getAssets(), "fonts/blood_font.ttf");
        welcomeText.setTypeface(typeFace);

        distance = (TextView) findViewById(R.id.distance);

        Intent i = getIntent(); // gets intent that ended us up in this activity, can extract from
        // Log.i("Developer's name", i.getStringExtra("developersName"));
        hidersName = i.getStringExtra("hidersName");
        playersName = i.getStringExtra("playersName");
        welcomeText.setText(playersName + ", you are looking for " + hidersName +
                            ". But you never know who is looking for ya :-)");
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

    public double degToRad(double deg) {
        return deg * Math.PI/180;
    }

    public double getDistance(double lat1, double lng1, double lat2, double lng2) {
        int R = 6371 * 1000; // Radius of the earth in m
        double dLat = degToRad(lat2-lat1);
        double dLng = degToRad(lng2 - lng1);
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(degToRad(lat1)) * Math.cos(degToRad(lat2)) *
                                Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in m
        return d;
    }

    public void getDistance(View view) {

        players.child(hidersName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String hLat = dataSnapshot.child("latitude").getValue().toString();
                String hLng = dataSnapshot.child("longitude").getValue().toString();
                Log.i("Hider's Location", hLat + ", " + hLng);

                Double dist = getDistance(Double.parseDouble(hLat), Double.parseDouble(hLng),
                                location.getLatitude(), location.getLongitude());
                distance.setText(dist.toString().substring(0, dist.toString().indexOf(".")) + " m.");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

        if (playersName != null && !playersName.isEmpty()) {
            players.child(playersName).child("latitude").setValue(location.getLatitude());
            players.child(playersName).child("longitude").setValue(location.getLongitude());
            this.location = location;
            // Log.i("New location", this.location.toString());
        } else {
            Log.i("Location", "Something's wrong with changing location");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onResume() {

        super.onResume();
        locationManager.requestLocationUpdates(provider, 3000, 1, this);
    }

    @Override
    protected void onPause() {

        super.onPause();
        locationManager.removeUpdates(this);


    }
}
