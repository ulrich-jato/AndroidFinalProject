package algonquin.cst2335.finalproject.Trivia;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class QuizResultViewModel extends AndroidViewModel {

    private QuizResultRepository repository;
    private LiveData<List<QuizResult>> allQuizResults;

    public QuizResultViewModel(@NonNull Application application) {
        super(application);
        repository = new QuizResultRepository(application);
        allQuizResults = (LiveData<List<QuizResult>>) repository.getAllQuizResults();
    }

    public LiveData<List<QuizResult>> getAllQuizResults() {
        return allQuizResults;
    }

}
