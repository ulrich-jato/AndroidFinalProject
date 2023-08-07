package algonquin.cst2335.finalproject.Trivia;

/**
 * This interface defines callback methods for handling click and long-click events in a RecyclerView item.
 */
public interface TriviaRecycleClick {
    /**
     * Called when a RecyclerView item is clicked.
     *
     * @param position The position of the clicked item in the RecyclerView.
     */
    void onItemClick(int position);

    /**
     * Called when a RecyclerView item is long-clicked.
     *
     * @param position The position of the long-clicked item in the RecyclerView.
     */
    void onLongClick(int position);
}
