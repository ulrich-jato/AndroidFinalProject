package algonquin.cst2335.finalproject.bearData;

import android.graphics.Bitmap;

public class ImageEntity {
    private long id;
    private int height ;
    private int weight;
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
