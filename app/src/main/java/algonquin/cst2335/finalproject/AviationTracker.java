package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import algonquin.cst2335.finalproject.data.Flight;
import algonquin.cst2335.finalproject.data.FlightDAO;
import algonquin.cst2335.finalproject.data.FlightDatabase;
import algonquin.cst2335.finalproject.data.FlightDetailsFragment;
import algonquin.cst2335.finalproject.data.FlightViewModel;
import algonquin.cst2335.finalproject.databinding.ActivityAviationTrackerBinding;
import algonquin.cst2335.finalproject.databinding.ActivityFlightListBinding;

public class AviationTracker extends AppCompatActivity implements  FlightDetailsFragment.OnFlightDetailsListener {
    protected RequestQueue queue = null;
    protected String delay;
    protected String destination;
    protected String iataCodeArrival;
    protected String terminal;
    protected String gate;
    ActivityAviationTrackerBinding binding;
    RecyclerView.Adapter<FlightViewHolder> myAdapter;
    ArrayList<Flight> flightlist;
    FlightViewModel flightModel;
    FlightDAO flightDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);
        binding = ActivityAviationTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.myToolbar);

        //Save the selected flight details to the Room database
        FlightDatabase db = Room.databaseBuilder(getApplicationContext(),
                FlightDatabase.class, "flightDatabase").build();
        flightDAO = db.flightDAO();

        FlightViewModel.selectedFlight.observe(this, (newValue) -> {
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();

            boolean isSavedFlight = isFlightSavedInDatabase(newValue);
            FlightDetailsFragment flightFragment = new FlightDetailsFragment( newValue, isSavedFlight);
            flightFragment.setOnFlightDetailsListener(this);
            tx.add(R.id.fragmentLocation, flightFragment);
            tx.replace(R.id.fragmentLocation, flightFragment);
            tx.commit();
            tx.addToBackStack("");
        });

        SharedPreferences prefs = getSharedPreferences("FlightData", Context.MODE_PRIVATE);
        String iataCode =  prefs.getString("iataCode", "");
        if (!iataCode.equals("")){
            binding.inputCode.setText(iataCode);
        }

        flightModel = new ViewModelProvider(this).get(FlightViewModel.class);

        flightlist = flightModel.flights.getValue();

        if(flightlist == null)
        {
            flightModel.flights.postValue( flightlist = new ArrayList<Flight>());
        }

        binding.savedFlightButton.setOnClickListener(click -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(AviationTracker.this);
            builder.setMessage("Do You Want To View Saved Flight ?");
            builder.setTitle("Attention!");
            builder.setNegativeButton("No", (cl, which) -> {
                // Code to handle "No" button click
            });

            builder.setPositiveButton("Yes", (cl, which) -> {
                flightlist.clear();
                myAdapter.notifyDataSetChanged();
                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(() ->
                {
                    flightlist.addAll(flightDAO.getAllFlights()); //Once you get the data from database
                    runOnUiThread( () ->  myAdapter.notifyDataSetChanged());
                });
            });

            builder.create().show();
        });


        binding.searchFlightButton.setOnClickListener(click -> {
            if(binding.inputCode.getText().toString().equals("")){
                String message = "Please enter a valid code";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            flightlist.clear();
            myAdapter.notifyDataSetChanged();
            String inputCode = binding.inputCode.getText().toString().toUpperCase().trim();
            String stringUrl = "http://api.aviationstack.com/v1/flights?access_key=639c8417ee6ce1d35459e96dbdbb54f6&dep_iata="+ inputCode;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringUrl, null,
                    (successfulResponse) -> {

                        try {
                            JSONArray  data = successfulResponse.getJSONArray("data");
                            int dataLength = data.length();
                            for(int i = 0; i< dataLength; i++){
                                JSONObject thisObj = data.getJSONObject(i);
                                JSONObject arrival = thisObj.getJSONObject("arrival");
                                iataCodeArrival = arrival.getString("iata");
                                destination = arrival.getString("airport");
                                JSONObject departure = thisObj.getJSONObject("departure");
                                terminal = departure.getString("terminal");
                                gate = departure.getString("gate");
                                // add the code to handle delay

                                Object delayObject = departure.opt("delay");

                                if (delayObject.equals(null)) {
                                    delay = "N/A";
                                } else if (delayObject instanceof Integer) {
                                    int delayInteger = (int) delayObject;
                                    delay = String.valueOf(delayInteger) +  " min";
                                }
                                flightlist.add(new Flight(destination,iataCodeArrival, terminal, gate,delay));
                                myAdapter.notifyItemInserted(flightlist.size()-1);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    (errorResponse)->{
                        int i = 0;
                    } );
            queue.add(request);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("iataCode", binding.inputCode.getText().toString());
            editor.apply();

            binding.inputCode.setText("");
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<FlightViewHolder>()    {
            @NonNull
            @Override
            public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ActivityFlightListBinding binding = ActivityFlightListBinding.inflate(getLayoutInflater());
                return new FlightViewHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
                Flight flight = flightlist.get(position);
                holder.destinationText.setText(flight.getDestination());
                holder.terminalText.setText(flight.getTerminal());
                holder.gateText.setText(flight.getGate());
                holder.delayText.setText(flight.getDelay());
            }

            @Override
            public int getItemCount() {

                return flightlist.size();
            }
        });
    }

    class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView destinationText;
        TextView terminalText;
        TextView delayText;
        TextView gateText;
        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk->{
                int position = getAbsoluteAdapterPosition();
                Flight selected = flightlist.get(position);
                flightModel.selectedFlight.postValue(selected);

            });
            destinationText = itemView.findViewById(R.id.destinationTextView);
            terminalText = itemView.findViewById(R.id.terminalTextView);
            gateText = itemView.findViewById(R.id.gateTextView);
            delayText = itemView.findViewById(R.id.delayTextView);
        }
    }

    // Implement the onFlightDeleted() method from the OnFlightDeletedListener interface
    @Override
    public void onFlightDeleted(Flight flight) {
        // Remove the deleted flight from the flightlist and update the RecyclerView
        flightlist.remove(flight);
        myAdapter.notifyDataSetChanged();
    }
    @Override
    public void onFlightSaved(Flight flight) {
        // Save the deleted flight from the flightlist and update the RecyclerView
        flightlist.add(flight);
        myAdapter.notifyDataSetChanged();
    }
    private boolean isFlightSavedInDatabase(Flight flight) {
        long selectedId = flight.getId();
        // Use the provided ExecutorService to execute the database query in a separate thread
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executorService.submit(() -> {
            for (Flight savedFlight : flightDAO.getAllFlights()) {
                if (savedFlight.getId() == selectedId) {
                    return true;
                }
            }
            return false;
        });

        // Get the result of the database query (true or false) from the Future
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false; // Return a default value in case of an exception
        } finally {
            // Shutdown the ExecutorService to release resources
            executorService.shutdown();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.menu_flight ) {
            startActivity(new Intent(this, AviationTracker.class));
        } else if( item.getItemId() == R.id.menu_help ){
            AlertDialog.Builder builder = new AlertDialog.Builder(AviationTracker.this)
                    .setMessage(R.string.aviation_help_message)
                    .setTitle("Instructions!")
                    .setPositiveButton("OK", (cl, which) -> {
            });
            builder.create().show();
        }
        return true;
    }
}

