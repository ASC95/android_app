package com.example.austinchang.testproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.cloudinary.utils.ObjectUtils;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cloudinary.*;

public class MainActivity extends FragmentActivity implements View.OnClickListener, OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener{

    //Globals for locations
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected LocationRequest locationRequest;
    protected FusedLocationProviderApi fusedLocationProviderApi;
    protected Double longitudeDouble;
    protected Double latitudeDouble;
    protected List<Place> mGeofenceList = new ArrayList<Place>();

    //Globals for uploading
    private String mCurrentPhotoPath;
    private File uploadFile;
    private Uri mPhotoUri;
    private String uploadImagePath;
    private String cloudTag;

    //Global radius for posting
    static final double RADIUS = 25.0;

    //Global list view
    private ListView mListView;

    //Global Cloudinary api
    protected Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dlw60s6pl",
            "api_key", "181639785459231",
            "api_secret", "reNtqzWpDj-GQ06v6cvWvKDOH90"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_listview); //redundant?
        final ArrayList<UVALocation> locationList = UVALocation.getLocationsFromFile("locations.json", this);
        mListView = (ListView) findViewById(R.id.locations_list_view);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(MainActivity.this, DetailActivity.class);
                String locationKey = "UVALocationObject";
                String viewKey = "view";

                myIntent.putExtra(locationKey, parent.getItemAtPosition(position));
                myIntent.putExtra(viewKey, view);

                /*
                this is the UVALocation that was clicked. I need description, title, timestamp, image
                UVALocation has title, timeStamp, I'll add description.
                I just need to pass the view, then call findViewbyID to get the same image
                 */
                parent.getItemAtPosition(position);

                /*I made the detail page id the same as the listitem ID. Hopefully they are the same?*/
                //(NetworkImageView) view.findViewById(R.id.locationImage);

                startActivity(myIntent);
            }
        });

        final MainListViewAdapter adapter = new MainListViewAdapter(this, locationList);
        mListView.setAdapter(adapter);

        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {
            @Override
            public void run() {
                adapter.shouldUpdate = true;
                adapter.notifyDataSetChanged();
                adapter.shouldUpdate = false;

                Toast.makeText(MainActivity.this, "Handler called", Toast.LENGTH_SHORT).show();

                handler.postDelayed( this, 60 * 1000 );
            }
        }, 60 * 1000 );

        findViewById(R.id.settingsButton).setOnClickListener(this);
        findViewById(R.id.postButton).setOnClickListener(this);
        checkFinePermission();
        getLocation();
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        // Helper method to set up places
        setUpLocations();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                mPhotoUri = photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            Uri imageUri = mPhotoUri;
            uploadFile = new File(mPhotoUri.getPath());


            uploadImagePath = imageUri.getPath();
            Toast.makeText(MainActivity.this, "Photo successfully uploaded!",
                    Toast.LENGTH_SHORT).show();

            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute();

        }
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;


        @Override
        protected String doInBackground(String... params) {
            try {
                //getAbsolutePath()
                cloudinary.uploader().upload(mCurrentPhotoPath,ObjectUtils.asMap("tags",cloudTag,"folder",cloudTag));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resp;
        }
    }

    public void uploadToCloud(){

    }

    public void dialog(String placeName){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Would You Like To Post?");
        builder.setMessage("You are in range of "+placeName+". Would you" +
                " like to post?");
        builder.setNegativeButton("Cancel",null);
        builder.setPositiveButton("Post", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dispatchTakePictureIntent();
            }
        });
        builder.create();
        builder.show();

    }

    // Set up our 4 locations (we are going to have more, but not for this milestone)
    public void setUpLocations() {
        mGeofenceList.add(new Place("Clemons Library",  -78.506091, 38.036355,"Clemons_Library"));
        mGeofenceList.add(new Place("Einstein Bros. Bagels", -78.510684, 38.031631,"Einstein_Bros"));
        mGeofenceList.add(new Place("Observatory Hill Dining Hall", -78.515074, 38.034868,"Observatory_Hill"));
        mGeofenceList.add(new Place("Pavilion XI", -78.506740, 38.035955,"Pavilion_XI"));
        mGeofenceList.add(new Place("Dumpling Cart", -78.506118, 38.034069,"Dumpling_Cart"));

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
    }


    @Override
    public void onStop() {
        //mGoogleApiClient.disconnect();
        super.onStop();
    }

    /**
     * Restore preferences from the shared preferences file.
     */
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences settings = getSharedPreferences("myFile", MODE_PRIVATE);
        String name = settings.getString("username", "DefaultName");
        TextView textView = (TextView) findViewById(R.id.usernameDisplay);
        textView.setText("Welcome " + name + "!");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            // 3 is access fineLocation i think
            case 3: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, permissionsActicity.class);
                    startActivity(intent);

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {

            } else {
                Toast.makeText(MainActivity.this, "mLast is null",
                        Toast.LENGTH_LONG).show();
            }
        }
        catch(SecurityException e){
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        longitudeDouble = location.getLongitude();
        latitudeDouble = location.getLatitude();
        //This is what our location is in. UVALocation
    }

    // Uses the haversine formula to calculate if you are within 20m of the GPS coords of that
    //  particular location
    public boolean inGeofence(Double in_long1, Double in_lat1, Double in_long2, Double in_lat2) {
        final int R = 6371; // earths rad in KM
        Double radius = RADIUS / 1000; //radius we are using in m (/1000 for km) =

        Double latDistance = toRad(in_lat2 - in_lat1);
        Double lonDistance = toRad(in_long2 - in_long1);

        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(in_lat1)) * Math.cos(toRad(in_lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        Double distance = R * c;

        // Within radius
        if (distance <= radius) {
//            Toast.makeText(MainActivity.this, "You are: " + Math.round((distance * 1000) * 100d) / 100d + "m away, Feel free to post (feature coming soon)",
//                    Toast.LENGTH_LONG).show();
            return true;
        }
        // Out of radius
        else {
//            Toast.makeText(MainActivity.this, "You are: " + Math.round((distance * 1000) * 100d) / 100d + "m  away, (Too far away to post)",
//                    Toast.LENGTH_LONG).show();
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

                // Will be false if they are not at one of the locations and give them an alert/dialogue
                // saying they are out of range of any of the locations
                //Will return true if they are within the specified radius of a locations
                boolean in_a_loc_flag = false;
                Place nearby_Place = null;
                for (int i = 0; i < mGeofenceList.size(); i++) {
                    Double tempLat1 = mGeofenceList.get(i).getLatitude();
                    Double tempLon1 = mGeofenceList.get(i).getLongitude();

                    Double tempLat2 = latitudeDouble;
                    Double tempLon2 = longitudeDouble;

                    boolean flag = inGeofence(tempLon1, tempLat1, tempLon2, tempLat2);

                    String name = mGeofenceList.get(i).getName();

                    //If this is true, you are in a geofence and will be prompted with a dialogue
                    if (flag) {
                        in_a_loc_flag = true;
                        nearby_Place = mGeofenceList.get(i);
                        // You can only be in one location at once, no need to search through all the fences
                        break;
                    }
                    else {
                        //Do something else if you are not in a location if you really want too
                    }

                }

                if (in_a_loc_flag == true){
                    String nearby_Place_Name = nearby_Place.getName();
                    cloudTag = nearby_Place.getCloudTag();

                    dialog(nearby_Place_Name);
                }
                //Open alert to say you are not in range of any postable place, right now just a toast
                else if (in_a_loc_flag == false){
                    Toast.makeText(MainActivity.this, "Sorry, you are not in range to post anywhere",
                            Toast.LENGTH_LONG).show();
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

