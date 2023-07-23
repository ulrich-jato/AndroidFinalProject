package algonquin.cst2335.finalproject.data;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.DetailsLayoutBinding;

public class FlightDetailsFragment extends Fragment {

    Flight selected;
    FlightDAO flightDAO;

    public FlightDetailsFragment(Flight  flight) {
        selected = flight;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);
        //Save the selected flight details to the Room database
        FlightDatabase db = Room.databaseBuilder(requireContext().getApplicationContext(),
                FlightDatabase.class, "flightDatabase").build();

        // Get the FlightDAO from the database.
        flightDAO = db.flightDAO();
        binding.destinationText.setText(selected.getDestination());
        binding.terminalText.setText(selected.getTerminal());
        binding.gateText.setText(selected.getGate());
        binding.delayText.setText(selected.getDelay());

        binding.saveFlightDetailsButton.setOnClickListener(click ->{
            showSavedFlightDialog();
        });
        return binding.getRoot();
    }

    private void showSavedFlightDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Do you Want to save this Flight ?");
        builder.setTitle("Attention!");
        builder.setNegativeButton("No", (dialog, which) ->{

        });

        builder.setPositiveButton("Yes", (dialog, which) ->{
            saveFlightDetails();
        });
        builder.create().show();
    }

    private void saveFlightDetails(){
        // Check if the selected flight already exists in the database.
        long selectedId = selected.getId();
        // Create an Executor to run the database operation on a separate thread.
        Executor thread1 = Executors.newSingleThreadExecutor();
        thread1.execute(() -> {
            Flight existingFlight = flightDAO.getFlightById(selectedId);

            if (existingFlight != null) {
                // Flight already exists in the database, show a message to the user.
                requireActivity().runOnUiThread(() -> {
                    Snackbar.make(requireView(), "This Flight has already been saved!", Snackbar.LENGTH_SHORT).show();
                });
            } else {
                // Selected flight does not exist in the database, proceed with saving.
                long id = flightDAO.insertFlight(selected);
                selected.setId(id);

                // Make sure to update the UI components on the main thread.
                requireActivity().runOnUiThread(() -> {
                    if (id != -1) {
                        Snackbar.make(requireView(), "Flight details saved!", Snackbar.LENGTH_LONG)
                                .setAction("Undo", (snackbarClick) -> {
                                    undoSavedFlightDetails(selected);
                                })
                                .show();
                    } else {
                        Snackbar.make(requireView(), "Failed to save flight details!", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void undoSavedFlightDetails(Flight flight){
        // Create an Executor to run the database operation on a separate thread.
        Executor thread1 = Executors.newSingleThreadExecutor();

        // Perform the database operation on the separate thread.
        thread1.execute(() -> {
            flightDAO.deleteFlight(flight);

            // Make sure to update the UI components on the main thread.
            requireActivity().runOnUiThread(() -> {
                Snackbar.make(requireView(), "Flight details undone!", Snackbar.LENGTH_SHORT).show();
            });

        });
    }
}
