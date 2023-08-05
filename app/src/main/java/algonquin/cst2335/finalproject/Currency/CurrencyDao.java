package algonquin.cst2335.finalproject.Currency;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
/**
 * CurrencyDao is a Data Access Object (DAO) interface used to interact with the Room database
 * for currency conversions. It provides methods to insert, retrieve, and delete currency conversions.
 */
@Dao
public interface CurrencyDao {

    /**
     * Inserts a new currency conversion into the Room database.
     *
     * @param m The CurrencyObject representing the currency conversion to be inserted.
     * @return The ID of the inserted currency conversion if successful, or -1 on failure.
     */
    @Insert
    public long insertConvertTo(CurrencyObject m);

    /**
     * Retrieves all currency conversions from the Room database.
     *
     * @return A List of CurrencyObject representing all saved currency conversions in the database.
     */
    @Query("Select * from CurrencyObject")
    public List<CurrencyObject> getMessages();

    /**
     * Deletes a currency conversion from the Room database.
     *
     * @param x The CurrencyObject representing the currency conversion to be deleted.
     */
    @Delete
    void delete(CurrencyObject x);

}

