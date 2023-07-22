package algonquin.cst2335.finalproject;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.ActivityCurrencyGeneratorBinding;


public class CurrencyGenerator extends AppCompatActivity {
    ActivityCurrencyGeneratorBinding binding;

    RequestQueue queue = null;

    CurrencyViewModel currencymodel;
    ArrayList<CurrencyObject> message;

    CurrencyDao mDao;

    private RecyclerView.Adapter myAdapter;

    Executor thread = Executors.newSingleThreadExecutor();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityCurrencyGeneratorBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        setContentView(binding.getRoot());
        queue = Volley.newRequestQueue(this);

        Spinner dropdown = findViewById(R.id.spinto);
        String[] items = new String[]{"CAD", "USD", "DHZ"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        Spinner dropdown2 = findViewById(R.id.spinfrom);
        String[] items2 = new String[]{"CAD", "USD", "DHZ"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        dropdown2.setAdapter(adapter2);

//        Button btn_search = findViewById(R.id.sendButton);
        SharedPreferences prefs = getSharedPreferences("convert from", Context.MODE_PRIVATE);
        String iataCode = prefs.getString("iataCode", "");
        if (!iataCode.equals("")) {
            binding.convertfrom.setText(iataCode);
        }
        binding.sendButton.setOnClickListener(clik ->
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyGenerator.this);
            builder.setMessage("Do You Want To Save this Currency ?");
            builder.setTitle("Attention!");
            builder.setNegativeButton("No", (cl, which) -> {
                // Code to handle "No" button click
            });

            builder.setPositiveButton("Yes", (cl, which) -> {
                // Code to handle "Yes" button click
                Snackbar.make(binding.sendButton, "You saved The currency", Snackbar.LENGTH_LONG)
                        .setAction("Undo", (snackbarClick) -> {
                            // Code to handle "Undo" action in the Snackbar
                        })
                        .show();
            });

            builder.create().show();

            if (binding.convertfrom.getText().toString().equals("")) {
                String message = "Please enter a valid code";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("iataCode", binding.convertfrom.getText().toString());
            editor.apply();

            String convfrom = binding.spinfrom.getSelectedItem().toString();
            String convto = binding.spinto.getSelectedItem().toString();
            String amount = binding.convertfrom.getText().toString();
            String stringUrl = "https://api.getgeoapi.com/v2/currency/convert?format=json&from=" + convfrom + "&to=" + convto + "&amount=" + amount + "&api_key=199f9d4d706aa96f2918fc148ce31a12e8035972&format=json";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringUrl, null,
                    (successfulResponse) -> {
                        JSONObject rates = null;
                        try {
                            rates = successfulResponse.getJSONObject("rates");

                            // Get the "CAD" object from "rates"
                            JSONObject cadObject = rates.getJSONObject(convto);

                            // Get the "rate_for_amount" value from "CAD" object
                            double rateForAmount = cadObject.getDouble("rate_for_amount");

                            // Now you have the rate_for_amount value, you can set it in the EditText
                            String rateText = String.valueOf(rateForAmount);
                            binding.too.setText(rateText);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    },
                    (errorResponse) -> {
                        int j = 0;
                    });
            queue.add(request);


        });

    }
}