package nate.quizit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by adamguo on 9/19/16.
 */
public class QuizActivity extends Activity {

    int score = 0;
    int qId = 0;
    TextView question;
    RadioButton rb1, rb2;
    RadioGroup rg1;
    Button next;
    Question currentQ;
    ArrayList<Question> questionsList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        final Intent intent = getIntent();
        Bundle b = intent.getExtras();
        final String quizName = (String)b.get("quizId");
        //int quizId = Integer.parseInt(q.replaceAll("[\\D]",""));
        Quiz quiz = new Quiz();
        questionsList = quiz.getQuizQuestions(quizName);
        currentQ = questionsList.get(qId);
        question=(TextView)findViewById(R.id.textView);
        rb1=(RadioButton)findViewById(R.id.radioButton);
        rb2=(RadioButton)findViewById(R.id.radioButton2);
        next = (Button)findViewById(R.id.button);
        setQuestion();
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("This", Integer.toString(qId));
                RadioGroup grp=(RadioGroup)findViewById(R.id.radioGroup);
                RadioButton answer=(RadioButton)findViewById(grp.getCheckedRadioButtonId());

                if(currentQ.getAnswer().equals(answer.getText())) {
                    score++;
                }
                if(qId < 2) {
                    currentQ = questionsList.get(qId);
                    setQuestion();
                }
                else {
                    Intent intent1 = new Intent(QuizActivity.this, ResultActivity.class);
                    intent1.putExtra("score",score);
                    intent1.putExtra("name",quizName);
                    startActivity(intent1);
                }
            }
        });

    }

    private void setQuestion() {
        question.setText(currentQ.getQuestion());
        rb1.setText(currentQ.getOptionA());
        rb2.setText(currentQ.getOptionB());
        qId++;
    }


}
