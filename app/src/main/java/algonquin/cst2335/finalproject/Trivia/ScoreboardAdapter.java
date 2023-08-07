package algonquin.cst2335.finalproject.Trivia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import algonquin.cst2335.finalproject.R;

/**
 * The ScoreboardAdapter class is a RecyclerView adapter that binds quiz result data to the scoreboard list items.
 */
public class ScoreboardAdapter extends RecyclerView.Adapter<ScoreboardAdapter.ScoreboardViewHolder> {

    private List<QuizResult> quizResults;
    private OnItemClickListener listener;
    private TriviaRecycleClick recyclerViewClickInterface;

    /**
     * Constructs a ScoreboardAdapter instance.
     *
     * @param quizResults               The list of quiz results to display.
     * @param recyclerViewClickInterface The interface for handling click events on RecyclerView items.
     */
    public ScoreboardAdapter(List<QuizResult> quizResults, TriviaRecycleClick recyclerViewClickInterface) {
        this.quizResults = quizResults;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    /**
     * Sets an item click listener for the adapter.
     *
     * @param listener The item click listener.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Updates the adapter's data with new quiz results.
     *
     * @param newQuizResults The new quiz results to display.
     */
    public void updateData(List<QuizResult> newQuizResults) {
        quizResults.clear();
        quizResults.addAll(newQuizResults);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScoreboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_scoreboard, parent, false);
        return new ScoreboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreboardViewHolder holder, int position) {
        QuizResult quizResult = quizResults.get(position);
        holder.usernameText.setText(quizResult.getUsername());
        holder.percentageText.setText(quizResult.getPercentage() + "%");

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(quizResults.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizResults.size();
    }

    /**
     * The ScoreboardViewHolder class represents individual view items in the scoreboard list.
     */
    public class ScoreboardViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;
        TextView percentageText;

        public ScoreboardViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.usernameText);
            percentageText = itemView.findViewById(R.id.percentageText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickInterface.onItemClick(getAbsoluteAdapterPosition());
                }
            });

            itemView.setOnLongClickListener((view) -> {
                recyclerViewClickInterface.onLongClick(getAbsoluteAdapterPosition());
                return true;
            });
        }
    }

    /**
     * Retrieves the quiz result at the specified position.
     *
     * @param position The position of the quiz result.
     * @return The quiz result at the specified position.
     */
    public QuizResult getItem(int position) {
        return quizResults.get(position);
    }

    /**
     * Adds a quiz result to the specified position.
     *
     * @param position   The position to add the quiz result.
     * @param quizResult The quiz result to add.
     */
    public void addItem(int position, QuizResult quizResult) {
        quizResults.add(position, quizResult);
        notifyItemInserted(position);
    }

    /**
     * Removes a quiz result from the specified position.
     *
     * @param position The position to remove the quiz result.
     */
    public void removeItem(int position) {
        quizResults.remove(position);
        notifyItemRemoved(position);
    }
}

/**
 * The OnItemClickListener interface defines a callback method for handling item click events.
 */
interface OnItemClickListener {
    /**
     * Called when an item is clicked.
     *
     * @param quizResult The clicked quiz result.
     */
    void onItemClick(QuizResult quizResult);
}
