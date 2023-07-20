package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import algonquin.cst2335.finalproject.databinding.ActivityTriviaQuestionBinding;

public class TriviaQuestion extends AppCompatActivity {
    ActivityTriviaQuestionBinding binding;
    private RecyclerView questionsRecylerView;
    private List<Question> qtnList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_question);
        binding = ActivityTriviaQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("FlightData", Context.MODE_PRIVATE);
        String iataCode =  prefs.getString("iataCode", "");
        if (!iataCode.equals("")){
            binding.editText.setText(iataCode);
        }

        binding.submit.setOnClickListener(click -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(TriviaQuestion.this);
            builder.setMessage("Do You Want To Submit your answer ?");
            builder.setTitle("Attention!");
            builder.setNegativeButton("No", (cl, which) -> {
                // Code to handle "No" button click
            });

            builder.setPositiveButton("Yes", (cl, which) -> {
                // Code to handle "Yes" button click
                Snackbar.make(binding.submit, "You submited your answer", Snackbar.LENGTH_LONG)
                        .setAction("Undo", (snackbarClick) -> {
                            // Code to handle "Undo" action in the Snackbar
                        })
                        .show();
            });

            builder.create().show();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("iataCode", binding.editText.getText().toString());
            editor.apply();
        });

        binding.prev.setOnClickListener(click ->{
            Toast.makeText(this,"You went to previous question", Toast.LENGTH_LONG).show();
        });

        questionsRecylerView = binding.view;
        questionsRecylerView.setLayoutManager(new LinearLayoutManager(this));

//        fetchQuestions();
    }
//    private void fetchQuestions(){
//        String url = "https://opentdb.com/api.php?amount=10&category=22&type=multiple";
////        JsonObjectRequest request = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener() ->{
////            qtnList = parseQuestions(response);
////        });
//    }
//
//    private List<Question> parseQuestions(Object response) {
//    }


}