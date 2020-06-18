package com.example.popularcinemas.viewmodel;

import android.app.Application;

import com.example.popularcinemas.database.AppDatabase;
import com.example.popularcinemas.model.Cinema;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Cinema>> favouriteCinemas;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        favouriteCinemas = database.cinemaDao().loadAllFavouriteCinemas();
    }

    public LiveData<List<Cinema>> getFavouriteCinemas() {
        return favouriteCinemas;
    }
}
