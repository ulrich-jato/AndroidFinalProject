package algonquin.cst2335.finalproject.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface FlightDAO {
    @Insert
    public long insertMessage(Flight flight);

    @Query("SELECT * FROM Flight")
    public List<Flight> getAllFlights() ;

    @Delete
    public void deleteFlight(Flight flight);
}
