package algonquin.cst2335.finalproject.data;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class FlightViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Flight>> flights = new MutableLiveData<>();
    public static  MutableLiveData<Flight> selectedFlight = new MutableLiveData< >();
}
