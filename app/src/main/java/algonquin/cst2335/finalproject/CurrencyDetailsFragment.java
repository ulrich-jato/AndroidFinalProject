package algonquin.cst2335.finalproject;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.databinding.CurrencySavedBinding;


public class CurrencyDetailsFragment extends Fragment {

    CurrencyObject selected;

    public CurrencyDetailsFragment(CurrencyObject m){
        selected = m;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        CurrencySavedBinding binding = CurrencySavedBinding.inflate(inflater);

//        c binding = DetailsLayoutBinding.inflate(inflater);

        binding.converText.setText(selected.converto);
        binding.convertfromText.setText(selected.convertfrom);
        binding.databaseText.setText("Id = " + selected.id);
        return binding.getRoot();
    }
}

