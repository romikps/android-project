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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class InitialActivity extends AppCompatActivity implements LocationListener {

    Firebase players;
    EditText playersNameView;
    LocationManager locationManager;
    Location location;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        Firebase.setAndroidContext(this);
        players = new Firebase("https://seek-n-hide.firebaseio.com/players/");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // false not to check if provider available
        provider = locationManager.getBestProvider(new Criteria(), false);
        location = locationManager.getLastKnownLocation(provider);

        Typeface typeFace= Typeface.createFromAsset(getAssets(), "fonts/blood_font.ttf");
        TextView nameTextView = (TextView) findViewById(R.id.askName);
        playersNameView = (EditText) findViewById(R.id.playersName);
        nameTextView.setTypeface(typeFace);
        playersNameView.setTypeface(typeFace);


    }

    public void changeActivity(View view) {

        String playersName = playersNameView.getText().toString();

        if (location == null)
            Log.i("Location", "Not available");
        else {
            if (playersName.isEmpty()) playersName = "Anonymous";
            players.child(playersName).child("latitude").setValue(location.getLatitude());
            players.child(playersName).child("longitude").setValue(location.getLongitude());
        }
        // description of an operation
        final Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("playersName", playersName);
        startActivity(i);
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

    @Override
    public void onLocationChanged(Location location) {
        // telnet localhost 5554
        // geo fix 11 17

        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        Float spd = location.getSpeed();

        Log.i("Latitude", lat.toString());
        Log.i("Longitude", lng.toString());
        Log.i("Speed", spd.toString());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}

    public void getLocation(View view) {
        Location location = locationManager.getLastKnownLocation(provider);
        onLocationChanged(location);
    }
}
