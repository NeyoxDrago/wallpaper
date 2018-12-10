package app.sample.app.wallpaper;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;


@Entity(tableName = "Recents" , primaryKeys = {"imageLink","CategoryID"})
public class recents {

    @ColumnInfo(name="imageLink")
    @NonNull
    private String imageLink;

    @ColumnInfo(name="CategoryID")
    @NonNull
    private String CategoryID;

    @ColumnInfo(name="SavingTime")
    @NonNull
    private String SavingTime;

    @NonNull
    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(@NonNull String imageLink) {
        this.imageLink = imageLink;
    }

    @NonNull
    public String getSavingTime() {
        return SavingTime;
    }

    public void setSavingTime(@NonNull String savingTime) {
        SavingTime = savingTime;
    }

    @NonNull
    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(@NonNull String categoryID) {
        CategoryID = categoryID;
    }

    public recents(@NonNull String imageLink,@NonNull String CategoryID,@NonNull String SavingTime) {

        this.imageLink = imageLink;
        this.CategoryID = CategoryID;
        this.SavingTime = SavingTime;
    }
}
