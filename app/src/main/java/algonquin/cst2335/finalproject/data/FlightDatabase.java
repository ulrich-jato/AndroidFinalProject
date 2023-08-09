package algonquin.cst2335.finalproject.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Room database class that defines the database configuration and serves as the main access point to the database.
 * This class extends RoomDatabase, and it is responsible for providing access to the DAO interface and managing the database connection.
 *
 * @author Jato Ulrich Guiffo Kengne
 */
@Database(entities = {Flight.class}, version = 1)
public abstract class FlightDatabase extends RoomDatabase {

    /**
     * Abstract method that returns the DAO interface for the Flight entity.
     * This method is automatically implemented by Room during compilation.
     * @return The FlightDAO object that provides access to database operations for Flight entity.
     */
    public abstract FlightDAO flightDAO();
}
