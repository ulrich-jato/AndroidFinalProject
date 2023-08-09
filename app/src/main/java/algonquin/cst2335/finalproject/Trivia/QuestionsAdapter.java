package algonquin.cst2335.finalproject.Trivia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import algonquin.cst2335.finalproject.R;

/**
 * The QuestionsAdapter class is responsible for populating a RecyclerView with trivia questions.
 */
public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {

    private List<Question> questions;
    private int score = 0;

    /**
     * Constructs an empty QuestionsAdapter.
     */
    public QuestionsAdapter() {
    }

    /**
     * Constructs a QuestionsAdapter with a list of questions.
     *
     * @param questions The list of trivia questions.
     */
    public QuestionsAdapter(List<Question> questions) {
        this.questions = questions;
    }

    /**
     * Creates a new QuestionViewHolder when needed by the RecyclerView.
     *
     * @param parent   The parent view group that the new view will be attached to.
     * @param viewType The view type of the new view.
     * @return A new QuestionViewHolder that holds the view for a question item.
     */
    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_option, parent, false);
        return new QuestionViewHolder(view);
    }

    /**
     * Binds data to the QuestionViewHolder and handles user interaction.
     *
     * @param holder   The QuestionViewHolder to bind data to.
     * @param position The position of the item within the data set.
     */
    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questions.get(position);
        String questionNumber = String.valueOf(position + 1);
        String questionText = questionNumber + ": " + question.getQuestion();
        holder.questionText.setText(questionText);

        // Clear the previous options
        holder.optionsRadioGroup.removeAllViews();

        // Add options to the RadioGroup
        for (int i = 0; i < question.getOptions().size(); i++) {
            String option = question.getOptions().get(i);
            RadioButton radioButton = new RadioButton(holder.itemView.getContext());
            radioButton.setId(i);
            radioButton.setText(option);
            holder.optionsRadioGroup.addView(radioButton);
        }

        // Handle option selection and scoring
        holder.optionsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String selectedOption = question.getOptions().get(checkedId);
            if (selectedOption.equals(question.getCorrectAnswer())) {
                // Increment the score
                score++;
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return questions.size();
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
     * The QuestionViewHolder class represents a ViewHolder for a trivia question item in the RecyclerView.
     */
    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        public TextView questionText;
        public RadioGroup optionsRadioGroup;

        /**
         * Constructs a QuestionViewHolder with the provided itemView.
         *
         * @param itemView The view representing a trivia question item.
         */
        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.questionText);
            optionsRadioGroup = itemView.findViewById(R.id.optionRadioButton);
        }
    }
}
