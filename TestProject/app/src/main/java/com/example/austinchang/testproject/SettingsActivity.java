package com.example.austinchang.testproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView welcomeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.save_button).setOnClickListener(this);
        findViewById(R.id.cancel_button).setOnClickListener(this);

        welcomeUser = (TextView)findViewById(R.id.textView);
        SharedPreferences settings = getSharedPreferences("myFile", MODE_PRIVATE);
        String name = settings.getString("username", "DefaultName");
        welcomeUser.setText("Welcome back, "+name+"!");




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_button:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.cancel_button:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                break;


            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences("myFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        String old_Name = settings.getString("username","");

        EditText editText = (EditText) findViewById(R.id.editText);
//        Toast.makeText(SettingsActivity.this, "["+editText.getText().toString()+"]",
//                Toast.LENGTH_LONG).show();
        if(editText.getText().toString()== "") {
            editor.putString("username", old_Name.toString());
            // Commit the edits!
            editor.commit();
        }
        else{
            editor.putString("username", editText.getText().toString());
            // Commit the edits!
            editor.commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
