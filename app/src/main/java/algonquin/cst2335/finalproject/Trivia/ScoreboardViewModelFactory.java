package algonquin.cst2335.finalproject.Trivia;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * The ScoreboardViewModelFactory class is responsible for creating instances of the ScoreboardViewModel.
 * It implements the ViewModelProvider.Factory interface to provide a customized ViewModel creation process.
 */
public class ScoreboardViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    /**
     * Constructs a ScoreboardViewModelFactory instance.
     *
     * @param application The application context used for creating the ViewModel.
     */
    public ScoreboardViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ScoreboardActivity.ScoreboardViewModel.class)) {
            return (T) new ScoreboardActivity.ScoreboardViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
