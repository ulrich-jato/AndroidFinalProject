package algonquin.cst2335.finalproject.Currency;


import android.content.Context;

import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;
/**
 * CurrencyDatabase is a Room database class that represents the database for currency conversions.
 * It is used to create and access the database and provides a singleton instance to ensure
 * there is only one instance of the database throughout the application.
 */
@Database(entities = {CurrencyObject.class}, version = 2)
public abstract class CurrencyDatabase extends RoomDatabase {

    /**
     * Abstract method to get the CurrencyDao interface, which allows interaction with the database.
     *
     * @return The CurrencyDao interface used to perform database operations.
     */
    public abstract CurrencyDao cmDAO();

    /**
     * Singleton instance of the CurrencyDatabase to ensure only one instance of the database exists.
     */
    private static CurrencyDatabase instance;

    /**
     * Gets the singleton instance of the CurrencyDatabase.
     * If the instance doesn't exist, it creates a new instance using Room.databaseBuilder.
     *
     * @param context The application context used to create the database.
     * @return The singleton instance of the CurrencyDatabase.
     */
    public static synchronized CurrencyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            CurrencyDatabase.class, "database-name")
                    .build();
        }
        return instance;
    }
}
