package algonquin.cst2335.finalproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.ActivityCurrencyGeneratorBinding;
import algonquin.cst2335.finalproject.databinding.ActivityCurrencyconvertedBinding;


public class CurrencyGenerator extends AppCompatActivity {
    ActivityCurrencyGeneratorBinding binding;

    RequestQueue queue = null;

    CurrencyViewModel currencymodel;
    ArrayList<CurrencyObject> currencylist;

    CurrencyDao mDao;

    private RecyclerView.Adapter myAdapter;

    Executor thread = Executors.newSingleThreadExecutor();


    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView currencyFrom;
        TextView currencyTo;
        TextView amountFrom;
        TextView amountTo;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            currencyFrom = itemView.findViewById(R.id.currencyFrom);
            currencyTo = itemView.findViewById(R.id.currencyTo);
            amountFrom = itemView.findViewById(R.id.amountFrom);
            amountTo = itemView.findViewById(R.id.amountTo);

            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();
                CurrencyObject selected = currencylist.get(position);

                currencymodel.selectedMessage.postValue(selected);
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.icon_flight) {
            startActivity(new Intent(this, AviationTracker.class));
        } else if (item.getItemId() == R.id.icon_question) {
            startActivity(new Intent(this, TriviaQuestion.class));
        } else if (item.getItemId() == R.id.icon_bear) {
            startActivity(new Intent(this, BearImageGenerator.class));
        } else
        if (item.getItemId() == R.id.icon_back) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (item.getItemId() == R.id.help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyGenerator.this);
            builder.setTitle("HELP: ")
                    .setMessage("1.Input a number\n2.Pick a currency (from)\n3.Pick a currency (to)\n" +
                            "4.Click CONVERT to make a conversion\n5.Click ARROWS between currencies to swap them")
                    .setPositiveButton("OK", (dialog, click) -> {}).create().show();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCurrencyGeneratorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currencymodel = new ViewModelProvider(this).get(CurrencyViewModel.class);

        currencylist = currencymodel.currency.getValue();

        setSupportActionBar(binding.toolbar);

//        if (currencylist == null) {
//            currencymodel.currency.postValue(currencylist = new ArrayList<CurrencyObject>());
//        }
        if (currencylist == null) {
            currencymodel.currency.setValue(new ArrayList<>());
            currencylist = currencymodel.currency.getValue();
        }

        binding.recycleview.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ActivityCurrencyconvertedBinding binding = ActivityCurrencyconvertedBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding.getRoot());
            }


            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                CurrencyObject currency = currencylist.get(position);
                holder.currencyFrom.setText(currency.convertfrom);
                holder.currencyTo.setText(currency.converto);
                holder.amountFrom.setText(currency.cfrom);
                holder.amountTo.setText(currency.too);

            }

            @Override
            public int getItemCount() {
                return currencylist.size();
            }
        });


        queue = Volley.newRequestQueue(this);

        Spinner dropdown = findViewById(R.id.spinto);
        String[] items = new String[]{"CAD", "USD", "AED", "EGP", "SAR"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        Spinner dropdown2 = findViewById(R.id.spinfrom);
        String[] items2 = new String[]{"CAD", "USD", "AED", "EGP", "SAR"};
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


            if (binding.convertfrom.getText().toString().equals("")) {
                String message = "Please enter a valid code";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("iataCode", binding.convertfrom.getText().toString());
            editor.apply();

            String converfrom = binding.spinfrom.getSelectedItem().toString();
            String converto = binding.spinto.getSelectedItem().toString();
            String amount = binding.convertfrom.getText().toString();
            String stringUrl = "https://api.getgeoapi.com/v2/currency/convert?format=json&from=" + converfrom + "&to=" + converto + "&amount=" + amount + "&api_key=78a4cd019e48494b85eec733e0b8c3426f268ccf&format=json";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringUrl, null,
                    (successfulResponse) -> {
                        JSONObject rates = null;
                        try {
                            rates = successfulResponse.getJSONObject("rates");

                            // Get the "CAD" object from "rates"
                            JSONObject cadObject = rates.getJSONObject(converto);

                            // Get the "rate_for_amount" value from "CAD" object
                            double rateForAmount = cadObject.getDouble("rate_for_amount");

                            // Now you have the rate_for_amount value, you can set it in the EditText
                            String rateText = String.valueOf(rateForAmount);

                            binding.too.setText(rateText);
                            currencylist.add(new CurrencyObject(converto, converfrom, amount, rateText));
                            myAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    },
                    (errorResponse) -> {
                        int j = 0;
                    });
            queue.add(request);


        });

        binding.recycleview.setLayoutManager(new LinearLayoutManager(this));

        currencymodel.selectedMessage.observe(this, (newvalue) -> {
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();

            CurrencyDetailsFragment chatFragment = new CurrencyDetailsFragment(newvalue);
            tx.add(R.id.fragmentLocation, chatFragment);
            tx.replace(R.id.fragmentLocation, chatFragment);
            tx.commit();
            tx.addToBackStack("");
            Toast.makeText(this, "version 1.0, code by Seifeldin Eid", Toast.LENGTH_LONG).show();
        });


    }
}