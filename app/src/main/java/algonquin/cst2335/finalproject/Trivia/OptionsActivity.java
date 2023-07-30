package algonquin.cst2335.finalproject.Trivia;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.ActivityTriviaQuestionBinding;

//public class OptionsActivity extends TriviaActivity {
//
//    ActivityTriviaQuestionBinding binding;
//
//    private RecyclerView questionsRecylerView;
//    private QuestionsAdapter qtnAdapter;
//    private TriviaActivity trivia;
//    private List<Question> qtnList;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        binding = ActivityTriviaQuestionBinding.inflate(getLayoutInflater());
//        setContentView(R.layout.list_item_question);
//        questionsRecylerView = binding.view;
//
//        String url = "https://opentdb.com/api.php?amount=10&category=22&type=multiple";
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                (successfulResponse) ->{
//                    int it = 0;
//                    try {
//                        JSONArray results = successfulResponse.getJSONArray("results");
//                        qtnList = new ArrayList<>();
//
//                        for(int i = 0; i < results.length(); i++){
//                            JSONObject result = results.getJSONObject(i);
//                            String category = result.getString("category");
//                            String type = result.getString("type");
//                            String difficulty = result.getString("difficulty");
//                            String questionText = result.getString("question");
//                            String correctAnswer = result.getString("correct_answer");
//                            JSONArray incorrectAnswers = result.getJSONArray("incorrect_answers");
//
//                            List<String> options = new ArrayList<>();
//                            options.add(correctAnswer);
//                            for (int j = 0; j < incorrectAnswers.length(); j++) {
//                                options.add(incorrectAnswers.getString(j));
//                            }
//
//                            Question question = new Question(questionText, options, correctAnswer);
//                            qtnList.add(question);
////
//                            if (qtnList != null && !qtnList.isEmpty()){
//                                qtnAdapter = new QuestionsAdapter(qtnList);
//                                questionsRecylerView.setAdapter(qtnAdapter);
//                                qtnAdapter.notifyDataSetChanged();
//                            }
//
//                        }
//
//                    } catch (JSONException e){
//                        e.printStackTrace();
//                    }
//
//                },
//                (errorResponse)->{
//                    int j = 0;
//                    Toast.makeText(this, "Failed to fetch questions", Toast.LENGTH_SHORT).show();
//                });
//        // Add the request to the Volley queue
//        Volley.newRequestQueue(this).add(request);
//
//
////            Intent intent = new Intent(TriviaActivity.this, OptionsActivity.class);
////            startActivity(intent);
//
//        questionsRecylerView.setLayoutManager(new LinearLayoutManager(this));
//
//    }

    public class OptionsActivity extends TriviaActivity {

        private RecyclerView questionsRecyclerView;
        private QuestionsAdapter qtnAdapter;
        private List<Question> qtnList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_trivia_question);
            questionsRecyclerView = findViewById(R.id.view);

            String url = "https://opentdb.com/api.php?amount=10&category=22&type=multiple";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (successfulResponse) -> {
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
                                questionsRecyclerView.setAdapter(qtnAdapter);
                                qtnAdapter.notifyDataSetChanged();
                            }

                        }

                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                    },
                    (errorResponse) -> {
                        Toast.makeText(this, "Failed to fetch questions", Toast.LENGTH_SHORT).show();
                    });
            // Add the request to the Volley queue
            Volley.newRequestQueue(this).add(request);

            questionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }


