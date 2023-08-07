/**
 * This class provides type conversion methods for converting Bitmap objects to byte arrays
 * and vice versa. These methods are used for conversion during Room database operations.
 */
package algonquin.cst2335.finalproject.bearData;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;

public class BitmapTypeConverter {

    /**
     * Converts a byte array to a Bitmap object.
     *
     * @param byteArray The byte array representing the image data.
     * @return A Bitmap object converted from the byte array.
     */
    @TypeConverter
    public static Bitmap toBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    /**
     * Converts a Bitmap object to a byte array.
     *
     * @param bitmap The Bitmap object to be converted.
     * @return A byte array representing the image data from the Bitmap.
     */
    @TypeConverter
    public static byte[] fromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }
}
