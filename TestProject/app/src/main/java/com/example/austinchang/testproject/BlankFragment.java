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

import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment implements View.OnClickListener{
    /*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private Integer images[] = {R.drawable.pizza, R.drawable.cows_350};
    */
    private int currImage = 0;
    private String imageUrls[] = {"http://res.cloudinary.com/dlw60s6pl/image/upload/v1478488799/A10_wuvlc8.jpg",
            "http://res.cloudinary.com/dlw60s6pl/image/upload/v1478488795/titan_tgjxtb.jpg",
            "http://res.cloudinary.com/dlw60s6pl/image/upload/v1478413259/sample.jpg"
    };

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        /*
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        */
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        */
    }

    /**
     * Called each time the fragment is drawn. Inflates the fragment's view. Calls setCurrentImage()
     * to retrieve the image from the Cloudinary hosting service. Attaches an onClickListener to
     * the entire fragment.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the fragment's view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.fragment_image);
        setCurrentImage(imageView);
        view.setOnClickListener(this);
        return view;
    }

    /**
     * This is onClickListener for the entire fragment. Upon a click, it updates the index variable
     * then calls setCurrentImage(). Since the index variable was changed, setCurrentImage() loads
     * a new image from a new url stored in the array.
     * @param view
     */
    @Override
    public void onClick(View view) {
        currImage++;//change the index of the array which stored the image urls
        if (currImage == 3) {
            currImage = 0;
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.fragment_image);
        setCurrentImage(imageView);
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
        /*
        mListener = null;
        */
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
     * Creates an ImageDownloader object to retrieve the image from the url.
     * @param imageView
     */
    private void setCurrentImage(ImageView imageView) {
        ImageDownloader imageDownLoader = new ImageDownloader(imageView);
        imageDownLoader.execute(imageUrls[currImage]);
    }
}
