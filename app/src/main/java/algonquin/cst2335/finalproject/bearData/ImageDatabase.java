/**
 * Represents the Room Database class for managing ImageEntity objects.
 */
package algonquin.cst2335.finalproject.bearData;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ImageEntity.class}, version = 1)
public abstract class ImageDatabase extends RoomDatabase {

    /**
     * Provides an abstract method to access the associated ImageDAO object.
     *
     * @return The ImageDAO object for performing database operations.
     */
    public abstract ImageDAO imageDAO();
}
