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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;

    LocationManager locationManager;
    Location mCurrentLocation;
    LocationListener locationListener;
    double findLatitude, findLongitude;
    String playerName, findName;
    TextView distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("players");

        Intent i = getIntent(); // gets intent that ended us up in this activity, can extract from
        findName = i.getStringExtra("findName");
        playerName = i.getStringExtra("playerName");

        TextView welcomeText = (TextView) findViewById(R.id.welcomeText);
        Typeface typeFace= Typeface.createFromAsset(getAssets(), "fonts/blood_font.ttf");
        welcomeText.setTypeface(typeFace);

        distance = (TextView) findViewById(R.id.distance);

        welcomeText.setText(playerName + ", you are looking for " + findName +
                            ". But you never know who is looking for ya :-)");

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                mCurrentLocation = location;
                processUser(playerName, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(locationManager.getBestProvider(new Criteria(), false),
                5000, 5, locationListener);
    }

    public void getDistance(View view) {

        myRef.child(findName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                findLatitude = Double.valueOf(dataSnapshot.child("latitude").getValue().toString());
                findLongitude = Double.valueOf(dataSnapshot.child("longitude").getValue().toString());

                if (mCurrentLocation != null) {
                    Double dist = getDistance(findLatitude, findLongitude,
                            mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    distance.setText(dist.toString().substring(0, dist.toString().indexOf(".")) + " m.");
                } else {
                    distance.setText("Location problem.");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void processUser(String name, double latitude, double longitude) {
        DatabaseReference user = myRef.child(name);
        user.child("latitude").setValue(latitude);
        user.child("longitude").setValue(longitude);
    }

    public void logLocation() {
        Log.i("Location", String.valueOf(mCurrentLocation.getLatitude())
                + ", " + String.valueOf(mCurrentLocation.getLongitude()));
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

}
