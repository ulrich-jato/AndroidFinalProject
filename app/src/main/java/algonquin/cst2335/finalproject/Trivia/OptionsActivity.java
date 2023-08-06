//package algonquin.cst2335.finalproject.Trivia;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//import algonquin.cst2335.finalproject.R;
//import algonquin.cst2335.finalproject.databinding.ActivityTriviaQuestionBinding;
//
//    public class OptionsActivity extends AppCompatActivity {
//
//        private RecyclerView questionsRecyclerView;
//        private QuestionsAdapter qtnAdapter;
//        private List<Question> qtnList;
//        ActivityTriviaQuestionBinding binding;
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
////            binding = ActivityTriviaQuestionBinding.inflate(getLayoutInflater());
//            setContentView(R.layout.activity_trivia_qtns_setup);
////            questionsRecyclerView = findViewById(R.id.view);
//            Button btn = findViewById(R.id.goBtn);
//            EditText numberOfQtns = findViewById(R.id.questionsNum);
//
//            /*
//            For passing data between different pages/activities.
//             */
//            SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = prefs.edit();
//            String choice = prefs.getString("Category", "");
//
//            //passing the dropdown object to java here.
//            Spinner category = findViewById(R.id.dropdown);
//
//            //Creating an Array responsible for rendering the the dropdown items on access.
//            ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
//
//            //Attaching the dropdown menu to the adapter.
//            category.setAdapter(adapter);
//            category.setBackgroundColor(Color.WHITE);
//
//
//            btn.setOnClickListener(click ->{
//                /*
//                Stores the value of the editText field (number of questions) to an integer and will be validated
//                not to exceed 50. A toast message will be displayed informing the input should not exceed 50.
//                 */
//
//                int numOfQtns;
//
//                    numOfQtns = Integer.parseInt(numberOfQtns.getText().toString());
////                    Toast.makeText(this, "Number of questions cannot be blankss. Enter a digit (1 - 50)", Toast.LENGTH_SHORT).show();
//
//                 if (numOfQtns > 50){
//                    Toast.makeText(this, "Number of questions cannot exceed 50", Toast.LENGTH_SHORT).show();
//                }else if((numOfQtns == 0)){
//
//                    Toast.makeText(this, "Number of questions cannot be blank. Enter a digit (1 - 50)", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                     category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                         @Override
//                         public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                             String selectedCategory = adapterView.getItemAtPosition(position).toString();
//                             int qtnNum = Integer.parseInt(numberOfQtns.getText().toString());
//                             editor.putString("selectedCategory", selectedCategory);
//                             editor.putInt("questionSize", qtnNum);
//                             editor.apply();
//
//                         }
//
//                         @Override
//                         public void onNothingSelected(AdapterView<?> adapterView) {
//
//                         }
//                     });
//                     Intent intent = new Intent(OptionsActivity.this, TriviaActivity.class);
//                     intent.putExtra("selectedCategory", adapter.getItem(category.getSelectedItemPosition()).toString());
//                     intent.putExtra("questionSize", numberOfQtns.getText().toString());
//                     startActivity(intent);
//
//                }
//            });
//
//        }
//    }
//
//


package algonquin.cst2335.finalproject.Trivia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import algonquin.cst2335.finalproject.R;

public class OptionsActivity extends AppCompatActivity {

    private List<Question> qtnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_qtns_setup);

        Button btn = findViewById(R.id.goBtn);
        EditText numberOfQtns = findViewById(R.id.questionsNum);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String choice = prefs.getString("Category", "");

        Spinner category = findViewById(R.id.dropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        category.setAdapter(adapter);
        category.setBackgroundColor(Color.WHITE);

        String savedCategory = prefs.getString("selectedCategory", "");
        int savedQuestionSize = prefs.getInt("questionSize", 10);

        int categoryPosition = adapter.getPosition(savedCategory);
        category.setSelection(categoryPosition);

        numberOfQtns.setText(String.valueOf(savedQuestionSize));

        btn.setOnClickListener(click -> {
            int numOfQtns;
            numOfQtns = Integer.parseInt(numberOfQtns.getText().toString());

            if (numOfQtns > 50) {
                Toast.makeText(this, "Number of questions cannot exceed 50", Toast.LENGTH_SHORT).show();
            } else if (numOfQtns == 0) {
                Toast.makeText(this, "Number of questions cannot be blank. Enter a digit (1 - 50)", Toast.LENGTH_SHORT).show();
            } else {
                category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        String selectedCategory = adapterView.getItemAtPosition(position).toString();
                        int qtnNum = Integer.parseInt(numberOfQtns.getText().toString());
                        editor.putString("selectedCategory", selectedCategory);
                        editor.putInt("questionSize", qtnNum);
                        editor.apply();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                Intent intent = new Intent(OptionsActivity.this, TriviaActivity.class);
                intent.putExtra("selectedCategory", adapter.getItem(category.getSelectedItemPosition()).toString());
                intent.putExtra("questionSize", numberOfQtns.getText().toString());
                startActivity(intent);
            }
        });
    }
}
