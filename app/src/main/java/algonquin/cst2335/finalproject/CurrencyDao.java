package algonquin.cst2335.finalproject;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CurrencyDao {
    @Insert
    public long insertConvertTo(CurrencyObject m);

    @Query("Select * from CurrencyObject")
    public List<CurrencyObject> getAllmessages();
}
