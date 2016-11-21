package nate.quizit;

/**
 * Created by adamguo on 9/19/16.
 */
public class Question {
    private String question;
    private String optionA;
    private String optionB;
    private String answer;
    private String quizName;
    private int questionId;

    public Question() {
        question = "";
        optionA = "";
        optionB = "";
        answer = "";
        quizName = "";
        questionId = 0;
    }

    public Question(String Question,String OptionA,String OptionB,
                    String Answer, String QuizName, int QuestionId) {
        question = Question;
        optionA = OptionA;
        optionB = OptionB;
        answer = Answer;
        quizName = QuizName;
        questionId = QuestionId;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuizName() {
        return quizName;
    }

    public int getQuestionId() {
        return questionId;
    }
}
