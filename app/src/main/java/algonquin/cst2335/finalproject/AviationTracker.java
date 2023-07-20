package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import algonquin.cst2335.finalproject.data.Flight;
import algonquin.cst2335.finalproject.data.FlightViewModel;
import algonquin.cst2335.finalproject.databinding.ActivityAviationTrackerBinding;
import algonquin.cst2335.finalproject.databinding.ActivityFlightListBinding;

public class AviationTracker extends AppCompatActivity {
    protected RequestQueue queue = null;
    protected String delay;
    protected String destination;
    protected String iataCodeArrival;
    protected String terminal;
    protected String gate;
    ActivityAviationTrackerBinding binding;
    RecyclerView.Adapter<FlightViewHolder> myAdapter;
    ArrayList<Flight> flightlist = new ArrayList<>();
    FlightViewModel flightModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);
        binding = ActivityAviationTrackerBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

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

        binding.searchFlightButton.setOnClickListener(click -> {
            flightlist.clear();
            myAdapter.notifyDataSetChanged();
            String inputCode = binding.inputCode.getText().toString().toUpperCase().trim();
            String stringUrl = "http://api.aviationstack.com/v1/flights?access_key=092a0043b5a7a8a889ee42d27bc5e044&dep_iata="+ inputCode;
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
        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<FlightViewHolder>() {
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
}

class FlightViewHolder extends RecyclerView.ViewHolder {
    TextView destinationText;
    TextView terminalText;
    TextView delayText;
    TextView gateText;
    public FlightViewHolder(@NonNull View itemView) {
        super(itemView);
        destinationText = itemView.findViewById(R.id.destinationTextView);
        terminalText = itemView.findViewById(R.id.terminalTextView);
        gateText = itemView.findViewById(R.id.gateTextView);
        delayText = itemView.findViewById(R.id.delayTextView);
    }
}