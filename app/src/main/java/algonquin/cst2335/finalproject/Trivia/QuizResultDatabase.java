package algonquin.cst2335.finalproject.Trivia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The QuizResultDatabase class represents the Room database for quiz results and provides access to its data and operations.
 */
@Database(entities = {QuizResult.class}, version = 2, exportSchema = false)
public abstract class QuizResultDatabase extends RoomDatabase {

    /**
     * Retrieves the DAO interface for quiz results.
     *
     * @return The QuizResultDao interface.
     */
    public abstract QuizResultDao quizResultDao();

    private static volatile QuizResultDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Returns an instance of the QuizResultDatabase using the singleton pattern.
     *
     * @param context The application context.
     * @return An instance of QuizResultDatabase.
     */
    public static QuizResultDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (QuizResultDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    QuizResultDatabase.class, "quiz_results_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
