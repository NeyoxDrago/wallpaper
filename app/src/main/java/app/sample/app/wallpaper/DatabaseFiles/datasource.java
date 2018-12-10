package app.sample.app.wallpaper.DatabaseFiles;

import java.util.List;

import app.sample.app.wallpaper.DatabaseFiles.database.Recentsdao;
import app.sample.app.wallpaper.recents;
import io.reactivex.Flowable;

public class datasource implements Irecentdatasource {

    public Recentsdao DAO;
    public static datasource instance;

    public datasource(Recentsdao DAO) {
        this.DAO = DAO;
    }

    public static datasource getInstance(Recentsdao d)
    {

        if(instance==null)
        {
            instance = new datasource(d);
        }
        return instance;

    }

    @Override
    public Flowable<List<recents>> getAllRecents() {
         return  DAO.getAllRecents();
    }

    @Override
    public void insertRecents(recents... r) {

         DAO.insertRecents(r);

    }

    @Override
    public void updaterecents(recents... r) {

        DAO.updaterecents(r);
    }

    @Override
    public void deleterecents(recents... r) {

        DAO.deleterecents(r);
    }

    @Override
    public void deleteall() {

        DAO.deleteall();
    }
}
