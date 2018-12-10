package app.sample.app.wallpaper.DatabaseFiles;

import java.util.List;

import app.sample.app.wallpaper.recents;
import io.reactivex.Flowable;

public class repository implements Irecentdatasource {

    public Irecentdatasource ds;
    public static repository instance;

    public repository(Irecentdatasource ds) {
        this.ds = ds;
    }

    public static Irecentdatasource getInstance(Irecentdatasource d)
    {
        if(instance==null)
            instance = new repository(d);
        return instance ;
    }

    @Override
    public Flowable<List<recents>> getAllRecents() {
        return ds.getAllRecents();
    }

    @Override
    public void insertRecents(recents... r) {

        ds.insertRecents(r);
    }

    @Override
    public void updaterecents(recents... r) {

        ds.updaterecents(r);
    }

    @Override
    public void deleterecents(recents... r) {

        ds.deleterecents(r);
    }

    @Override
    public void deleteall() {
        ds.deleteall();
    }
}
