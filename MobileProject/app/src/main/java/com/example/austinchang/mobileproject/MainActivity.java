package com.example.austinchang.mobileproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    /* Happens exactly once in the application lifecycle */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.settingsButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Restore preferences from the shared preferences file
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Restore preferences
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        String name = settings.getString("username", "defaultName");
        TextView textView = (TextView) findViewById(R.id.usernameDisplay);
        textView.setText(name);
    }

    /* Called when the user clicks the Send button
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    */
}
