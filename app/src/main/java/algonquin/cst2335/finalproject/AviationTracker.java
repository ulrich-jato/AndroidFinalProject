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

/**
 * An activity that tracks aviation flights and displays flight details using RecyclerView.
 * It allows users to search and view flight details and save flights to a local database.
 *
 * @author Jato Ulrich Guiffo Kengne
 */
public class AviationTracker extends AppCompatActivity implements FlightDetailsFragment.OnFlightDetailsListener {

    /**
     * The RequestQueue instance for handling network requests using Volley.
     */
    protected RequestQueue queue = null;

    /**
     * Represents the delay status of a flight.
     */
    protected String delay;

    /**
     * Represents the destination of a flight.
     */
    protected String destination;

    /**
     * Represents the IATA code of the arrival airport of a flight.
     */
    protected String iataCodeArrival;

    /**
     * Represents the terminal information of a flight's departure.
     */
    protected String terminal;

    /**
     * Represents the gate information of a flight's departure.
     */
    protected String gate;


    // View binding for the activity
    ActivityAviationTrackerBinding binding;

    // RecyclerView adapter for flight list
    RecyclerView.Adapter<FlightViewHolder> myAdapter;

    // ArrayList to store the list of flights
    ArrayList<Flight> flightlist;

    // ViewModel for handling data operations
    FlightViewModel flightModel;

