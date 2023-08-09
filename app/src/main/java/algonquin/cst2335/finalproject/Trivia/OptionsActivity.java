package algonquin.cst2335.finalproject.Trivia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import algonquin.cst2335.finalproject.AviationTracker;
import algonquin.cst2335.finalproject.BearImageGenerator;
import algonquin.cst2335.finalproject.Currency.CurrencyGenerator;
import algonquin.cst2335.finalproject.MainActivity;
import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.ActivityTriviaQtnsSetupBinding;

/**
 * The OptionsActivity class represents the activity where users can set up options for the trivia quiz.
 * Users can select a category, enter the number of questions, and start the trivia quiz.
 */
public class OptionsActivity extends AppCompatActivity {

    private List<Question> qtnList;
    ActivityTriviaQtnsSetupBinding binding;

    /**
     * Called when the activity is first created. This method initializes the activity's UI components,
     * sets up event listeners, and handles the logic for setting up trivia options.
     *
     * @param savedInstanceState A Bundle containing the saved instance state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTriviaQtnsSetupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

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

    /**
     * Initialize the options menu for the activity.
     *
     * @param menu The menu to be initialized.
     * @return true if the menu was successfully initialized, false otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    /**
     * Handle menu item selection events.
     *
     * @param item The selected menu item.
     * @return true if the menu item event was handled, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_bear_home) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (item.getItemId() == R.id.menu_bear_help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OptionsActivity.this)
                    .setMessage(R.string.opt_help)
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
