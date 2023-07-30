package algonquin.cst2335.finalproject.Trivia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.finalproject.R;

public class MainTriviaActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_mainpage);

        Button goBtn = findViewById(R.id.goBtn);
        goBtn.setOnClickListener(click ->{
            startActivity(new Intent(this, OptionsActivity.class));

        });

    }

}
