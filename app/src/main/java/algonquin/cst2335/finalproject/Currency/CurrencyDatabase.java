package algonquin.cst2335.finalproject.Currency;


import android.content.Context;

import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CurrencyObject.class}, version = 2)
public abstract class CurrencyDatabase extends RoomDatabase {
    public abstract CurrencyDao cmDAO();
//    private static final String DATABASE_NAME = "database-name";


    private static CurrencyDatabase instance;

    public static synchronized CurrencyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            CurrencyDatabase.class, "database-name")
                    .build();
        }
        return instance;
    }
}