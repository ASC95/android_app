package com.example.austinchang.testproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener {

    protected GoogleApiClient mGoogleApiClient;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    protected Location mLastLocation;
    protected LocationRequest locationRequest;
    protected FusedLocationProviderApi fusedLocationProviderApi;
    protected Double longitudeDouble;
    protected Double latitudeDouble;
    protected List<Place> mGeofenceList = new ArrayList<Place>();
    static final float RADIUS = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Buttons
        findViewById(R.id.settingsButton).setOnClickListener(this);
        findViewById(R.id.postButton).setOnClickListener(this);

        //Check permissions
        checkFinePermission();

        // Set our initial location request
        getLocation();

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        setUpLocations();
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Print some toast to show we couldn't connect
        Toast.makeText(MainActivity.this, "Problem connecting",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
//        Toast.makeText(MainActivity.this, "Connected from onStart()",
//                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /**
     * Restore preferences from the shared preferences file. It is getting called!
     */
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences settings = getSharedPreferences("myFile", MODE_PRIVATE);
        String name = settings.getString("username", "DefaultName");
        TextView textView = (TextView) findViewById(R.id.usernameDisplay);
        textView.setText("Welcome " + name + "!");
    }

    /**
     * This changes the text, but the text goes back to the hint when I return. Why is that?
     */
    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
//                Toast.makeText(MainActivity.this, "mLast is NOT null",
//                        Toast.LENGTH_LONG).show();
//                mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
//                mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));

            } else {
                Toast.makeText(MainActivity.this, "mLast is null",
                        Toast.LENGTH_LONG).show();
            }

        } catch (SecurityException e) {
            Toast.makeText(MainActivity.this, e.getMessage(),
                    Toast.LENGTH_LONG).show();


        }

    }

    @Override
    public void onLocationChanged(Location location) {
        longitudeDouble = location.getLongitude();
        latitudeDouble = location.getLatitude();
        //This is what our location is in. Location
//        Toast.makeText(MainActivity.this, "location :"+location.getLatitude()+" , "+location.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    // Set up our 4 locations (we are going to have more, but not for this milestone)
    public void setUpLocations() {
        mGeofenceList.add(new Place("Einstein Bagel's", -78.510684, 38.031631));
        mGeofenceList.add(new Place("O'Hill Dining Hall", -78.515074, 38.034868));
        mGeofenceList.add(new Place("The Pav", -78.506740, 38.035955));
        mGeofenceList.add(new Place("Dumpling Cart", -78.506118, 38.034069));

    }

    // Uses the haversine formula to calculate if you are within 20m of the GPS coords of that
    //  particular location
    public boolean inGeofence(Double in_long1, Double in_lat1, Double in_long2, Double in_lat2) {
        final int R = 6371; // earths rad in KM
        Double radius = 20.0 / 1000; //radius we are using in m (/1000 for km) =

        Double latDistance = toRad(in_lat2 - in_lat1);
        Double lonDistance = toRad(in_long2 - in_long1);

        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(in_lat1)) * Math.cos(toRad(in_lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        Double distance = R * c;

        if (distance <= radius) {
            Toast.makeText(MainActivity.this, "You are: " + Math.round((distance * 1000) * 100d) / 100d + "m away, Feel free to post (feature coming soon)",
                    Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(MainActivity.this, "You are: " + Math.round((distance * 1000) * 100d) / 100d + "m  away, (Too far away to post)",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    // Helper method for inGeofence
    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settingsButton:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.postButton:
                //For loop checking if they are "dwelling" in GeoFence that goes through list of GF and
                //  checks which we are "in". Will return the ID of the geofence which we will then use to
                //  say "Hey man you can post here" if they click the post button"
                getLocation();


                for (int i = 0; i < mGeofenceList.size(); i++) {
                    Double tempLat1 = mGeofenceList.get(i).getLatitude();
                    Double tempLon1 = mGeofenceList.get(i).getLongitude();

                    Double tempLat2 = latitudeDouble;
                    Double tempLon2 = longitudeDouble;

                    boolean flag = inGeofence(tempLon1, tempLat1, tempLon2, tempLat2);

                    String name = mGeofenceList.get(i).getName();
                    if (flag) {
                        Toast.makeText(MainActivity.this, "You are near: " + name + ". Would you like to post? (Post feature coming soon)",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "You are not in range to post at: " + name + " at this time.",
                                Toast.LENGTH_LONG).show();
                    }

                }
                break;


            default:
                break;
        }
    }

    private void getLocation() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(30000);
        fusedLocationProviderApi = LocationServices.FusedLocationApi;

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    //For some reason this only runs when the app is first installed. This is what is allowing 
    // us to use location since we are past the target API
    private void checkFinePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3);
        }
    }


}

