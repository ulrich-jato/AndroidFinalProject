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

    public ScoreboardAdapter(List<QuizResult> quizResults) {
        this.quizResults = quizResults;
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
        holder.usernameText.setText("Username: " + quizResult.getUsername());
        holder.percentageText.setText("Percentage: " + quizResult.getPercentage() + "%");
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
        }
    }
}
