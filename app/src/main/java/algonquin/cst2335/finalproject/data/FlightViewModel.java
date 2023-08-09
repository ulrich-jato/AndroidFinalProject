package algonquin.cst2335.finalproject.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * ViewModel class for managing flight-related data.
 * This class extends the ViewModel class provided by the Android architecture components,
 * which allows data to be preserved during configuration changes.
 *
 * @author Jato Ulrich Guiffo Kengne
 */
public class FlightViewModel extends ViewModel {

    /**
     * MutableLiveData representing a list of flights.
     * The ViewModel stores and updates this list, and the UI can observe it for changes.
     */
    public MutableLiveData<ArrayList<Flight>> flights = new MutableLiveData<>();

    /**
     * MutableLiveData representing the selected flight.
     * The ViewModel stores and updates the selected flight, and the UI can observe it for changes.
     */
    public static MutableLiveData<Flight> selectedFlight = new MutableLiveData<>();

}

