package algonquin.cst2335.finalproject.Trivia;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.ActivityTriviaQuestionBinding;

public class OptionsActivity extends AppCompatActivity {

    ActivityTriviaQuestionBinding binding;

    private RecyclerView questionsRecylerView;
    private QuestionsAdapter qtnAdapter;
    private TriviaActivity trivia;
//    private List<Question> qtnList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item_option);

    }
}
