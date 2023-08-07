package algonquin.cst2335.finalproject.data;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.FlightDetailsBinding;

/**
 * A Fragment class that displays flight details and allows the user to save or delete the flight.
 * Implements the functionality for saving, deleting, and undoing flight details.
 *
 * @author  Jato Ulrich Guiffo Kengne
 */
public class FlightDetailsFragment extends Fragment {

    /**
     * Represents the selected flight whose details are being displayed.
     */
    Flight selected;

    /**
     * The Data Access Object (DAO) for interacting with the Flight database.
     */
    FlightDAO flightDAO;

    /**
     * Indicates whether the selected flight is already saved in the database or not.
     */
    private boolean isSavedFlight;


    /**
     * Constructor for the FlightDetailsFragment.
     *
     * @param flight       The Flight object representing the flight details to display.
     * @param isSavedFlight True if the flight is already saved in the database, false otherwise.
     */
    public FlightDetailsFragment(Flight flight, boolean isSavedFlight) {
        this.selected = flight;
        this.isSavedFlight = isSavedFlight;
    }

    /**
     * Interface to define callbacks for saving or deleting a flight.
     */
    public interface OnFlightDetailsListener {
        /**
         * Callback method when a flight is saved to the database.
         *
         * @param flight The Flight object representing the saved flight.
         */
        void onFlightSaved(Flight flight);

        /**
         * Callback method when a flight is deleted from the database.
         *
         * @param flight The Flight object representing the deleted flight.
         */
        void onFlightDeleted(Flight flight);
    }

    private OnFlightDetailsListener flightDetailsListener;

    /**
     * Sets the listener for flight details events.
     *
     * @param listener The OnFlightDetailsListener to be set as the listener.
     */
    public void setOnFlightDetailsListener(OnFlightDetailsListener listener) {
        this.flightDetailsListener = listener;
    }

    /**
     * Called when creating the View for the FlightDetailsFragment.
     *
     * @param inflater           The LayoutInflater used to inflate the layout for the fragment.
     * @param container          The parent view that the fragment's UI will be attached to.
     * @param savedInstanceState The saved instance state of the fragment.
     * @return The View for the FlightDetailsFragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout using ViewBinding
        FlightDetailsBinding binding = FlightDetailsBinding.inflate(inflater);

        // Save the selected flight details to the Room database
        FlightDatabase db = Room.databaseBuilder(requireContext().getApplicationContext(),
                FlightDatabase.class, "flightDatabase").build();
        flightDAO = db.flightDAO();

        // Set flight details in the UI
        binding.destinationText.setText(selected.getDestination());
        binding.terminalText.setText(selected.getTerminal());
        binding.gateText.setText(selected.getGate());
        binding.delayText.setText(selected.getDelay());
        binding.destIataCodeText.setText(selected.getIataCodeArrival());

        // Check if the flight is already saved, and show corresponding UI
        if (isSavedFlight) {
            binding.saveFlightDetailsButton.setText(R.string.delete_flight);
            binding.saveFlightDetailsButton.setOnClickListener(click -> {
                showDeleteFlightDialog();
            });
        } else {
            binding.saveFlightDetailsButton.setText(R.string.save_flight);
            binding.saveFlightDetailsButton.setOnClickListener(click -> {
                showSavedFlightDialog();
            });
        }

        binding.closeFragmentImageView.setOnClickListener(click ->{
            // Remove the fragment from the fragment container
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragmentLocation);

            if (currentFragment != null) {
                // Remove the current fragment
                fragmentManager.beginTransaction().remove(currentFragment).commit();
            }
        });

        return binding.getRoot();
    }



    /**
     * Shows a dialog to confirm saving the flight details to the database.
     */
    private void showSavedFlightDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(R.string.prompt_save_flight);
        builder.setTitle("Attention!");

        // Set the "No" button click listener to perform no action when clicked
        builder.setNegativeButton(R.string.No, (dialog, which) -> {
            // No action required when the user clicks "No"
        });

