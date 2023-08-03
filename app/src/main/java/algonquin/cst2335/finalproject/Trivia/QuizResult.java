package algonquin.cst2335.finalproject.Trivia;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz_results")
public class QuizResult {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String username;
    private int score;
    private double percentage;

    public QuizResult(String username, int score, double percentage) {
        this.username = username;
        this.score = score;
        this.percentage = percentage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }
    public double getPercentage(){
        return percentage;
    }
}
