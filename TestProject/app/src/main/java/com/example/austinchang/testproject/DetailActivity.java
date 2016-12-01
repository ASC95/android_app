package com.example.austinchang.testproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import org.w3c.dom.Text;

/**
 * Created by austinchang on 12/1/16.
 */

public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        String locationTitle = intent.getStringExtra("locationTitle");
        String imageURL = intent.getStringExtra("imageURL");
        String timeStamp = intent.getStringExtra("timeStamp");
        String description = intent.getStringExtra("description");
        TextView title = (TextView) findViewById(R.id.locationTitle);
        title.setText(locationTitle);
        NetworkImageView view = (NetworkImageView) findViewById(R.id.locationImage);
        view.setImageUrl(imageURL, VolleySingleton.getInstance(this).getImageLoader());
        TextView time = (TextView) findViewById(R.id.timeStamp);
        time.setText(timeStamp);
        TextView desc = (TextView) findViewById(R.id.description);
        desc.setText(description);

        /*
        mViewHolder.locationImage.setImageUrl(imageValues.get("imageURL"),
                VolleySingleton.getInstance(mContext).getImageLoader());*/
        /*
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_message);
        layout.addView(textView);
        */
    }
}
