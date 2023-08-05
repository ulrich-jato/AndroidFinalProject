package algonquin.cst2335.finalproject.Trivia;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.DecimalFormat;

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

        //Format the percentage output to 2 decimal places.
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String formattedPercentage = decimalFormat.format(percentage);
        return Double.parseDouble(formattedPercentage);
    }
}
