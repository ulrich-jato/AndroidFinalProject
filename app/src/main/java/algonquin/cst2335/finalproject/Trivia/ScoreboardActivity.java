package algonquin.cst2335.finalproject.Trivia;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private ExecutorService executor = Executors.newSingleThreadExecutor();



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
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Delete result")
//                .setMessage("Are you sure to delete this result from the scoreboard?")
//                .setPositiveButton("Delete", (dialog, which) -> {
//                    QuizResult quizResultToDelete = scoreboardAdapter.getItem(position);
//
//                    // Save the deleted item temporarily for undo
//                    QuizResult deletedQuizResult = quizResultToDelete;
//                    viewModel.deleteQuizResult(quizResultToDelete);
//                    scoreboardAdapter.removeItem(position);
//                    Snackbar snackbar = Snackbar.make(recyclerView, "Quiz result deleted", Snackbar.LENGTH_LONG);
//                    snackbar.setAction("Undo", v -> {
//                        // Restore the deleted item
//                        viewModel.insertQuizResult(deletedQuizResult);
//                        scoreboardAdapter.addItem(position, deletedQuizResult);
//                    });
//                    snackbar.show();
//                })
//                .setNegativeButton("cancel", (dialog, which)-> dialog.dismiss())
//                .show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Quiz Result")
                .setMessage("Are you sure you want to delete this quiz result?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Delete the quiz result from the database
                    QuizResult quizResultToDelete = scoreboardAdapter.getItem(position);

                    // Generate a new ID for the re-inserted quiz result
//                    QuizResult newQuizResult = new QuizResult(
//                            quizResultToDelete.getUsername(),
//                            generateUniqueId(),
//
//                            quizResultToDelete.getPercentage()
//                    );

                    // Insert the deleted item back to the database using ExecutorService
                    executor.execute(() -> {
                        viewModel.deleteQuizResult(quizResultToDelete);

                        // Remove the deleted item from the adapter
                        runOnUiThread(() -> {
                            scoreboardAdapter.removeItem(position);
//                            myAdapter.notifyItemRemoved(position);

                            // Show Snackbar with undo option
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                                    "Quiz result deleted", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Undo", view -> {
                                // Re-insert the deleted item into the database and adapter
                                executor.execute(() ->{
                                    viewModel.insertQuizResult(quizResultToDelete);

                                });
                                scoreboardAdapter.addItem(position, quizResultToDelete);
//                                myAdapter.notifyItemInserted(position);

                            });
                            snackbar.show();
                        });
                    });
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();

    }

    private int generateUniqueId() {
        // Generate a unique ID using your preferred method
        // For example, you can use a combination of timestamp and a random number
        long timestamp = System.currentTimeMillis();
        int random = new Random().nextInt(1000); // Adjust the range as needed
        return (int) (timestamp + random);
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
        public void deleteQuizResult(QuizResult quizResult){
            repository.deleteQuizResult(quizResult);
        }

        public void insertQuizResult(QuizResult quizResult) {
            repository.insertQuizResult(quizResult);
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