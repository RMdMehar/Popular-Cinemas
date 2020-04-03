package com.example.popularcinemas.database;

import java.util.ArrayList;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface CinemaDao {

    @Query("SELECT * FROM favourite_cinemas")
    ArrayList<Cinema> loadAllFavouriteCinemas();

    @Insert
    void insertFavouriteCinema(Cinema cinema);

    @Delete
    void deleteFavouriteCinema(Cinema cinema);
}
