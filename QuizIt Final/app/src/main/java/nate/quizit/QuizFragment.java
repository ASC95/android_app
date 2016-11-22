package nate.quizit;

/**
 * Created by N on 9/18/2016.
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;import java.util.ArrayList;
import java.util.Arrays;

public class QuizFragment extends Fragment {
    OnQuizListener mCallback;

    public interface OnQuizListener {
        public void OnQuizResult(String data);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mCallback = (OnQuizListener)activity;
    }

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

//    public String [] data;
    public ArrayList<String> data = new ArrayList<String>();


    public void setData(ArrayList<String> inData){
        data = inData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);

        //String [] arr = data.toArray(new String[data.size()]);
        MyAdapter adapter = new MyAdapter(data);
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

//        mCallback.OnQuizResult();

        return rootView;
    }

}