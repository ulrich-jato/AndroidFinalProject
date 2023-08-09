package algonquin.cst2335.finalproject.Currency;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
/**
 * CurrencyViewModel is a ViewModel class that holds and manages the data related to currency conversions.
 * It uses LiveData to observe changes in the data and notify the observers (usually, the UI components) when the data changes.
 * This ViewModel class is used to share and retain data between the CurrencyGenerator activity and its fragments.
 */
public class CurrencyViewModel extends ViewModel {

    /**
     * LiveData that holds the list of currency conversions.
     * It is MutableLiveData, which means it can be modified.
     */
    public MutableLiveData<ArrayList<CurrencyObject>> currency = new MutableLiveData<>();

    /**
     * LiveData that holds the selected currency conversion.
     * It is MutableLiveData, which means it can be modified.
     */
    public MutableLiveData<CurrencyObject> selectedMessage = new MutableLiveData<>();
}
