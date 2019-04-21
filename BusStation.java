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
    
    public boolean equals( BusStation b )
    {
        return super.equals( b ) && this.name.equals( b.name );
    }
    
    public void connect( BusStation station )
    {
        connectedStations.add( station );
    }
    
    public void disconnect( BusStation station )
    {
        connectedStations.remove( station );
    }
    
    @Override
    public String toString()
    {
    	// Convert lat and long to floats to make line shorter
        return name + " at (" + (float)getLatitude() + ", " + (float)getLongitude() + ")";
    }
    
    public double hoursTo( Place place, Bus bus )
    {
    	return this.milesTo( place ) / bus.getCruisingSpeed();
    	// miles/(miles/hour) = miles*(hours/mile) = hours
    }
    
    public boolean connectedTo( BusStation station )
    {
        for( BusStation b : this.connectedStations )
            if( b.equals( station ) ) // ¿ Do we need the equals method, or can we use == ?
                return true;
        return false;
    }
}
