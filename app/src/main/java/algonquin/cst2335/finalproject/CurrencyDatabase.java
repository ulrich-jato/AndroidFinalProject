package algonquin.cst2335.finalproject;


import androidx.room.Database;

import androidx.room.RoomDatabase;

@Database(entities = {CurrencyObject.class}, version = 1)
public abstract class CurrencyDatabase extends RoomDatabase {
    public abstract CurrencyDao cmDAO();
}