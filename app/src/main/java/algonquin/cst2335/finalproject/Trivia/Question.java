package algonquin.cst2335.finalproject.Trivia;

import java.util.List;

/**
 * The Question class represents a trivia question with its options and correct answer.
 */
public class Question {
    private String question;
    private List<String> options;
    private String correctAnswer;

    /**
     * Constructs a Question object with the specified question, options, and correct answer.
     *
     * @param q The question text.
     * @param o The list of options for the question.
     * @param a The correct answer for the question.
     */
    public Question(String q, List<String> o, String a) {
        question = q;
        options = o;
        correctAnswer = a;
    }

    /**
     * Gets the text of the question.
     *
     * @return The question text.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Gets the correct answer for the question.
     *
     * @return The correct answer.
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Gets the list of options for the question.
     *
     * @return The list of options.
     */
    public List<String> getOptions() {
        return options;
    }
}
