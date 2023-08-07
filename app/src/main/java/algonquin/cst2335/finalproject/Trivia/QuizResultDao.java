package algonquin.cst2335.finalproject.Trivia;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * The QuizResultDao interface defines the data access methods for interacting with the quiz results database.
 */
@Dao
public interface QuizResultDao {

    /**
     * Inserts a new quiz result into the database.
     *
     * @param quizResult The quiz result to insert.
     */
    @Insert
    void insertQuizResult(QuizResult quizResult);

    /**
     * Retrieves a list of all quiz results from the database, ordered by percentage in descending order.
     *
     * @return A list of quiz results.
     */
    @Query("SELECT * FROM quiz_results ORDER BY percentage DESC")
    List<QuizResult> getAllQuizResults();

    /**
     * Retrieves a LiveData object containing a list of all quiz results from the database, ordered by percentage
     * in descending order. LiveData allows for automatic updates when the data changes.
     *
     * @return A LiveData object containing a list of quiz results.
     */
    @Query("SELECT * FROM quiz_results ORDER BY percentage DESC")
    LiveData<List<QuizResult>> getAllQuizResultsLiveData();

    /**
     * Deletes a quiz result from the database.
     *
     * @param q The quiz result to delete.
     */
    @Delete
    void deleteMessage(QuizResult q);
}
