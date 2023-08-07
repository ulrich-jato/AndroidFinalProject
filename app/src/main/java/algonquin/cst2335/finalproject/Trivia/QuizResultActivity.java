package algonquin.cst2335.finalproject.Trivia;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.text.DecimalFormat;

import algonquin.cst2335.finalproject.AviationTracker;
import algonquin.cst2335.finalproject.BearImageGenerator;
import algonquin.cst2335.finalproject.CurrencyGenerator;
import algonquin.cst2335.finalproject.MainActivity;
import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.ActivityQuizResultBinding;


/**
 * The QuizResultActivity class displays the user's quiz result and provides options to save the result to the database
 * and view the scoreboard.
 */
public class QuizResultActivity extends AppCompatActivity {

    ActivityQuizResultBinding binding;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        TextView usernameText = findViewById(R.id.usernameText);
        TextView scoreText = findViewById(R.id.scoreText);
        TextView percentage = findViewById(R.id.overall);

        // Retrieve the username and score from the intent
        Intent fromPrevious = getIntent();
        String username = fromPrevious.getStringExtra("username");
        int score = fromPrevious.getIntExtra("score", 0);
        count = fromPrevious.getIntExtra("count", 0);
        double overall = ((double) score / count)*100;

        //Format the percentage output to 2 decimal places.
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String formattedPercentage = decimalFormat.format(overall);



        // Display the username and score
        usernameText.setText("Username: " + username);
        scoreText.setText("Score: " + score + "/" + count);
        percentage.setText("Overall: " + formattedPercentage +"%");

        Button saveBtn = findViewById(R.id.save);
        Button scoreBoard = findViewById(R.id.scoreboard);

        // Save the result to the database
        saveBtn.setOnClickListener(view -> {
            QuizResult quizResult = new QuizResult(username, score, overall);
            QuizResultDatabase db = Room.databaseBuilder(getApplicationContext(),
                            QuizResultDatabase.class, "quiz_results_db")
                    .allowMainThreadQueries() // For demonstration purposes, not recommended for production
                    .build();
            db.quizResultDao().insertQuizResult(quizResult);
            Toast.makeText(this, "Result saved to database", Toast.LENGTH_SHORT).show();
        });

        // Go to the ScoreboardActivity
        scoreBoard.setOnClickListener(view -> {
            Intent scoreboardIntent = new Intent(QuizResultActivity.this, ScoreboardActivity.class);
            startActivity(scoreboardIntent);
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.menu_bear_home ) {
            startActivity(new Intent(this, MainActivity.class));
        } else if( item.getItemId() == R.id.menu_bear_help ){
            AlertDialog.Builder builder = new AlertDialog.Builder(QuizResultActivity.this)
                    .setMessage(R.string.quizResult_help)
                    .setTitle("Instructions!")
                    .setPositiveButton("OK", (cl, which) -> {
                    });
            builder.create().show();
        } else if (item.getItemId() == R.id.menu_bear_aviation) {
            startActivity(new Intent(this, AviationTracker.class));
        }else if (item.getItemId() == R.id.menu_bear_currency) {
            startActivity(new Intent(this, CurrencyGenerator.class));
        }else if (item.getItemId() == R.id.menu_bear_trivia) {
            startActivity(new Intent(this, BearImageGenerator.class));
        }
        return true;
    }
}
