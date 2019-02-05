import java.util.ArrayList;

public class Route
{
    private ArrayList<BusStation> route;
    
    public Route( BusStation... stations )
    {
        for( int i = 0; i < stations.length; ++i )
            this.route.add( stations[i] );
    }
    
    public void add( BusStation station )
    {
        route.add( station );
    }
    
    @Override
    public String toString() // Use this to display the route!
    {
        String s = "";
        for( int i = 1; i < route.size(); ++i )
        {
            BusStation current = route.get( i-1 );
            BusStation next    = route.get( i   );
            s += "Leg " + i + "\n";
            s += "  Start:       " + current.getName() + "\n";
            s += "    Latitude:  " + current.getLatitude() + "\n";
            s += "    Longitude: " + current.getLongitude() + "\n";
            s += "  Distance:    " + current.distanceTo( next ) + " miles\n";
            s += "  Time:        " + "\n"; // add functionality
            s += "  Heading:     " + current.headingTo( next ) + "\n";
            s += "  Destination: " + next.getName() + "\n";
            s += "    Latitude:  " + next.getLatitude() + "\n";
            s += "    Longitude: " + next.getLongitude() + "\n";
            s += "\n";
        }
        return s;
    }
}