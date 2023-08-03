package algonquin.cst2335.finalproject.Trivia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {QuizResult.class}, version = 2, exportSchema = false)
public abstract class QuizResultDatabase extends RoomDatabase {

    public abstract QuizResultDao quizResultDao();

    private static volatile QuizResultDatabase INSTANCE;

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
