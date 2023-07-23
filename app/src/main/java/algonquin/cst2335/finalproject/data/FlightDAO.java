package algonquin.cst2335.finalproject.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface FlightDAO {
    @Insert
    public long insertFlight(Flight flight);

    @Query("SELECT * FROM Flight")
    public List<Flight> getAllFlights() ;

    @Query("SELECT * FROM Flight WHERE id = :id")
    public Flight getFlightById(long id);

    @Delete
    public void deleteFlight(Flight flight);


}
