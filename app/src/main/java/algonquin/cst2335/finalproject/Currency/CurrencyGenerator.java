package algonquin.cst2335.finalproject.Currency;


import static android.widget.Toast.LENGTH_SHORT;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.AviationTracker;
import algonquin.cst2335.finalproject.BearImageGenerator;
import algonquin.cst2335.finalproject.MainActivity;
import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.TriviaQuestion;
import algonquin.cst2335.finalproject.databinding.ActivityCurrencyGeneratorBinding;
import algonquin.cst2335.finalproject.databinding.ActivityCurrencyconvertedBinding;
import algonquin.cst2335.finalproject.databinding.CurrencySavedBinding;


public class CurrencyGenerator extends AppCompatActivity {
    ActivityCurrencyGeneratorBinding binding;


    RequestQueue queue = null;

    CurrencyViewModel currencymodel;
    ArrayList<CurrencyObject> currencylist;

    CurrencyDetailsFragment displayedFragment;

    private static final String PREFS_NAME = "MyPrefsFile"; // Unique name for your SharedPreferences
    private static final String AMOUNT_KEY = "amountKey"; // Key for storing the amount
    CurrencyDao myDAO;
    //    CurrencyDatabase myDB;
    private RecyclerView.Adapter myAdapter;

    Executor thread = Executors.newSingleThreadExecutor();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        } else if (item.getItemId() == R.id.icon_back) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (item.getItemId() == R.id.help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyGenerator.this);
            builder.setTitle("HELP: ")
                    .setMessage("1.Input a number\n2.Pick a currency (from)\n3.Pick a currency (to)\n" +
                            "4.Click CONVERT to make a conversion\n5.Click ARROWS between currencies to swap them")
                    .setPositiveButton("OK", (dialog, click) -> {
                    }).create().show();
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
        CurrencyDatabase myDB = Room.databaseBuilder(getApplicationContext(), CurrencyDatabase.class, "database-name").build();
        myDAO = myDB.cmDAO();

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
                Toast.makeText(this, message, LENGTH_SHORT).show();
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
        currencymodel.selectedMessage.observe(this, (newValue) -> {
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();

            CurrencyDetailsFragment chatFragment = new CurrencyDetailsFragment(newValue);
            tx.add(R.id.fragment, chatFragment);
            tx.replace(R.id.fragment, chatFragment);
            tx.commit();
            tx.addToBackStack("");
        });
        binding.hibutton.setOnClickListener(click -> {
            loadSavedConversions();
        });
    }
    private void loadSavedConversions() {
        try {

            Executor loadThread = Executors.newSingleThreadExecutor();
            Toast.makeText(this, "you saved me", Toast.LENGTH_LONG).show();
            loadThread.execute(() -> {
                List<CurrencyObject> saved = myDAO.getAllmessages();
                Log.d("CurrencyActivity", "Loaded " + saved.size() + " conversions from the database.");
                runOnUiThread(() -> {
                    currencylist.clear();
                    currencylist.addAll(saved);
                    if (myAdapter == null) {

                        binding.recycleview.setAdapter((RecyclerView.Adapter) saved);
                        myAdapter.notifyDataSetChanged();
                    } else {
                        myAdapter.notifyDataSetChanged();
                    }

                    Log.d("CurrencyActivity", "Posted " + saved.size() + " conversions to the ViewModel.");


                });
            });
        }catch (Exception e) {
            e.printStackTrace();
            Log.e("CurrencyActivity", "Error loading saved conversions: " + e.getMessage());
        }
    }
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
//            itemView.setOnLongClickListener(longClick -> {
//                int position = getAbsoluteAdapterPosition();
//                CurrencyObject selected = currencylist.get(position);
//
//                // Show a dialog to confirm the deletion
//                new AlertDialog.Builder(CurrencyGenerator.this)
//                        .setTitle("Delete")
//                        .setMessage("Do you want to delete this currency conversion?")
//                        .setPositiveButton("Yes", (dialog, c) -> {
//                            CurrencyObject m = currencylist.get(position);
//                            Executor thread2 = Executors.newSingleThreadExecutor();
//                            thread2.execute(() -> {
//                                myDAO.delete(m);
//                                currencylist.remove(position);
//
//
//                                // Must be done on the main UI thread to update the RecyclerView
//                                runOnUiThread(() -> {
//                                            myAdapter.notifyDataSetChanged();
//                                        });
//                                    // Show a Snackbar with the option to undo the deletion
//                                    Snackbar.make(itemView, "Currency conversion deleted!", Snackbar.LENGTH_LONG)
//                                            .setAction("Undo", clk -> {
//                                                Executor myThread = Executors.newSingleThreadExecutor();
//                                                myThread.execute(() -> {
//                                                    myDAO.insertConvertTo(m);
//                                                    currencylist.add(position, m);
//
//                                                    // Must be done on the main UI thread to update the RecyclerView
//                                                    runOnUiThread(() -> {
//                                                        myAdapter.notifyDataSetChanged();
//                                                    });
//                                                });
//                                            })
//                                            .show();
//                                });
//                            })
//
//                        .setNegativeButton("No", (dialog, which) -> {
//                            // Do nothing, just dismiss the dialog
//                        })
//                        .show();
//
//                return true; // Consume the event to prevent short click event triggering as well
//            });
        }
    }

}
