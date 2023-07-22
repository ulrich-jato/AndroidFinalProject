package algonquin.cst2335.finalproject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CurrencyViewModel extends ViewModel {
    public MutableLiveData<ArrayList<CurrencyObject>> currency = new MutableLiveData<>();
    public MutableLiveData<CurrencyObject> selectedMessage = new MutableLiveData< >();

}
