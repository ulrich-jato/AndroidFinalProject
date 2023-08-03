package algonquin.cst2335.finalproject.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object (DAO) interface for the Flight entity.
 * This interface defines the database operations related to the Flight entity.
 *
 * @author Jato Ulrich Guiffo Kengne
 */
@Dao
public interface FlightDAO {

    /**
     * Inserts a new flight record into the database.
     * @param flight The Flight object to insert.
     * @return The ID of the newly inserted flight record.
     */
    @Insert
    public long insertFlight(Flight flight);

    /**
     * Retrieves a list of all flights from the database.
     * @return A list of Flight objects representing all flights in the database.
     */
    @Query("SELECT * FROM Flight")
    public List<Flight> getAllFlights();

    /**
     * Retrieves a single flight record from the database based on the specified ID.
     * @param id The ID of the flight to retrieve.
     * @return The Flight object with the specified ID, or null if not found.
     */
    @Query("SELECT * FROM Flight WHERE id = :id")
    public Flight getFlightById(long id);

    /**
     * Deletes a flight record from the database.
     * @param flight The Flight object to delete.
     */
    @Delete
    public void deleteFlight(Flight flight);
}
