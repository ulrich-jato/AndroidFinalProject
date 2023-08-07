package algonquin.cst2335.finalproject.Trivia;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.AviationTracker;
import algonquin.cst2335.finalproject.BearImageGenerator;
import algonquin.cst2335.finalproject.CurrencyGenerator;
import algonquin.cst2335.finalproject.MainActivity;
import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.ActivityScoreboardBinding;


public class ScoreboardActivity extends AppCompatActivity implements TriviaRecycleClick {

    private RecyclerView recyclerView;
    private static RecyclerView.Adapter myAdapter;
    private ScoreboardAdapter scoreboardAdapter;
    private ScoreboardViewModel viewModel;
    private FrameLayout fragmentLocation;
    private boolean isFrameLayoutVisible = false;
    ActivityScoreboardBinding binding;

    private ExecutorService executor = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        viewModel = new ViewModelProvider(this, new ScoreboardViewModelFactory(getApplication())).get(ScoreboardViewModel.class);
        recyclerView = findViewById(R.id.recyclerViewScoreboard);
        fragmentLocation = findViewById(R.id.fragmentLocation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Initialize the scoreboardAdapter
        scoreboardAdapter = new ScoreboardAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(scoreboardAdapter);

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

        fragmentLocation.setOnClickListener(click -> {
            fragmentLocation.setVisibility(View.GONE);
        });

        binding.goHome.setOnClickListener(click ->{
            Intent intent = new Intent(this, MainTriviaActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_bear_home) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (item.getItemId() == R.id.menu_bear_help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ScoreboardActivity.this)
                    .setMessage(R.string.score_help)
                    .setTitle("Instructions!")
                    .setPositiveButton("OK", (cl, which) -> {
                    });
            builder.create().show();
        } else if (item.getItemId() == R.id.menu_bear_aviation) {
            startActivity(new Intent(this, AviationTracker.class));
        } else if (item.getItemId() == R.id.menu_bear_currency) {
            startActivity(new Intent(this, CurrencyGenerator.class));
        } else if (item.getItemId() == R.id.menu_bear_trivia) {
            startActivity(new Intent(this, BearImageGenerator.class));
        }
        return true;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Quiz Result")
                .setMessage("Are you sure you want to delete this quiz result?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Delete the quiz result from the database
                    QuizResult quizResultToDelete = scoreboardAdapter.getItem(position);

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
                                executor.execute(() -> {
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

        public void deleteQuizResult(QuizResult quizResult) {
            repository.deleteQuizResult(quizResult);
        }

        public void insertQuizResult(QuizResult quizResult) {
            repository.insertQuizResult(quizResult);
        }

    }
}