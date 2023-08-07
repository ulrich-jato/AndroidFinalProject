/**
 * Data Access Object (DAO) interface for performing database operations related to ImageEntity objects.
 */
package algonquin.cst2335.finalproject.bearData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageDAO {

        /**
         * Inserts an ImageEntity object into the database.
         *
         * @param image The ImageEntity object to be inserted.
         * @return The ID of the inserted image.
         */
        @Insert
        public long insertImage(ImageEntity image);

        /**
         * Retrieves all ImageEntity objects from the database.
         *
         * @return A list containing all ImageEntity objects in the database.
         */
        @Query("SELECT * FROM ImageEntity")
        public List<ImageEntity> getAllImages();

        /**
         * Retrieves an ImageEntity object from the database by its ID.
         *
         * @param id The ID of the desired ImageEntity object.
         * @return The ImageEntity object with the specified ID.
         */
        @Query("SELECT * FROM ImageEntity WHERE id = :id")
        public ImageEntity getImageById(long id);

        /**
         * Deletes an ImageEntity object from the database.
         *
         * @param image The ImageEntity object to be deleted.
         */
        @Delete
        public void deleteImage(ImageEntity image);
}
