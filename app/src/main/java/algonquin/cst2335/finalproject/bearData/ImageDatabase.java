package algonquin.cst2335.finalproject.bearData;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ImageEntity.class}, version=1)
public abstract class ImageDatabase extends RoomDatabase {
    public abstract  ImageDAO imageDAO();
}
