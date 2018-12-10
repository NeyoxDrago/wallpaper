package app.sample.app.wallpaper.DatabaseFiles.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import app.sample.app.wallpaper.recents;
import io.reactivex.Flowable;

@Dao()
public interface    Recentsdao {
    @Query("SELECT * FROM recents ORDER BY SavingTime DESC")
    Flowable<List<recents>> getAllRecents();

    @Insert
    void insertRecents(recents... r);

    @Update
    void updaterecents(recents... r);

    @Delete
    void deleterecents(recents... r);

    @Query("DELETE FROM recents")
    void deleteall();
}
