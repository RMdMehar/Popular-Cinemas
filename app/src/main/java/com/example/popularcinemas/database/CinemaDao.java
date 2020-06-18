package com.example.popularcinemas.database;

import com.example.popularcinemas.model.Cinema;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface CinemaDao {

    @Query("SELECT * FROM favourite_cinemas")
    LiveData<List<Cinema>> loadAllFavouriteCinemas();

    @Insert
    void insertFavouriteCinema(Cinema cinema);

    @Delete
    void deleteFavouriteCinema(Cinema cinema);

    @Query("SELECT * FROM favourite_cinemas WHERE mCinemaId = :id")
    Cinema loadCinemaById(int id);
}
