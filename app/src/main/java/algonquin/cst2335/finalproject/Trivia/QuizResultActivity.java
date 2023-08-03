package algonquin.cst2335.finalproject.Trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import algonquin.cst2335.finalproject.R;

public class QuizResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        TextView usernameText = findViewById(R.id.usernameText);
        TextView scoreText = findViewById(R.id.scoreText);
        TextView percentage = findViewById(R.id.overall);

        // Retrieve the username and score from the intent
        Intent fromPrevious = getIntent();
        String username = fromPrevious.getStringExtra("username");
        int score = fromPrevious.getIntExtra("score", 0);
        int count = fromPrevious.getIntExtra("count", 0);
        double overall = ((double) score / count)*100;

        //Format the percentage output to 2 decimal places.
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String formattedPercentage = decimalFormat.format(overall);



        // Display the username and score
        usernameText.setText("Username: " + username);
        scoreText.setText("Score: " + score + "/" + count);
        percentage.setText("Overall: " + formattedPercentage +"%");


        // Insert the quiz result into the Room database
//        QuizResult quizResult = new QuizResult(username, score, overall);
//        QuizResultDatabase db = Room.databaseBuilder(getApplicationContext(),
//                        QuizResultDatabase.class, "quiz_results_db")
//                .allowMainThreadQueries() // For demonstration purposes, not recommended for production
//                .build();
//        db.quizResultDao().insertQuizResult(quizResult);


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
}
