/**
 * done by Seifeldin Eid
 * Student #041084185
 * Final project
 */

package algonquin.cst2335.finalproject.Currency;


import static android.widget.Toast.LENGTH_SHORT;

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
/**
 * CurrencyGenerator is an activity that allows the user to perform currency conversions
 * using an API. It also provides the functionality to save and view previously converted
 * currency values. The activity extends AppCompatActivity and handles user interactions
 * related to currency conversions and data presentation.
 */
public class CurrencyGenerator extends AppCompatActivity {
    /**
     * ActivityCurrencyGeneratorBinding represents the ViewBinding for the CurrencyGenerator activity.
     * It is used to access and bind views in the layout XML file.
     */
    ActivityCurrencyGeneratorBinding binding;
    /**
     * RequestQueue used to handle API requests using Volley library.
     */

    RequestQueue queue = null;

    /**
     * ViewModel instance to hold the data for the Currency conversions.
     */
    CurrencyViewModel currencymodel;
    /**
     * ArrayList to hold the list of CurrencyObject representing the converted currency values.
     */
    ArrayList<CurrencyObject> currencylist;

    /**
     * CurrencyDao instance to interact with the Room database for Currency conversions.
     */
    CurrencyDao myDAO;
    /**
     * RecyclerView adapter used to display the converted currency items.
     */
    private RecyclerView.Adapter myAdapter;


    /**
     * This method is called when the activity is first created. It initializes the views,
     * sets up RecyclerView, and handles user interactions related to currency conversions
     * and saving the converted currency data.
     */
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
                    .setMessage(R.string.message)
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
                Toast.makeText(this, R.string.valid, LENGTH_SHORT).show();
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
                            currencylist.clear();
                            myAdapter.notifyDataSetChanged();
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
        Executor loadThread = Executors.newSingleThreadExecutor();
        loadThread.execute(() -> {
            List<CurrencyObject> savedConversions = myDAO.getMessages();
            Log.d("CurrencyGenerator", "Loaded " + savedConversions.size() + " conversions from the database.");
            runOnUiThread(() -> {
                if (savedConversions.isEmpty()) {
                    // Show a Snackbar or Toast indicating that there are no saved currencies
                    Snackbar.make(binding.getRoot(), R.string.nofound, Snackbar.LENGTH_LONG).show();
                } else {
                    currencylist.clear();
                    currencylist.addAll(savedConversions);
                    if (myAdapter == null) {
                        binding.recycleview.setAdapter(null);
                    } else {
                        myAdapter.notifyDataSetChanged();
                    }
                    Log.d("CurrencyGenerator", "Posted " + savedConversions.size() + " conversions to the ViewModel.");
                }
            });
        });

    }

    /**
     * MyRowHolder is an inner class that represents the ViewHolder for the RecyclerView in the
     * CurrencyGenerator activity. It holds references to the views in each row of the RecyclerView
     * and handles click and long click events on the RecyclerView items.
     */
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView currencyFrom;
        TextView currencyTo;
        TextView amountFrom;
        TextView amountTo;

        /**
         * Constructor for MyRowHolder that initializes the views in the RecyclerView row.
         *
         * @param itemView The root view of the RecyclerView row.
         */
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            currencyFrom = itemView.findViewById(R.id.currencyTo);
            currencyTo = itemView.findViewById(R.id.currencyFrom);
            amountFrom = itemView.findViewById(R.id.amountFrom);
            amountTo = itemView.findViewById(R.id.amountTo);

            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();
                CurrencyObject selected = currencylist.get(position);
                currencymodel.selectedMessage.postValue(selected);
            });

            itemView.setOnLongClickListener(longClick -> {
                int position = getAbsoluteAdapterPosition();
                CurrencyObject selected = currencylist.get(position);

                // Show a dialog to confirm the deletion
                new AlertDialog.Builder(itemView.getContext())
                        .setTitle("Delete")
                        .setMessage(R.string.delete)
                        .setPositiveButton("Yes", (dialog, c) -> {
                            CurrencyObject m = currencylist.get(position);
                            Executor thread2 = Executors.newSingleThreadExecutor();
                            thread2.execute(() -> {
                                myDAO.delete(m);
                                currencylist.remove(position);

                                // Must be done on the main UI thread to update the RecyclerView
                                runOnUiThread(() -> {
                                    myAdapter.notifyDataSetChanged();
                                });

                                // Show a Snackbar with the option to undo the deletion
                                Snackbar snackbar = Snackbar.make(itemView, R.string.deleted, Snackbar.LENGTH_LONG);
                                snackbar.setAction(R.string.undo, clk -> {
                                    Executor myThread = Executors.newSingleThreadExecutor();
                                    myThread.execute(() -> {
                                        myDAO.insertConvertTo(m);
                                        currencylist.add(position, m);

                                        // Must be done on the main UI thread to update the RecyclerView
                                        runOnUiThread(() -> {
                                            myAdapter.notifyDataSetChanged();
                                        });
                                    });
                                });
                                snackbar.show();
                            });
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            // Do nothing, just dismiss the dialog
                        })
                        .show();

                return true; // Consume the event to prevent short click event triggering as well
            });
        }
    }
}