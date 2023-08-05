package algonquin.cst2335.finalproject.Currency;


import android.content.Context;

import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CurrencyObject.class}, version = 2, exportSchema = false)
public abstract class CurrencyDatabase extends RoomDatabase {
    public abstract CurrencyDao cmDAO();
    private static final String DATABASE_NAME = "conversion-database";


    private static CurrencyDatabase instance;

    public static synchronized CurrencyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            CurrencyDatabase.class, DATABASE_NAME)
                    .build();
        }
        return instance;
    }
}