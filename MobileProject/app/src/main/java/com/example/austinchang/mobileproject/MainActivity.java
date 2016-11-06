package com.example.austinchang.mobileproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
//Use "option + enter" to do automatic imports on Mac!! Super cool

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    /* Happens exactly once in the application lifecycle */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//always call superclass
        setContentView(R.layout.activity_main);
    }

    /* Perform minimal work here. Only save things the user expects to be immediately available.
    Permanent storage is done in onStop. App is unpaused with onResume()*/
    @Override
    protected void onPause() {
        super.onPause();

    }

    /*The system retains memory of an activity. May not be necessary to implement onStart, onRestart, onStop
    When an activity is stopped, the system retains memory of it. I should let the system do everything
    and see if anything missing. onStart goes with onStop
     */

    /* Remove any threads or long running process not already removed. Always called after
    onPause and onStop unless finish() was called from onCreate. Must prevent memory leaks here. This method could
     be skipped in extreme cases.*/
    @Override
    protected void onDestroy() {
        super.onDestroy(); //always call superclass

    }
    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
