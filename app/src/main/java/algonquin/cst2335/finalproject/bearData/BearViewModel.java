package algonquin.cst2335.finalproject.bearData;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class BearViewModel extends ViewModel {
    public MutableLiveData<ArrayList<ImageEntity>> imageList = new MutableLiveData<>();
}
