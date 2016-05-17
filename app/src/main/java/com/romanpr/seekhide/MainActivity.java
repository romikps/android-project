package com.romanpr.seekhide;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.Firebase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String provider;
    Firebase myFirebaseRef;

    public void changeActivity(View view) {

        // description of an operation
        Intent i = new Intent(getApplicationContext(), SecondActivity.class);
        i.putExtra("developersName", "Roman Priscepov");
        startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://seek-n-hide.firebaseio.com/");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // false not to check if provider available
        provider = locationManager.getBestProvider(new Criteria(), false);


        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            Log.i("Location info", "Location achieved!");
        } else {
            Log.i("Location info", "No location :(");
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        final ArrayList<String> friends = new ArrayList<String>();
        friends.add("Roman K.");
        friends.add("Roman P.");
        friends.add("Rozerin");
        friends.add("Tansel");
        friends.add("Yekta");
        friends.add("Beste");
        friends.add("Ibrahim");

        // link between a set of data and the AdapterView that displays the data
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                                                        friends);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), SecondActivity.class);
                i.putExtra("hidersName", friends.get(position));
                startActivity(i);
            }
        });

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

        myFirebaseRef.child("location").setValue(lat.toString() + ", " + lng.toString());


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

    public void getLocation(View view) {

        Location location = locationManager.getLastKnownLocation(provider);

        onLocationChanged(location);
    }
}
