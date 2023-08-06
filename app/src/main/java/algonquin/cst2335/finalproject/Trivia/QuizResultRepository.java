package algonquin.cst2335.finalproject.Trivia;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class QuizResultRepository {

    private static QuizResultRepository instance;
    private QuizResultDao quizResultDao;

    public QuizResultRepository(Context context) {
        QuizResultDatabase db = QuizResultDatabase.getInstance(context);
        quizResultDao = db.quizResultDao();
    }

    public static QuizResultRepository getInstance(Context context) {
        if (instance == null) {
            instance = new QuizResultRepository(context);
        }
        return instance;
    }

    public List<QuizResult> getAllQuizResults() {
        return quizResultDao.getAllQuizResults();
    }

    public void insertQuizResult(QuizResult quizResult) {
        quizResultDao.insertQuizResult(quizResult);
    }

    public LiveData<List<QuizResult>> getAllQuizResultsLiveData() {
        return quizResultDao.getAllQuizResultsLiveData();
    }

    public void deleteQuizResult(QuizResult quizResult){
        QuizResultDatabase.databaseWriteExecutor.execute(() ->{
            quizResultDao.deleteMessage(quizResult);
        });
    }
}
