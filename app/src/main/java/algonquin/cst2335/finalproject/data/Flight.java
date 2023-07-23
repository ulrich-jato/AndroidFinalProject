package algonquin.cst2335.finalproject.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Flight {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "Destination")
    private String destination;
    @ColumnInfo(name = "IataCodeArrival")
    private String iataCodeArrival;
    @ColumnInfo(name = "Terminal")
    private String terminal;
    @ColumnInfo(name = "Gate")
    private String gate;
    @ColumnInfo(name = "Delay")
    private String delay;

    public Flight(){}
    public Flight(String destination, String iataCodeArrival, String terminal, String gate, String delay) {
        this.id = id;
        this.destination = destination;
        this.iataCodeArrival = iataCodeArrival;
        this.terminal = terminal;
        this.gate = gate;
        this.delay = delay;
    }

    public long getId(){
        return id;
    }
    public String getDestination() {
        return destination;
    }

    public String getIataCodeArrival() {
        return iataCodeArrival;
    }

    public String getTerminal() {
        return terminal;
    }

    public String getGate() {
        return gate;
    }

    public String getDelay() {
        return delay;
    }
}
