import java.util.ArrayList;

public class BusStation extends Place
{
    private String name;
    public ArrayList<BusStation> connectedStations;
    
    public String getName(){ return name; }
    
    public BusStation( double latitude, double longitude, String name ) throws Exception
    {
        super( latitude, longitude );
        this.name = name;
        this.connectedStations = new ArrayList<BusStation>();
    }
    
    public void connect( BusStation station )
    {
        connectedStations.add( station );
    }
    
    @Override
    public String toString()
    {
    	return name + " at (" + getLatitude() + ", " + getLongitude() + ")";
    }
}