    // Data Access Object for Flight database
    FlightDAO flightDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);

        // Inflate the layout using ViewBinding
        binding = ActivityAviationTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.myToolbar);

        // Initialize the Room database and FlightDAO
        FlightDatabase db = Room.databaseBuilder(getApplicationContext(),
                FlightDatabase.class, "flightDatabase").build();
        flightDAO = db.flightDAO();

        // Observe selectedFlight LiveData and update the FlightDetailsFragment
        FlightViewModel.selectedFlight.observe(this, (newValue) -> {
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();

            // Check if the selected flight is already saved in the database
            boolean isSavedFlight = isFlightSavedInDatabase(newValue);
            FlightDetailsFragment flightFragment = new FlightDetailsFragment(newValue, isSavedFlight);
            flightFragment.setOnFlightDetailsListener(this);

            tx.add(R.id.fragmentLocation, flightFragment);
            tx.replace(R.id.fragmentLocation, flightFragment);
            tx.commit();
            tx.addToBackStack("");
        });

        // Restore the last entered IATA code from SharedPreferences and set it to the input field
        SharedPreferences prefs = getSharedPreferences("FlightData", Context.MODE_PRIVATE);
        String iataCode = prefs.getString("iataCode", "");
        if (!iataCode.equals("")) {
            binding.inputCode.setText(iataCode);
        }

        // Initialize the ViewModel and the flight list
        flightModel = new ViewModelProvider(this).get(FlightViewModel.class);
        flightlist = flightModel.flights.getValue();

        if (flightlist == null) {
            flightModel.flights.postValue(flightlist = new ArrayList<Flight>());
        }

        // Set up the click listener for the "View Saved Flight" button
        binding.savedFlightButton.setOnClickListener(click -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(AviationTracker.this);
            builder.setMessage("Do You Want To View Saved Flight ?");
            builder.setTitle("Attention!");
            builder.setNegativeButton("No", (cl, which) -> {
                // Code to handle "No" button click
            });

            builder.setPositiveButton("Yes", (cl, which) -> {
                // Clear the current flight list
                flightlist.clear();
                myAdapter.notifyDataSetChanged();

                // Fetch saved flights from the database in a background thread
                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(() -> {
                    flightlist.addAll(flightDAO.getAllFlights());
                    // Notify the adapter on the main thread about the data changes
                    runOnUiThread(() -> myAdapter.notifyDataSetChanged());
                });
            });

            builder.create().show();
        });

        // Set up the click listener for the "Search Flight" button
        binding.searchFlightButton.setOnClickListener(click -> {
            if (binding.inputCode.getText().toString().equals("")) {
                String message = "Please enter a valid code";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            // Clear the current flight list
            flightlist.clear();
            myAdapter.notifyDataSetChanged();

            // Retrieve the user-input IATA code
            String inputCode = binding.inputCode.getText().toString().toUpperCase().trim();
            String apiKey = "3622d6091a8e16662411582d9c9a8f13";
            String stringUrl = "http://api.aviationstack.com/v1/flights?access_key="+ apiKey + "&dep_iata=" + inputCode;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringUrl, null,
                    (successfulResponse) -> {
                        try {
                            JSONArray data = successfulResponse.getJSONArray("data");
                            int dataLength = data.length();
                            for (int i = 0; i < dataLength; i++) {
                                JSONObject thisObj = data.getJSONObject(i);
                                JSONObject arrival = thisObj.getJSONObject("arrival");
                                iataCodeArrival = arrival.getString("iata");
                                destination = arrival.getString("airport");
                                JSONObject departure = thisObj.getJSONObject("departure");
                                terminal = departure.getString("terminal");
                                gate = departure.getString("gate");

                                // add the code to handle delay
                                Object delayObject = departure.opt("delay");
                                if (delayObject == null) {
                                    delay = "N/A";
                                } else if (delayObject instanceof Integer) {
                                    int delayInteger = (int) delayObject;
                                    delay = String.valueOf(delayInteger) + " min";
                                }
                                flightlist.add(new Flight(destination, iataCodeArrival, terminal, gate, delay));
                                myAdapter.notifyItemInserted(flightlist.size() - 1);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    (errorResponse) -> {
                        int i = 0;
                    });
            queue.add(request);

            // Save the entered IATA code to SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("iataCode", binding.inputCode.getText().toString());
            editor.apply();

            binding.inputCode.setText("");
        });

        // Set up the RecyclerView with the adapter
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<FlightViewHolder>() {
            /**
             * Called when RecyclerView needs a new {@link FlightViewHolder} instance to represent an item.
             *
             * @param parent The ViewGroup into which the new View will be added after it is bound to
             *               an adapter position.
             * @param viewType The view type of the new View.
             * @return A new ViewHolder that holds a View of the given view type.
             */
            @NonNull
            @Override
            public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Inflate the layout using the ViewBinding and create a new FlightViewHolder
                ActivityFlightListBinding binding = ActivityFlightListBinding.inflate(getLayoutInflater());
                return new FlightViewHolder(binding.getRoot());
            }


            /**
             * Called by RecyclerView to display the data at the specified position.
             *
             * @param holder The ViewHolder which should be updated to represent the contents of the item
             *               at the given position in the data set.
             * @param position The position of the item within the adapter's data set.
             */
            @Override
            public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
                // Retrieve the Flight object at the specified position from the flightlist
                Flight flight = flightlist.get(position);

                // Update the ViewHolder to represent the contents of the Flight object
                holder.destinationText.setText(flight.getDestination());
                holder.terminalText.setText(flight.getTerminal());
                holder.gateText.setText(flight.getGate());
                holder.delayText.setText(flight.getDelay());
            }


            /**
             * Returns the total number of items in the data set held by the adapter.
             * @return The total number of items in the data set.
             */
            @Override
            public int getItemCount() {
                // Return the size of the flightlist, which represents the total number of items in the data set
                return flightlist.size();
            }

        });
    }

    /**
     * ViewHolder class for the RecyclerView items.
     * Represents a single flight item view in the list.
     */
    class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView destinationText;
        TextView terminalText;
        TextView delayText;
        TextView gateText;

        /**
         * Constructor for the FlightViewHolder.
         *
         * @param itemView The View for a single flight item in the RecyclerView.
         */
        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);

            // Set up the click listener for the flight item
            itemView.setOnClickListener(clk -> {
                // Get the absolute adapter position of the clicked item
                int position = getAbsoluteAdapterPosition();

                // Retrieve the Flight object at the clicked position from the flightlist
                Flight selected = flightlist.get(position);

                // Set the selected flight as the value of selectedFlight LiveData in flightModel
                flightModel.selectedFlight.postValue(selected);
            });

            // Initialize TextViews with their corresponding views in the item layout
            destinationText = itemView.findViewById(R.id.destinationTextView);
            terminalText = itemView.findViewById(R.id.terminalTextView);
            gateText = itemView.findViewById(R.id.gateTextView);
            delayText = itemView.findViewById(R.id.delayTextView);
        }
    }


    /**
     * Callback method invoked when a flight is deleted from the database or the user dismisses
     * the FlightDetailsFragment without saving the flight.
     *
     * @param flight The Flight object that has been deleted or dismissed.
     */
    @Override
    public void onFlightDeleted(Flight flight) {
        // Remove the deleted flight from the flightlist and update the RecyclerView
        flightlist.remove(flight);
        myAdapter.notifyDataSetChanged();
    }


    /**
     * Callback method invoked when a flight is saved to the database from the FlightDetailsFragment.
     * This method is called after the user confirms saving the flight.
     *
     * @param flight The Flight object that has been saved to the database.
     */
    @Override
    public void onFlightSaved(Flight flight) {
        // Save the flight to the flightlist and update the RecyclerView
        flightlist.add(flight);
        myAdapter.notifyDataSetChanged();
    }


    /**
     * Check if a flight is saved in the local database.
     *
     * @param flight The Flight object to check.
     * @return True if the flight is saved in the database, false otherwise.
     */
    private boolean isFlightSavedInDatabase(Flight flight) {
        // Get the ID of the provided Flight object
        long selectedId = flight.getId();

        // Use the provided ExecutorService to execute the database query in a separate thread
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executorService.submit(() -> {
            // Loop through all saved flights in the database
            for (Flight savedFlight : flightDAO.getAllFlights()) {
                // If the ID of the saved flight matches the ID of the provided flight, return true
                if (savedFlight.getId() == selectedId) {
                    return true;
                }
            }
            // If no match is found, return false
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


    /**
     * Initialize the contents of the Activity's standard options menu.
     *
     * @param menu The options menu in which the menu items are placed.
     * @return true for the menu to be displayed; false if it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu resource file (my_menu.xml) to create menu items
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }


    /**
     * Callback method to handle menu item selection from the options menu.
     *
     * @param item The selected MenuItem from the options menu.
     * @return true if the menu item is successfully handled, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Check the ID of the selected MenuItem and perform corresponding actions
        if (item.getItemId() == R.id.menu_flight) {
            // Open the AviationTracker activity from the menu
            startActivity(new Intent(this, MainActivity.class));
        } else if (item.getItemId() == R.id.menu_help) {
            // Show the help message in an AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(AviationTracker.this)
                    .setMessage(R.string.aviation_help_message)
                    .setTitle("Instructions!")
                    .setPositiveButton("OK", (cl, which) -> {
                        // Code to handle "OK" button click
                    });
            builder.create().show();
        }

        // Return true to indicate that the menu item selection is successfully handled
        return true;
    }

}

