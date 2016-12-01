/**
 * Thanks to Razeware and Ray Wenderlinch at https://www.raywenderlich.com/124438/android-listview-tutorial for loading json from an asset
 */
package com.example.austinchang.testproject;

import android.content.Context;

import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UVALocation {

    public String locationTitle;
    public String timeStamp;
    public String cloudTag;
    public String imageURL;
    public String description;
    //public Map<String,String> imageValues;
    //public NetworkImageView locationImage;

    public static ArrayList<UVALocation> getLocationsFromFile(String filename, Context context){
        final ArrayList<UVALocation> locationList = new ArrayList<>();

        try {
            // Load data
            String jsonString = loadJsonFromAsset("locations.json", context);
            JSONObject json = new JSONObject(jsonString);
            JSONArray locations = json.getJSONArray("locations");

            // Get UVALocation objects from data
            for(int i = 0; i < locations.length(); i++){
                UVALocation location = new UVALocation();
                location.locationTitle = locations.getJSONObject(i).getString("locationTitle");
                location.timeStamp = locations.getJSONObject(i).getString("timeStamp");
                location.cloudTag = locations.getJSONObject(i).getString("cloudTag");
                location.description = locations.getJSONObject(i).getString("description");
                location.imageURL = null;
                //location.locationImage = null;
                //location.imageValues = new HashMap<>();
                locationList.add(location);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return locationList;
    }

    private static String loadJsonFromAsset(String filename, Context context) {
        String json = null;

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }

}
