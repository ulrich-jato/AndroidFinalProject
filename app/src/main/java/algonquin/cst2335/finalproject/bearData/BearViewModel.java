/**
 * This class represents the ViewModel for the BearData application.
 * It holds the LiveData instances for managing image data and selected image.
 */
package algonquin.cst2335.finalproject.bearData;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class BearViewModel extends ViewModel {

    /**
     * LiveData instance holding an ArrayList of ImageEntity objects.
     * This LiveData is used to manage the list of images in the application.
     */
    public MutableLiveData<ArrayList<ImageEntity>> imageList = new MutableLiveData<>();

    /**
     * LiveData instance holding the currently selected ImageEntity object.
     * This LiveData is used to manage the selected image in the application.
     */
    public static MutableLiveData<ImageEntity> selectedImage = new MutableLiveData<>();
}
