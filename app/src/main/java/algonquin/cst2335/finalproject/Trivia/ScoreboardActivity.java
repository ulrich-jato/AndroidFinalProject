package algonquin.cst2335.finalproject.Trivia;

import android.app.Application;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import algonquin.cst2335.finalproject.R;


public class ScoreboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static RecyclerView.Adapter myAdapter;
    private ScoreboardAdapter scoreboardAdapter;
    private ScoreboardViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        viewModel = new ViewModelProvider(this, new ScoreboardViewModelFactory(getApplication())).get(ScoreboardViewModel.class);
//        viewModel = new ViewModelProvider(this, new ScoreboardViewModelFactory(getApplication(), this)).get(ScoreboardViewModel.class);


        recyclerView = findViewById(R.id.recyclerViewScoreboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the saved quiz results from the database and display in the RecyclerView
//        List<QuizResult> quizResults = QuizResultRepository.getInstance(this).getAllQuizResults();
//        scoreboardAdapter = new ScoreboardAdapter(quizResults);
//        recyclerView.setAdapter(scoreboardAdapter);

        // Observe the LiveData on a background thread using ViewModelScope
        viewModel.getAllQuizResults().observe(this, quizResults -> {
            if (quizResults != null) {
                scoreboardAdapter = new ScoreboardAdapter(quizResults);
                recyclerView.setAdapter(scoreboardAdapter);
            }
        });


    }

    static class ScoreboardViewModel extends ViewModel {
        private QuizResultRepository repository;
        private LiveData<List<QuizResult>> allQuizResults;

        public ScoreboardViewModel(Application application) {
            super(); // Call the ViewModel constructor
            repository = new QuizResultRepository(application);
            allQuizResults = repository.getAllQuizResultsLiveData();

        }

        public LiveData<List<QuizResult>> getAllQuizResults() {
            return allQuizResults;
        }
    }


//    static class ScoreboardViewModel extends RecyclerView.ViewHolder {
//        private QuizResultRepository repository;
//        private LiveData<List<QuizResult>> allQuizResults;
//
//        public ScoreboardViewModel(View itemView) {
//            super(itemView);
//            repository = new QuizResultRepository(itemView.getContext());
//            allQuizResults = repository.getAllQuizResultsLiveData();
//
//
//            itemView.setOnClickListener(click -> {
//                int position = getAbsoluteAdapterPosition();
//                RecyclerView.ViewHolder newRow = myAdapter.onCreateViewHolder(null, myAdapter.getItemViewType(position));
//
//
//            });
//        }
//
//        public LiveData<List<QuizResult>> getAllQuizResults() {
//            return allQuizResults;
////        }
//        }
//    }
}