package com.example.austinchang.testproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.settingsButton).setOnClickListener(this);

        TextView textView = (TextView) findViewById(R.id.usernameDisplay);
        textView.setText("On Create was just called");

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Restore preferences from the shared preferences file. It is getting called!
     */
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        String name = settings.getString("username", "defaultName");
        TextView textView = (TextView) findViewById(R.id.usernameDisplay);
        textView.setText(name);
    }

    /**
     * This changes the text, but the text goes back to the hint when I return. Why is that?
     */
    @Override
    protected void onPause() {
        super.onPause();

        TextView textView = (TextView) findViewById(R.id.usernameDisplay);
        textView.setText("On Pause was just called");
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

