package algonquin.cst2335.finalproject.Trivia;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.ActivityScoreboardBinding;


public class ScoreboardActivity extends AppCompatActivity implements TriviaRecycleClick{

    private RecyclerView recyclerView;
    private static RecyclerView.Adapter myAdapter;
    private ScoreboardAdapter scoreboardAdapter;
    private ScoreboardViewModel viewModel;
    private FrameLayout fragmentLocation;
    private boolean isFrameLayoutVisible = false;
    ActivityScoreboardBinding binding;

    QuizResult quizResults;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        binding = ActivityScoreboardBinding.inflate(getLayoutInflater());

        viewModel = new ViewModelProvider(this, new ScoreboardViewModelFactory(getApplication())).get(ScoreboardViewModel.class);
//        viewModel = new ViewModelProvider(this, new ScoreboardViewModelFactory(getApplication(), this)).get(ScoreboardViewModel.class);

        recyclerView = findViewById(R.id.recyclerViewScoreboard);
        fragmentLocation = findViewById(R.id.fragmentLocation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        // Initialize the scoreboardAdapter
        scoreboardAdapter = new ScoreboardAdapter(new ArrayList<>(), this);
//        scoreboardAdapter = new ScoreboardAdapter((List<QuizResult>) quizResults, this);
        recyclerView.setAdapter(scoreboardAdapter);

        // Retrieve the saved quiz results from the database and display in the RecyclerView
//        List<QuizResult> quizResults = QuizResultRepository.getInstance(this).getAllQuizResults();
//        scoreboardAdapter = new ScoreboardAdapter(quizResults);
//        recyclerView.setAdapter(scoreboardAdapter);

        scoreboardAdapter.setOnItemClickListener(click -> {
            // Show details fragment here
            ScoreDetailsFragment detailsFragment = new ScoreDetailsFragment(click);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentLocation, detailsFragment)
                    .addToBackStack("")
                    .commit();
            //Toggle framelayout visibility.
            isFrameLayoutVisible = !isFrameLayoutVisible;
            updateFrameLayoutVisibility();

        });



        // Observe the LiveData on a background thread using ViewModelScope
        viewModel.getAllQuizResults().observe(this, quizResults -> {
            if (quizResults != null) {
//                scoreboardAdapter = new ScoreboardAdapter(quizResults, this);
                scoreboardAdapter.updateData(quizResults);
                recyclerView.setAdapter(scoreboardAdapter);
            }
        });

        fragmentLocation.setOnClickListener(click ->{
            fragmentLocation.setVisibility(View.GONE);
            Toast.makeText(this, "clicked on Fragment", Toast.LENGTH_LONG).show();
        });


    }
    private void updateFrameLayoutVisibility() {
        fragmentLocation = findViewById(R.id.fragmentLocation);
        fragmentLocation.setVisibility(isFrameLayoutVisible ? View.VISIBLE : View.GONE);
    }

    /**
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "quizResults.getId()", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param position
     */
    @Override
    public void onLongClick(int position) {
        Toast.makeText(this, "quizResults.getId()", Toast.LENGTH_SHORT).show();
    }

//    /**
//     * @param quizResult
//     */
//    @Override
//    public void onItemClick(QuizResult quizResult) {
//        scoreboardAdapter.setOnItemClickListener(click -> {
//            // Show details fragment here
//            ScoreDetailsFragment detailsFragment = new ScoreDetailsFragment(click);
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragmentLocation, detailsFragment)
//                    .addToBackStack("")
//                    .commit();
//            //Toggle framelayout visibility.
//            isFrameLayoutVisible = !isFrameLayoutVisible;
//            updateFrameLayoutVisibility();
//            Toast.makeText(this, "clicked recycleview item", Toast.LENGTH_LONG).show();
//        });
//    }

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