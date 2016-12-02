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
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private int theme;
    private String old_Name;

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
        setContentView(R.layout.activity_settings);

        findViewById(R.id.save_button).setOnClickListener(this);
        findViewById(R.id.cancel_button).setOnClickListener(this);
        findViewById(R.id.OrangeTheme).setOnClickListener(this);
        findViewById(R.id.BlueTheme).setOnClickListener(this);
        findViewById(R.id.GoldTheme).setOnClickListener(this);

        welcomeUser = (TextView)findViewById(R.id.textView);
        SharedPreferences settings = getSharedPreferences("myFile", MODE_PRIVATE);
        String name = settings.getString("username", "DefaultName");








    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_button:
                //changed since last push
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.cancel_button:
                settings = getSharedPreferences("myFile", MODE_PRIVATE);
                editor = settings.edit();
                editor.putString("username",old_Name);
                editor.commit();

                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                break;

            case R.id.OrangeTheme:
                settings = getSharedPreferences("myFile", MODE_PRIVATE);
                editor = settings.edit();
                editor.putString("theme","OrangeTheme");
                editor.commit();

                changeTheme(R.style.OrangeTheme);
                break;

            case R.id.BlueTheme:
                settings = getSharedPreferences("myFile", MODE_PRIVATE);
                editor = settings.edit();
                editor.putString("theme","BlueTheme");
                editor.commit();

                changeTheme(R.style.BlueTheme);
                break;

            case R.id.GoldTheme:
                settings = getSharedPreferences("myFile", MODE_PRIVATE);
                editor = settings.edit();
                editor.putString("theme","GoldTheme");
                editor.commit();

                changeTheme(R.style.GoldTheme);
                break;

            default:
                break;
        }
    }


    public void changeTheme(int newTheme) {
        setTheme(newTheme);
        recreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        settings = getSharedPreferences("myFile", MODE_PRIVATE);
        editor = settings.edit();
        String name = settings.getString("username", "DefaultName");
        welcomeUser.setText("Welcome back, "+name+"!");

    }

    @Override
    protected void onPause() {
        super.onPause();
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        settings = getSharedPreferences("myFile", MODE_PRIVATE);
        editor = settings.edit();
        old_Name = settings.getString("username","DefaultUser");
        EditText editText = (EditText) findViewById(R.id.editText);

        if(editText.getText().toString().equals("")) {
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
