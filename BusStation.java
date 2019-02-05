import java.util.ArrayList;

public class BusStation extends Place
{
    private String name;
    private ArrayList<BusStation> connectedStations;
    
    public String getName(){ return name; }
    
    public BusStation getConnectedStation( int i )
    {
        return connectedStations.get(i);
    }
    
    public BusStation( double latitude, double longitude, String name )
    {
        super( latitude, longitude );
        this.name = name;
        this.connectedStations = new ArrayList<BusStation>();
    }
    
    public void connect( BusStation station )
    {
        connectedStations.add( station );
    }
}
