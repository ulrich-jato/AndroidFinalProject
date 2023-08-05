package algonquin.cst2335.finalproject.bearData;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
@TypeConverters(BitmapTypeConverter.class)
public class ImageEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "Height")
    private int height ;

    @ColumnInfo(name = "width")
    private int weight;

    @ColumnInfo(name = "Image")
    private Bitmap image ;

    public ImageEntity(){}

    public ImageEntity(int height, int weight, Bitmap image) {
        this.height = height;
        this.weight = weight;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
