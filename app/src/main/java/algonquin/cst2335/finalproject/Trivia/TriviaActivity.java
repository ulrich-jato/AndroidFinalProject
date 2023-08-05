package algonquin.cst2335.finalproject.Trivia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

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

import algonquin.cst2335.finalproject.databinding.ActivityTriviaQuestionBinding;

public class TriviaActivity extends AppCompatActivity {
    ActivityTriviaQuestionBinding binding;
    ActivityTriviaQuestionBinding binding2;
    private RecyclerView questionsRecylerView;
    private List<Question> qtnList;
    RequestQueue queue = null;
    private QuestionsAdapter qtnAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTriviaQuestionBinding.inflate(getLayoutInflater());
        queue = Volley.newRequestQueue(this);
        setContentView(binding.getRoot());

        questionsRecylerView = binding.view;

        SharedPreferences prefs = getSharedPreferences("TriviaDB", Context.MODE_PRIVATE);
        String iataCode =  prefs.getString("iataCode", "");
        if (!iataCode.equals("")){
            binding.editText.setText(iataCode);
        }
        Intent fromPrevious = getIntent();
        String choice = fromPrevious.getStringExtra("selectedCategory");
        int choiceValue;
        String size = fromPrevious.getStringExtra("questionSize");

        //using switch to assign respective category numbers for the API to understand when fetching.
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

        //creating a string reference for the API url concatinated with the question size and category value.
        String url = "https://opentdb.com/api.php?amount="+size+"&category="+choiceValue+"&type=multiple";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (successfulResponse) ->{
                int it = 0;
                        try {
                            JSONArray results = successfulResponse.getJSONArray("results");
                            qtnList = new ArrayList<>();

                            for(int i = 0; i < results.length(); i++){
                                JSONObject result = results.getJSONObject(i);
                                String category = result.getString("category");
                                String type = result.getString("type");
                                String difficulty = result.getString("difficulty");
                                String questionText = result.getString("question");
                                String correctAnswer = result.getString("correct_answer");
                                JSONArray incorrectAnswers = result.getJSONArray("incorrect_answers");

                                List<String> options = new ArrayList<>();
                                options.add(correctAnswer);
                                for (int j = 0; j < incorrectAnswers.length(); j++) {
                                    options.add(incorrectAnswers.getString(j));
                                }

                                Question question = new Question(questionText, options, correctAnswer);
                                qtnList.add(question);
//
                                if (qtnList != null && !qtnList.isEmpty()){
                                    qtnAdapter = new QuestionsAdapter(qtnList);
                                    questionsRecylerView.setAdapter(qtnAdapter);
                                    qtnAdapter.notifyDataSetChanged();
                                }

                            }

                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    },
                    (errorResponse)->{
                        int j = 0;
                        Toast.makeText(this, "Failed to fetch questions", Toast.LENGTH_SHORT).show();
                    });
            // Add the request to the Volley queue
            Volley.newRequestQueue(this).add(request);

            //Return to previous page when user clicks on "previous" button.
        binding.prev.setOnClickListener(click ->{
            //calling the finish function to close the current activity and return to previous page/activity.
            finish();
            Toast.makeText(this,"You went to previous page", Toast.LENGTH_LONG).show();

        });

        questionsRecylerView.setLayoutManager(new LinearLayoutManager(this));

        binding.submit.setOnClickListener(click ->{
            int score = qtnAdapter.getScore();
            //Toast.makeText(this,"Your score is: "+ score + " / "+ qtnList.size(), Toast.LENGTH_LONG).show();

            // Get the user's name (you need to replace "username" with the actual way you get the username)
            String username = "username";
            int count = qtnList.size();
            double percentage = ((double) score / qtnList.size())*100;
            // Format the percentage to two decimal places

            // Launch the QuizResultActivity
//            Intent resultIntent = new Intent(TriviaActivity.this, QuizResultActivity.class);
//            resultIntent.putExtra("username", username);
//            resultIntent.putExtra("score", score);
//            resultIntent.putExtra("count", count);
//            startActivity(resultIntent);

            new AlertDialog.Builder(this)
                    .setTitle("Confirm Submission")
                    .setMessage("Are you sure you want to submit the quiz?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // User clicked "Yes", prompt for username
                        promptForUsername();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        // User clicked "Cancel", do nothing
                    })
                    .show();
        });

    }
    private void promptForUsername() {
        // Create an EditText view for username input
        final EditText input = new EditText(this);
        input.setHint("Enter your username");

        // Create and show an AlertDialog to prompt for username
        new AlertDialog.Builder(this)
                .setTitle("Enter Username")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    // User clicked "OK", get the entered username
                    String username = input.getText().toString().trim();

                    if (!username.isEmpty()) {
                        int score = qtnAdapter.getScore();
                        int count = qtnList.size();
                        double percentage = ((double) score / qtnList.size()) * 100;
                        // Format the percentage to two decimal places

                        // Launch the QuizResultActivity with username, score, and count
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
                    // User clicked "Cancel", do nothing
                })
                .show();

    }


}

