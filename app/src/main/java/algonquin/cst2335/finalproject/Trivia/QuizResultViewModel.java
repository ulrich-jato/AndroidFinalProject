package algonquin.cst2335.finalproject.Trivia;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * The QuizResultViewModel class serves as an interface between the UI (Activity) and the data repository (QuizResultRepository).
 * It holds and manages the data that is required for the UI to display and interact with.
 */
public class QuizResultViewModel extends AndroidViewModel {

    private QuizResultRepository repository;
    private LiveData<List<QuizResult>> allQuizResults;

    /**
     * Constructs a QuizResultViewModel instance.
     *
     * @param application The application instance.
     */
    public QuizResultViewModel(@NonNull Application application) {
        super(application);
        repository = new QuizResultRepository(application);
        allQuizResults = repository.getAllQuizResultsLiveData();
    }

    /**
     * Retrieves a LiveData object containing a list of all quiz results from the repository.
     * LiveData allows for automatic updates when the data changes.
     *
     * @return A LiveData object containing a list of quiz results.
     */
    public LiveData<List<QuizResult>> getAllQuizResults() {
        return allQuizResults;
    }
}
