package com.example.austinchang.testproject;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        /*
        //Intent intent = getIntent();
        //String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText("hello");

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_settings);
        layout.addView(textView);
        */
    }

    @Override
    protected void onPause() {
        super.onPause();
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        EditText editText = (EditText) findViewById(R.id.editText);
        editor.putString("username", editText.getText().toString());
        // Commit the edits!
        editor.commit();
    }
}
