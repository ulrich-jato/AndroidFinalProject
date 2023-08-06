package algonquin.cst2335.finalproject.Trivia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {QuizResult.class}, version = 2, exportSchema = false)
public abstract class QuizResultDatabase extends RoomDatabase {

    public abstract QuizResultDao quizResultDao();

    private static volatile QuizResultDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


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
