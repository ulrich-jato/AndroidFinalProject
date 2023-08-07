package algonquin.cst2335.finalproject.Trivia;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * The QuizResultRepository class acts as an intermediary between the data source (database) and the ViewModel,
 * providing methods to interact with quiz results.
 */
public class QuizResultRepository {

    private static QuizResultRepository instance;
    private QuizResultDao quizResultDao;

    /**
     * Constructs a QuizResultRepository instance.
     *
     * @param context The application context.
     */
    public QuizResultRepository(Context context) {
        QuizResultDatabase db = QuizResultDatabase.getInstance(context);
        quizResultDao = db.quizResultDao();
    }

    /**
     * Returns an instance of QuizResultRepository using the singleton pattern.
     *
     * @param context The application context.
     * @return An instance of QuizResultRepository.
     */
    public static QuizResultRepository getInstance(Context context) {
        if (instance == null) {
            instance = new QuizResultRepository(context);
        }
        return instance;
    }

    /**
     * Retrieves a list of all quiz results from the database.
     *
     * @return A list of quiz results.
     */
    public List<QuizResult> getAllQuizResults() {
        return quizResultDao.getAllQuizResults();
    }

    /**
     * Inserts a new quiz result into the database.
     *
     * @param quizResult The quiz result to insert.
     */
    public void insertQuizResult(QuizResult quizResult) {
        quizResultDao.insertQuizResult(quizResult);
    }

    /**
     * Retrieves a LiveData object containing a list of all quiz results from the database.
     * LiveData allows for automatic updates when the data changes.
     *
     * @return A LiveData object containing a list of quiz results.
     */
    public LiveData<List<QuizResult>> getAllQuizResultsLiveData() {
        return quizResultDao.getAllQuizResultsLiveData();
    }

    /**
     * Deletes a quiz result from the database.
     *
     * @param quizResult The quiz result to delete.
     */
    public void deleteQuizResult(QuizResult quizResult) {
        QuizResultDatabase.databaseWriteExecutor.execute(() -> {
            quizResultDao.deleteMessage(quizResult);
        });
    }
}
