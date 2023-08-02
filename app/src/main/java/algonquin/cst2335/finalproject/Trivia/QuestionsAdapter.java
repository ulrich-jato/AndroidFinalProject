package algonquin.cst2335.finalproject.Trivia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import algonquin.cst2335.finalproject.R;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {

    private List<Question> questions;

    public QuestionsAdapter(){};
    private int score = 0;

    public QuestionsAdapter(List<Question> questions) {

        this.questions = questions;
    }



    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_option, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questions.get(position);
        String questionNumber = String.valueOf(position + 1); // Adding 1 because position starts from 0
        String questionText = questionNumber + ": " + question.getQuestion();
        holder.questionText.setText(questionText);

        // Clear the previous options
        holder.optionsRadioGroup.removeAllViews();

        // Add options to the RadioGroup
        for (int i = 0; i < question.getOptions().size(); i++) {
            String option = question.getOptions().get(i);
//            RadioButton radioButton = (RadioButton) LayoutInflater.from(holder.itemView.getContext())
//                    .inflate(R.layout.list_item_option, holder.optionsRadioGroup, false);
            RadioButton radioButton = new RadioButton(holder.itemView.getContext());
            radioButton.setId(i);
            radioButton.setText(option);
            holder.optionsRadioGroup.addView(radioButton);
        }
        holder.optionsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String selectedOption = question.getOptions().get(checkedId);
                    if (selectedOption.equals(question.getCorrectAnswer())) {
                        // User selected the correct option
                        // Increment the score
                        score++;

                    }

        });

    }
    public int getScore(){
        return score;
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

    // ... (ViewHolder and other methods)

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        public TextView questionText;
        public RadioGroup optionsRadioGroup;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.questionText);
            optionsRadioGroup = itemView.findViewById(R.id.optionRadioButton);

            optionsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                // Code to handle the user's answer selection
                // You can use the 'checkedId' to get the selected option and handle it accordingly.

            });
        }
    }
}
