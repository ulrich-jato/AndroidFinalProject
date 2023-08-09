package algonquin.cst2335.finalproject.Currency;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.CurrencySavedBinding;

/**
 * CurrencyDetailsFragment is a fragment that displays the details of a selected currency conversion.
 * It shows the converted currency values along with the "Save" button to save the conversion to the database.
 * The fragment provides functionality to save the conversion data to the Room database and displays
 * appropriate Toast messages for success or failure. It also provides functionality to delete the saved
 * conversion from the database.
 */
public class CurrencyDetailsFragment extends Fragment {
    /**
     * CurrencyObject representing the selected currency conversion to be displayed in the fragment.
     */
    private CurrencyObject selected;
    /**
     * Empty public constructor for CurrencyDetailsFragment.
     * This constructor is required when using fragments.
     */
    public CurrencyDetailsFragment() {
        // Required empty public constructor
    }
    /**
     * Constructor for CurrencyDetailsFragment that sets the selected currency conversion.
     *
     * @param m The CurrencyObject representing the selected currency conversion.
     */
    public CurrencyDetailsFragment(CurrencyObject m) {
        selected = m;
    }

    /**
     * Called when the fragment should create its view hierarchy.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        CurrencySavedBinding binding = CurrencySavedBinding.inflate(inflater);


        binding.converttotext.setText(selected.converto);
        binding.converttext.setText(selected.convertfrom);
        binding.amountf.setText(selected.cfrom);
        binding.amountt.setText(selected.too);


        binding.button.setOnClickListener(v -> saveConversionToDatabase());
        return binding.getRoot();
    }
    /**
     * Saves the selected currency conversion to the Room database in a background thread.
     * Displays appropriate Toast messages for success or failure.
     */
    private void saveConversionToDatabase() {
        CurrencyObject newConversion = new CurrencyObject(selected.convertfrom, selected.converto, selected.cfrom, selected.too);
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            CurrencyDao myDAO = CurrencyDatabase.getInstance(requireContext()).cmDAO();
            List<CurrencyObject> allConversions = myDAO.getMessages();
            for (CurrencyObject conversion : allConversions) {
                if (conversion.getToo().equals(newConversion.getToo())) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), R.string.csaved, Toast.LENGTH_SHORT).show());
                    return;
                }
            }
            long insertedId = myDAO.insertConvertTo(newConversion);
            if (insertedId != -1) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), R.string.saved, Toast.LENGTH_SHORT).show());
            } else {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Failed to save conversion", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
