package algonquin.cst2335.finalproject.Trivia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.finalproject.R;

/**
 * The MainTriviaActivity class represents the main activity for the trivia application.
 * This activity allows users to start the trivia quiz by clicking the "Go" button.
 */
public class MainTriviaActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created. This method initializes the activity's UI components,
     * sets up event listeners, and handles the "Go" button click to start the trivia quiz.
     *
     * @param savedInstanceState A Bundle containing the saved instance state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_mainpage);

        // Initialize the "Go" button and set its click listener
        Button goBtn = findViewById(R.id.goBtn);
        goBtn.setOnClickListener(click ->{
            // Start the OptionsActivity when the "Go" button is clicked
            startActivity(new Intent(this, OptionsActivity.class));
        });
    }
}
