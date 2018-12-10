package app.sample.app.wallpaper.DatabaseFiles;

import java.util.List;

import app.sample.app.wallpaper.recents;
import io.reactivex.Flowable;

public interface Irecentdatasource {

    Flowable<List<recents>> getAllRecents();
    void insertRecents(recents... r);
    void updaterecents(recents... r);
    void deleterecents(recents... r);
    void deleteall();
}
