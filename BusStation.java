import java.util.ArrayList;

public class BusStation extends Place
{
    private String name;
        // This station's name. Can be the name of a city if the station is
        // used in Long Distance Mode. Should not contain a comma.
    public ArrayList<BusStation> connectedStations;
        // A list of stations "connected" to this station. A bus can directly
        // travel between stations only if they are connected; otherwise it
        // must attempt to use intermediate connected stations.
    
    public String getName(){ return name; }
    
    public BusStation( double latitude, double longitude, String name ) throws Exception
    {
        super( latitude, longitude );
        if( name.contains( "," ) )
            throw new Exception( "A BusStation's name cannot contain ','" );
        this.name = name;
        this.connectedStations = new ArrayList<BusStation>();
    }
    
    public boolean equals( BusStation b )
    // Returns a boolean representing whether this object has the same
    // coordinates and name as another BusStation, b
    {
        return super.equals( b ) && this.name.equals( b.name );
    }
    
    public void connect( BusStation station )
    // Connects this station with another station, but does not connect that
    // station with this one (creates a connection, but only in one direction)
    {
        connectedStations.add( station );
    }
    
    public void disconnect( BusStation station )
    // Disconnects this station from another station, but does not disconnect
    // that station from this one (disconnects only in one direction)
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
    // Returns the number of hours from this station to another place by a
    // certain bus, which travels at a certain speed
    {
    	return this.milesTo( place ) / bus.getCruisingSpeed();
    	// miles/(miles/hour) = miles*(hours/mile) = hours
    }
    
    public boolean connectedTo( BusStation station )
    // Returns a boolean representing whether a bus can travel directly from
    // this station to another station
    {
        for( BusStation b : this.connectedStations )
            if( b.equals( station ) ) // ¿ Do we need the equals method, or can we use == ?
                return true;
        return false;
    }
}
