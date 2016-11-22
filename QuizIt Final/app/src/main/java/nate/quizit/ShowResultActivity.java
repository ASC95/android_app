package nate.quizit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowResultActivity extends AppCompatActivity {

    int score;
    String qPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showresult);
        RatingBar bar = (RatingBar)findViewById(R.id.ratingBar2);
        TextView text = (TextView)findViewById(R.id.textView4);
        TextView text2 = (TextView)findViewById(R.id.textView5);
        final Intent intent = getIntent();
        Bundle b = intent.getExtras();
        qPos = b.getString("name");


        ArrayList<String> tfData = ((QuizIt) this.getApplication()).gettFData();


        if(((QuizIt) this.getApplication()).gettFScores().size() != 0) {
            score = ((QuizIt) this.getApplication()).gettFScores().get(tfData.indexOf(qPos));
        }

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
        Button button = (Button)findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ShowResultActivity.this, MainActivity.class);
                startActivity(intent2);
            }
        });
    }
}
