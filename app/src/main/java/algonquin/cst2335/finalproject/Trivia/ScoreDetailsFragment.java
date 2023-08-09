package algonquin.cst2335.finalproject.Trivia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.databinding.DetailsLayoutBinding;

/**
 * The ScoreDetailsFragment displays detailed information about a quiz result.
 * It extends the Fragment class and overrides the onCreateView method to inflate and display the layout.
 */
public class ScoreDetailsFragment extends Fragment {

    private QuizResult selected;

    /**
     * Constructs a ScoreDetailsFragment instance.
     *
     * @param q The QuizResult object containing the selected quiz result data.
     */
    public ScoreDetailsFragment(QuizResult q) {
        selected = q;
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);
        binding.userText.setText("User: " + selected.getUsername());
        binding.sizeText.setText(String.valueOf("Correct answers: " + selected.getScore()));
        binding.outcomeText.setText(String.valueOf("Percentage score: " + selected.getPercentage()));

        return binding.getRoot();
    }
}
