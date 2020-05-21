package com.example.popularcinemas.viewmodel;

import android.app.Application;
import android.util.Log;

import com.example.popularcinemas.database.AppDatabase;
import com.example.popularcinemas.model.Cinema;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {
    private static final String LOG_TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Cinema>> favouriteCinemas;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(LOG_TAG, "Custom Log: Actively retrieving favourite cinemas from the DataBase");
        favouriteCinemas = database.cinemaDao().loadAllFavouriteCinemas();
    }

    public LiveData<List<Cinema>> getFavouriteCinemas() {
        return favouriteCinemas;
    }
}
