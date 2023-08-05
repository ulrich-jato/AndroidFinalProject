package algonquin.cst2335.finalproject.bearData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageDAO {
        @Insert
        public long insertImage(ImageEntity image);

        @Query("SELECT * FROM ImageEntity")
        public List<ImageEntity> getAllImages() ;

        @Query("SELECT * FROM ImageEntity WHERE id = :id")
        public ImageEntity getImageById(long id);

        @Delete
        public void deleteImage(ImageEntity image);
}
