package algonquin.cst2335.finalproject.data;

public class Flight {
    private String destination;
    private String iataCodeArrival;
    private String terminal;
    private String gate;
    private String delay;

    public Flight(){}
    public Flight(String destination, String iataCodeArrival, String terminal, String gate, String delay) {
        this.destination = destination;
        this.iataCodeArrival = iataCodeArrival;
        this.terminal = terminal;
        this.gate = gate;
        this.delay = delay;
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
