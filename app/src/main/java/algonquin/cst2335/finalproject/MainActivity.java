package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import algonquin.cst2335.finalproject.Trivia.MainTriviaActivity;
import algonquin.cst2335.finalproject.Trivia.TriviaActivity;
import algonquin.cst2335.finalproject.databinding.ActivityMainBinding;

/**
 * The MainActivity class represents the main activity of the application.
 * It displays a menu with buttons to navigate to different features of the application.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is starting. This is where most initialization
     * should go: calling setContentView(int) to inflate the activity's UI,
     * using findViewById(int) to programmatically interact with widgets in the UI,
     * and binding any data to the UI.
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using View Binding
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set click listeners for the buttons

        // Button for Aviation Tracker
        binding.aviation.setOnClickListener(click -> {
            startActivity(new Intent(this, AviationTracker.class));
        });

        // Button for Currency Generator
        binding.currency.setOnClickListener(click -> {
            startActivity(new Intent(this, CurrencyGenerator.class));
        });

        // Button for Trivia Question
        binding.trivia.setOnClickListener(click -> {
            startActivity(new Intent(this, MainTriviaActivity.class));
        });

        // Button for Bear Image Generator
        binding.bearImage.setOnClickListener(click -> {
            startActivity(new Intent(this, BearImageGenerator.class));
        });
    }
}
