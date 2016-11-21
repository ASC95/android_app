package nate.quizit;

import java.util.ArrayList;

/**
 * Created by adamguo on 9/19/16.
 */
public class Quiz {
    private int quizId = 0;
    private ArrayList<Question> questionList;

    public Quiz() {
        questionList = new ArrayList<Question>();
        Question question1 = new Question("Which team won Super Bowl 50?", "Denver Broncos",
                "Carolina Panthers", "Denver Broncos", "UVa Sports", 0);
        Question question2 = new Question("Who is UVa football coach?", "Mike London",
                "Bronco Mendenhall", "Bronco Mendenhall", "UVa Sports", 1);
        Question question3 = new Question("Which year is UVa founded", "1814",
                "1819", "1819", "UVa History", 0);
        Question question4 = new Question("How many undergrads are there?", "12000",
                "15000", "15000", "UVa History", 1);
        Question question5 = new Question("Are you socially _____", "Liberal",
                "Conservative", "Liberal", "2016 Election", 0);
        Question question6 = new Question("Are you economically _____", "Liberal",
                "Conservative", "Liberal", "2016 Election", 1);
        questionList.add(question1);
        questionList.add(question2);
        questionList.add(question3);
        questionList.add(question4);
        questionList.add(question5);
        questionList.add(question6);
}

    public ArrayList<Question> getQuizQuestions(String quizName) {
        ArrayList<Question> quizQuestions = new ArrayList<Question>();
        for(Question q: questionList) {
            if (q.getQuizName().equals(quizName)) {
                quizQuestions.add(q);
            }
        }
        return quizQuestions;
    }

}
