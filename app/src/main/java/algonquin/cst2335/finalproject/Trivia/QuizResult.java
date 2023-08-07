package algonquin.cst2335.finalproject.Trivia;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.DecimalFormat;

/**
 * The QuizResult class represents a user's quiz result, including their username, score, and percentage.
 */
@Entity(tableName = "quiz_results")
public class QuizResult {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String username;
    private int score;
    private double percentage;

    /**
     * Constructs a QuizResult object with the provided username, score, and percentage.
     *
     * @param username   The username of the user who took the quiz.
     * @param score      The score achieved by the user.
     * @param percentage The percentage score achieved by the user.
     */
    public QuizResult(String username, int score, double percentage) {
        this.username = username;
        this.score = score;
        this.percentage = percentage;
    }

    /**
     * Gets the unique identifier of the quiz result.
     *
     * @return The unique identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the quiz result.
     *
     * @param id The unique identifier to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the username of the user who took the quiz.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the score achieved by the user.
     *
     * @return The user's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the percentage score achieved by the user, formatted to two decimal places.
     *
     * @return The formatted percentage score.
     */
    public double getPercentage() {
        // Format the percentage output to 2 decimal places.
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String formattedPercentage = decimalFormat.format(percentage);
        return Double.parseDouble(formattedPercentage);
    }
}
