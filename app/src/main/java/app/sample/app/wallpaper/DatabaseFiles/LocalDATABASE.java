package app.sample.app.wallpaper.DatabaseFiles;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import app.sample.app.wallpaper.DatabaseFiles.database.Recentsdao;
import app.sample.app.wallpaper.recents;

import static app.sample.app.wallpaper.DatabaseFiles.LocalDATABASE.DATABASE_VERSION;

@Database(entities = recents.class , version = DATABASE_VERSION)
public abstract class LocalDATABASE extends RoomDatabase {

    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME = "WallPaper_recents";


    public abstract Recentsdao Dao();

    private static LocalDATABASE db;
    public static LocalDATABASE getInstance(Context x)
    {
        if(db==null)
        {
            db = Room.databaseBuilder(x,LocalDATABASE.class , DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return db;
    }

}
