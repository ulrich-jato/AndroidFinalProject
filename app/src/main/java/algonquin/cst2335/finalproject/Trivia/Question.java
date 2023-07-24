package algonquin.cst2335.finalproject.Trivia;

import java.util.List;

public class Question {
    private String question;
    private List<String> options;
    private String correctAnswer;

    public Question(String q, List o, String a){
        question = q;
        options = o;
        correctAnswer = a;
    }

    public String getQuestion(){
        return question;
    }
    public String getCorrectAnswer(){
        return correctAnswer;
    }

    public List<String> getOptions() {
        return options;
    }
}
