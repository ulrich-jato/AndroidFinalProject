package algonquin.cst2335.finalproject.Trivia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalproject.AviationTracker;
import algonquin.cst2335.finalproject.BearImageGenerator;
import algonquin.cst2335.finalproject.CurrencyGenerator;
import algonquin.cst2335.finalproject.MainActivity;
import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.ActivityTriviaQuestionBinding;

/**
 * The TriviaActivity class represents the quiz-taking activity where users can answer trivia questions,
 * submit their answers, and view the quiz results.
 */
public class TriviaActivity extends AppCompatActivity {
    private ActivityTriviaQuestionBinding binding;
    private RecyclerView questionsRecyclerView;
    private List<Question> questionList;
    private RequestQueue queue;
    private QuestionsAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTriviaQuestionBinding.inflate(getLayoutInflater());
        queue = Volley.newRequestQueue(this);
        setContentView(binding.getRoot());

        questionsRecyclerView = binding.view;

        setSupportActionBar(binding.toolbar);

        SharedPreferences prefs = getSharedPreferences("TriviaDB", Context.MODE_PRIVATE);
        String iataCode =  prefs.getString("iataCode", "");

        Intent fromPrevious = getIntent();
        String choice = fromPrevious.getStringExtra("selectedCategory");
        int choiceValue;
        String size = fromPrevious.getStringExtra("questionSize");

        // Using switch to assign respective category numbers for the API to understand when fetching.
        switch(choice.toLowerCase()){
            case "history":
                choiceValue = 23;
                break;
            case "computers":
                choiceValue = 18;
                break;
            case "sports":
                choiceValue = 21;
                break;
            case "general knowledge":
                choiceValue = 9;
                break;
            default:
                choiceValue = 11;
                break;
        }

        binding.textView3.setText("Type: " + choice + "\t\t\t" + " \t\t qtns: " + size);

        String url = "https://opentdb.com/api.php?amount="+size+"&category="+choiceValue+"&type=multiple";

        // Fetch questions from the API using Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                (successfulResponse) ->{
                    try {
                        JSONArray results = successfulResponse.getJSONArray("results");
                        questionList = new ArrayList<>();

                        // Parse the JSON response and create Question objects
                        for(int i = 0; i < results.length(); i++){
                            JSONObject result = results.getJSONObject(i);
                            String questionText = result.getString("question");
                            String correctAnswer = result.getString("correct_answer");
                            JSONArray incorrectAnswers = result.getJSONArray("incorrect_answers");

                            List<String> options = new ArrayList<>();
                            options.add(correctAnswer);
                            for (int j = 0; j < incorrectAnswers.length(); j++) {
                                options.add(incorrectAnswers.getString(j));
                            }

                            Question question = new Question(questionText, options, correctAnswer);
                            questionList.add(question);
                        }

                        // Display questions using RecyclerView and QuestionsAdapter
                        if (questionList != null && !questionList.isEmpty()){
                            questionAdapter = new QuestionsAdapter(questionList);
                            questionsRecyclerView.setAdapter(questionAdapter);
                            questionAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                },
                (errorResponse)->{
                    Toast.makeText(this, "Failed to fetch questions", Toast.LENGTH_SHORT).show();
                });
        Volley.newRequestQueue(this).add(request);

        // Handle "Previous" button click
        binding.prev.setOnClickListener(click ->{
            finish();
            Toast.makeText(this, "You went to the previous page", Toast.LENGTH_LONG).show();
        });

        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Handle "Submit" button click
        binding.submit.setOnClickListener(click ->{
            int score = questionAdapter.getScore();
            promptForUsername(score);
        });
    }

    // Prompt the user to enter a username before submitting the quiz
    private void promptForUsername(int score) {
        final EditText input = new EditText(this);
        input.setHint("Enter your username");

        new AlertDialog.Builder(this)
                .setTitle("Enter Username")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String username = input.getText().toString().trim();
                    if (!username.isEmpty()) {
                        int count = questionList.size();
                        double percentage = ((double) score / questionList.size()) * 100;
                        Intent resultIntent = new Intent(TriviaActivity.this, QuizResultActivity.class);
                        resultIntent.putExtra("username", username);
                        resultIntent.putExtra("score", score);
                        resultIntent.putExtra("count", count);
                        startActivity(resultIntent);
                    } else {
                        Toast.makeText(this, "Please enter a valid username", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                })
                .show();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(TriviaActivity.this)
                    .setMessage(R.string.trivia_help)
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
}
