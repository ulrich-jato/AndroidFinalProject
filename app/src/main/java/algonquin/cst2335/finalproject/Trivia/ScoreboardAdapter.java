package algonquin.cst2335.finalproject.Trivia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import algonquin.cst2335.finalproject.R;

public class ScoreboardAdapter extends RecyclerView.Adapter<ScoreboardAdapter.ScoreboardViewHolder> {

    private List<QuizResult> quizResults;
    private OnItemClickListener listener;
    private TriviaRecycleClick recyclerViewClickInterface;

    public ScoreboardAdapter(List<QuizResult> quizResults, TriviaRecycleClick recyclerViewClickInterface) {
        this.quizResults = quizResults;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

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
//
//        holder.bind(quizResult);

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


}
 interface OnItemClickListener {
    void onItemClick(QuizResult quizResult);
}
