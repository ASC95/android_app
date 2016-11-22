package nate.quizit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by adamguo on 9/19/16.
 */
public class ResultActivity extends Activity {

    int score;
    String qPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        RatingBar bar = (RatingBar)findViewById(R.id.ratingBar);
        TextView text = (TextView)findViewById(R.id.textView2);
        TextView text2 = (TextView)findViewById(R.id.textView3);
        final Intent intent = getIntent();
        Bundle b = intent.getExtras();
        score = b.getInt("score");
        qPos = b.getString("name");
        text.setText(qPos);
        bar.setRating(score);
        switch (score) {
            case 1: text2.setText("You suck, sorry.");
                break;
            case 2: text2.setText("Okay.");
                break;
            case 3: text2.setText("Attaboy!");
                break;
        }
        Button button = (Button)findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ResultActivity.this, MainActivity.class);
                intent2.putExtra("score", score);
                intent2.putExtra("position", qPos);
                startActivity(intent2);
            }
        });
    }
}
