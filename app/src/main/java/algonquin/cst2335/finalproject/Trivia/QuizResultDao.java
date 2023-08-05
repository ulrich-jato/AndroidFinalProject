package algonquin.cst2335.finalproject.Trivia;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuizResultDao {

    @Insert
    void insertQuizResult(QuizResult quizResult);

    @Query("SELECT * FROM quiz_results ORDER BY percentage DESC")
    List<QuizResult> getAllQuizResults();

    @Query("SELECT * FROM quiz_results ORDER BY percentage DESC")
    LiveData<List<QuizResult>> getAllQuizResultsLiveData();

    @Delete
    void deleteMessage(QuizResult q);
}
