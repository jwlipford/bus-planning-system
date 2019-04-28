/* BusStation:
 * 
 * A Place (a location on the surface of the earth, identified by a latitude
 * and a longitude) with a name, to and from which a Bus can travel. Can be
 * either a city used in long-distance mode or a location within a city used in
 * city mode.
 * 
 * Every BusStation contains a list of other stations "connected" to it. A bus
 * can directly travel between stations only if they are connected; otherwise
 * it must attempt to use intermediate connected stations. This class contains
 * methods to connect it, disconnect it, or check its connection with other
 * stations.
 * 
 * This class also contains methods that determine whether it is equivalent
 * ("equal") to another station, determine the time to another station via a
 * certain Bus, and convert it to a String.
 */

import java.util.ArrayList;

public class BusStation extends Place
{
    private String name;
        // This station's name. Can be the name of a city if the station is
        // used in Long Distance Mode. Should not contain a comma.
    public ArrayList<BusStation> connectedStations;
        // A list of stations "connected" to this station. 
    
    public String getName(){ return name; }
        // A single accessor (those for latitude and longitude are in Place)
    
    public BusStation( double latitude, double longitude, String name ) throws Exception
    // Makes sure name contains no comma (throws an Exception if so) and assigns the five
    // arguments to the corresponding fields
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
    // Returns a String of the form "name at (lat, long)"
    {
    	// Convert lat and long to floats to make line shorter
        return name + " at (" + (float)getLatitude() + ", " + (float)getLongitude() + ")";
    }
    
    public double hoursTo( Place place, Bus bus )
    // Returns the number of hours from this station to another place by a
    // certain bus travelling at that bus's cruising speed
    {
    	return this.milesTo( place ) / bus.getCruisingSpeed();
    	// miles/(miles/hour) = miles*(hours/mile) = hours
    }
    
    public boolean connectedTo( BusStation station )
    // Returns whether this BusStation is connected to another BusStation
    {
        for( BusStation b : this.connectedStations )
            if( b.equals( station ) )
                return true;
        return false;
    }
}
