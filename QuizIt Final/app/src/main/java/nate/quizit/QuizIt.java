package nate.quizit;

import android.app.Application;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by adamguo on 9/19/16.
 */
public class QuizIt extends Application {

    public ArrayList<String> gettFData() {
        return tFData;
    }

    public void settFData(ArrayList<String> tFData) {
        this.tFData = tFData;
    }

    public ArrayList<String> getqFData() {
        return qFData;
    }

    public void setqFData(ArrayList<String> qFData) {
        this.qFData = qFData;
    }

    public ArrayList<Integer> gettFScores() {
        return tFScores;
    }

    public void settFScores(ArrayList<Integer> tFScores) {
        this.tFScores = tFScores;
    }

    private ArrayList<String> qFData = new ArrayList<String>(Arrays.asList("UVa Sports", "UVa History", "2016 Election"));
    private ArrayList<String> tFData = new ArrayList<String>();
    private ArrayList<Integer> tFScores = new ArrayList<Integer>();


}