        // Set the "Yes" button click listener to trigger the saveFlightDetails() method
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            saveFlightDetails();
        });

        // Show the AlertDialog to the user
        builder.create().show();
    }


    /**
     * Save the flight details to the database.
     */
    private void saveFlightDetails() {
        // Check if the selected flight already exists in the database.
        long selectedId = selected.getId();
        // Create an Executor to run the database operation on a separate thread.
        Executor thread1 = Executors.newSingleThreadExecutor();
        thread1.execute(() -> {
            Flight existingFlight = flightDAO.getFlightById(selectedId);

            if (existingFlight != null) {
                // Flight already exists in the database, show a message to the user.
                requireActivity().runOnUiThread(() -> {
                    Snackbar.make(requireView(), R.string.flight_already_saved, Snackbar.LENGTH_SHORT).show();
                });
            } else {
                // Selected flight does not exist in the database, proceed with saving.
                long id = flightDAO.insertFlight(selected);
                selected.setId(id);

                // Make sure to update the UI components on the main thread.
                requireActivity().runOnUiThread(() -> {
                    if (id != -1) {
                        Snackbar.make(requireView(), R.string.flight_details_saved, Snackbar.LENGTH_LONG)
                                .setAction(R.string.undo, (snackbarClick) -> {
                                    undoSavedFlightDetails(selected);
                                })
                                .show();
                    } else {
                        Snackbar.make(requireView(), R.string.failed_to_save_flight, Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * Show the dialog to confirm deleting the flight.
     */
    private void showDeleteFlightDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(R.string.prompt_delete_flight);
        builder.setTitle("Attention!");
        builder.setNegativeButton(R.string.No, (dialog, which) -> {
            // No action required when the user clicks "No"
        });

        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            deleteFlightDetails();
        });
        builder.create().show();
    }


    /**
     * Deletes the selected flight details from the database. Displays a Snackbar with the option to undo the deletion.
     */
    private void deleteFlightDetails() {
        long selectedId = selected.getId();
        Executor thread1 = Executors.newSingleThreadExecutor();
        thread1.execute(() -> {
            // Check if the selected flight exists in the database
            Flight existingFlight = flightDAO.getFlightById(selectedId);

            if (existingFlight == null) {
                // The selected flight does not exist in the database
                requireActivity().runOnUiThread(() -> {
                    Snackbar.make(requireView(), R.string.flight_not_in_database, Snackbar.LENGTH_SHORT).show();
                });
            } else {
                // The selected flight exists in the database, proceed with deletion
                flightDAO.deleteFlight(selected);

                // Update the UI components on the main thread
                requireActivity().runOnUiThread(() -> {
                    // Notify the listener that the flight has been deleted
                    if (flightDetailsListener != null) {
                        flightDetailsListener.onFlightDeleted(selected);
                    }

                    // Show a Snackbar with an option to undo the deletion
                    Snackbar.make(requireView(), R.string.flight_details_deleted, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.undo, (snackbarClick) -> {
                                undoDeletedFlightDetails(selected);
                            })
                            .show();
                });
            }
        });
    }


    /**
     * Undo the saving of flight details by deleting the flight from the database.
     *
     * @param flight The Flight object representing the flight details to undo.
     */
    private void undoSavedFlightDetails(Flight flight) {
        // Create an Executor to run the database operation on a separate thread.
        Executor thread1 = Executors.newSingleThreadExecutor();

        // Perform the database operation on the separate thread.
        thread1.execute(() -> {
            flightDAO.deleteFlight(flight);

            // Make sure to update the UI components on the main thread.
            requireActivity().runOnUiThread(() -> {
                Snackbar.make(requireView(), R.string.flight_saving_cancelled, Snackbar.LENGTH_SHORT).show();
            });
        });
    }

    /**
     * Undo the deletion of flight details by saving the flight back to the database.
     *
     * @param flight The Flight object representing the flight details to undo.
     */
    private void undoDeletedFlightDetails(Flight flight) {
        // Create an Executor to run the database operation on a separate thread.
        Executor thread1 = Executors.newSingleThreadExecutor();

        // Perform the database operation on the separate thread.
        thread1.execute(() -> {
            //long id = flightDAO.insertFlight(flight);
            //flight.setId(id);
            flightDAO.insertFlight(flight);
            // Make sure to update the UI components on the main thread.
            requireActivity().runOnUiThread(() -> {
                if (flightDetailsListener != null) {
                    flightDetailsListener.onFlightSaved(selected);
                }
                Snackbar.make(requireView(), "Flight details undone!", Snackbar.LENGTH_SHORT).show();
            });
        });
    }
}
