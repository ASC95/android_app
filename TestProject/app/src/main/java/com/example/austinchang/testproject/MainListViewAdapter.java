/**
 * Created by austinchang on 11/30/16. Thanks to https://www.raywenderlich.com/124438/android-listview-tutorial for the basic adapter
 * class. Thanks to http://stackoverflow.com/questions/25381435/unconditional-layout-inflation-from-view-adapter-should-use-view-holder-patter
 * for optimizing the getView() method.
 */

package com.example.austinchang.testproject;

import android.content.Context;
import android.support.v4.view.AsyncLayoutInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.cloudinary.Cloudinary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainListViewAdapter extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final ArrayList<UVALocation> mDataSource;
    private final Map config = new HashMap<>();

    public boolean shouldUpdate = false;

    public MainListViewAdapter(Context context, ArrayList<UVALocation> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        config.put("cloud_name", "dlw60s6pl");
        preLoadImages();
    }

    public void preLoadImages() {

    }

    /**
     * Tells ListView how many items to show.
     * @return the number of items in the data source
     */
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    /**
     * Accepts a position to place an object in the ListView
     * @param position
     * @return an object to be placed in the given position
     */
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    /**
     * Defines a unique ID for each item in the list
     * @param position the position of the item
     * @return the itemID for the list item. For simplicity, itemID = position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Using the ViewHolder pattern makes ListView scrolling more efficient because views are recycled.
     */
    static class ViewHolder {
        private TextView locationTitle;
        private TextView timeStamp;
        private NetworkImageView locationImage;
        //private ImageView locationImage;
    }

    /**
     * Creates the view to display within each list row.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return the view to display in the ListView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = null;
        UVALocation location = (UVALocation) getItem(position);

        //Toast.makeText(mContext, "Hello from getView", Toast.LENGTH_SHORT).show();

        if (convertView == null) {
            //location = (UVALocation) getItem(position);
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_view_row, parent, false);
            mViewHolder.timeStamp = (TextView) convertView.findViewById(R.id.timeStamp);
            mViewHolder.locationTitle = (TextView) convertView.findViewById(R.id.locationTitle);
            mViewHolder.locationImage = (NetworkImageView) convertView.findViewById(R.id.locationImage);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        if (location.jsonURL == null || shouldUpdate) {
            location.jsonURL = queryCloud(location);//this is a url to get JSON
        }
        executeVolleyRequest(location.jsonURL, location, mViewHolder);

        //mViewHolder.locationTitle.setText(location.locationTitle);

        return convertView;
    }

    /**
     * Contacts cloudinary to get most up to date image. Returns a url that will eventually get JSON.
     * @param location
     * @return a clean url which contains json
     */
    public String queryCloud(final UVALocation location) {
        final Cloudinary cloud = new Cloudinary(config);
        String cloudTag = location.cloudTag + ".json";
        String url = cloud.url().type("list").imageTag(cloudTag).replaceAll("<img src='", "");

        //Toast.makeText(mContext, "Queried the cloud", Toast.LENGTH_SHORT).show();

        String parsedURL = url.replaceAll("'/>", "");
        return parsedURL;
    }

    /**
     * Execute a volley request to get the image from the cloud. Volley automatically caches images that were downloaded so network usage
     * should be at a minimum.
     * @param jsonURL
     * @param location
     * @param mViewHolder
     */
    public void executeVolleyRequest(final String jsonURL, final UVALocation location, final ViewHolder mViewHolder) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, jsonURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Map<String, String> imageValues = getImageValues(response);

                if (imageValues.isEmpty()) {
                    /*Set dummy values*/
                } else {
                    location.timeStamp = parseUploadDate(imageValues.get("timeStamp"));
                    mViewHolder.timeStamp.setText(parseUploadDate(imageValues.get("timeStamp")));

                    mViewHolder.locationTitle.setText(location.locationTitle);

                    location.singlePicURL = imageValues.get("imageURL");
                    mViewHolder.locationImage.setImageUrl(location.singlePicURL,
                            VolleySingleton.getInstance(mContext).getImageLoader());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Make this load a dummy image or something...
                error.printStackTrace();
                System.out.println("error from Volley");
            }
        });
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsObjRequest);
    }

    public String parseUploadDate(String raw) {
        String[] parts = raw.split("T");
        return "Posted on " + parts[0] + " at " + parts[1].replace("Z", "");
    }

    /**
     * If we implement the upvote feature, I might need to change this to return two urls: a "newest"
     * and a "second newest". For now it just returns a map of one image's values
     *
     * @param input
     * @return the most recent image from a given location
     */
    public Map<String, String> getImageValues(JSONObject input) {
        final Cloudinary cloud = new Cloudinary(config);
        Map<String, String> imageValues = new HashMap<>();
        try {
            JSONArray ary = input.getJSONArray("resources");
            //JSONObject targetImage = ary.getJSONObject(ary.length() - 1);
            JSONObject targetImage = ary.getJSONObject(0);
            imageValues.put("imageURL", cloud.url().generate(targetImage.getString("public_id")));
            imageValues.put("timeStamp", targetImage.getString("created_at"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error from getImageValues");
        }
        return imageValues;
    }
}
