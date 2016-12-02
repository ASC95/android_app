package com.example.austinchang.testproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import org.w3c.dom.Text;

/**
 * Created by austinchang on 12/1/16.
 */

public class DetailActivity extends Activity {

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Getting and setting theme
        settings = getSharedPreferences("myFile", MODE_PRIVATE);
        String theme = settings.getString("theme","BlueTheme");

        if(theme.equals("OrangeTheme")){
            setTheme(R.style.OrangeTheme);
        }
        else if (theme.equals("GoldTheme")){
            setTheme(R.style.GoldTheme);
        }
        else{
            setTheme(R.style.BlueTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();

        String locationTitle = intent.getStringExtra("locationTitle");
        TextView title = (TextView) findViewById(R.id.locationTitle);
        title.setText(locationTitle);

        String singlePicURL = intent.getStringExtra("singlePicURL");
        NetworkImageView view = (NetworkImageView) findViewById(R.id.locationImage);
        view.setImageUrl(singlePicURL, VolleySingleton.getInstance(this).getImageLoader());

        String timeStamp = intent.getStringExtra("timeStamp");
        TextView time = (TextView) findViewById(R.id.timeStamp);
        time.setText(timeStamp);

        String description = intent.getStringExtra("description");
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

    @Override
    protected void onResume() {
        //Getting and setting theme
        settings = getSharedPreferences("myFile", MODE_PRIVATE);
        String theme = settings.getString("theme", "BlueTheme");

        if (theme.equals("OrangeTheme")) {
            setTheme(R.style.OrangeTheme);
            changeTheme(R.style.OrangeTheme);
        } else if (theme.equals("GoldTheme")) {
            setTheme(R.style.GoldTheme);
            changeTheme(R.style.GoldTheme);
        } else {
            setTheme(R.style.BlueTheme);
            changeTheme(R.style.BlueTheme);
        }
        super.onResume();


    }

    public void changeTheme(int newTheme) {
        setTheme(newTheme);
        //recreate();
    }
}
