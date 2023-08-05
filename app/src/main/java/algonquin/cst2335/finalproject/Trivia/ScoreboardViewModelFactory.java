package algonquin.cst2335.finalproject.Trivia;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ScoreboardViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

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
