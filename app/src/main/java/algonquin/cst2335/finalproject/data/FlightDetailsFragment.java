package algonquin.cst2335.finalproject.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.databinding.DetailsLayoutBinding;

public class FlightDetailsFragment extends Fragment {

    Flight selected;

    public FlightDetailsFragment(Flight  flight) {
        selected = flight;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);

        binding.destinationText.setText(selected.getDestination());
        binding.terminalText.setText(selected.getTerminal());
        binding.gateText.setText(selected.getGate());
        binding.delayText.setText(selected.getDelay());

        binding.saveFlightDetailsButton.setOnClickListener(click ->{

        });
        return binding.getRoot();
    }

}
