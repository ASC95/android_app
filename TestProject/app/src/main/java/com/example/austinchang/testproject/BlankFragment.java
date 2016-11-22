package com.example.austinchang.testproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.Cloudinary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment implements View.OnClickListener {

    Map config = new HashMap<>();

    /**
     * Connects the mobile device to our cloud account, given our cloud_name. This has nothing to do with security so storing this
     * information is fine.
     */
    public BlankFragment() {
        config.put("cloud_name", "dlw60s6pl");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called each time the fragment is drawn. Inflates the fragment's view. Gives the fragment an onClickListener which
     * allows the image of the fragment to change when clicked on. Note that when the image is clicked, it only pulls the most recently
     * uploaded picture from the cloud to the device. This means that if you click on it, the picture will only change one time
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the fragment's view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        view.setOnClickListener(this);
        return view;
    }

    /**
     * This method uses the android Volley library to submit a GET request to the cloud server. The cloud server returns a json array
     * that contains all of the images that have the given "imageTag". Upon "onResponse" completion, this method passes the json
     * to the parseJSON array.
     * @param view
     */
    @Override
    public void onClick(View view) {
        final ImageView imageView = (ImageView) view.findViewById(R.id.fragment_image);
        final Cloudinary cloud = new Cloudinary(config);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = cloud.url().type("list").imageTag("Amphitheater.json").replaceAll("<img src='", "");
        String parsedURL = url.replaceAll("'/>", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, parsedURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJSON(response, cloud, imageView);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("It's broken");
            }
        });
        queue.add(stringRequest);
    }

    /**
     * Accepts a string that is actually a json array, a Cloudinary object, and an ImageView that corresponds
     * to the ImageView of this fragment. The method parses the json to find the image with the most recent upload date.
     * Once it finds the image with the latest date, it re-scans the json to find the "public_id" of the image which is then
     * used to build a url to access the image. Once the url of the most recent image is obtained, it is passed to the "setCurrentImage"
     * method.
     * @param input
     * @param cloud
     * @param view
     */
    private void parseJSON(String input, Cloudinary cloud, ImageView view) {
        try {
            JSONObject str = new JSONObject(input);
            JSONArray ary = str.getJSONArray("resources");
            List<String> dates = new ArrayList<>();
            String targetImage = "";
            for (int i = 0; i < ary.length(); i++) {
                JSONObject innerObj = ary.getJSONObject(i);
                String date = innerObj.getString("created_at");
                dates.add(date);
            }
            Collections.sort(dates);
            String keyDate = dates.get(dates.size() - 1);
            for (int i = 0; i < ary.length(); i++) {
                JSONObject innerObj = ary.getJSONObject(i);
                String date = innerObj.getString("created_at");
                if (date.equals(keyDate)) {
                    targetImage = innerObj.getString("public_id");
                    break;
                }
            }
            setCurrentImage(view, cloud.url().generate(targetImage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */

    /**
     * This class creates an object that get its own an ImageView field from a url.
     * see https://developer.android.com/training/displaying-bitmaps/index.html for bitmaps.
     * An AsyncTask is useful for very short threads.
     */
    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public ImageDownloader(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("MyApp", e.getMessage());
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    /**
     * Accepts this fragment's imageView and a url to build the image. Creates an ImageDownloader object
     * that acquires the image from the cloud server.
     * @param imageView
     * @param url
     */
    private void setCurrentImage(ImageView imageView, String url) {
        ImageDownloader imageDownLoader = new ImageDownloader(imageView);
        imageDownLoader.execute(url);
    }
}
