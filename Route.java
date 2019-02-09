import java.util.ArrayList;

public class Route extends ArrayList<BusStation>
{    
    @Override
    public String toString() // Use this to display the route!
    {
        String s = "";
        for( int i = 1; i < this.size(); ++i )
        {
            BusStation current = this.get( i-1 );
            BusStation next    = this.get( i   );
            s += "Leg " + i + "\n";
            s += "  Start:       " + current.toString() + "\n";
            s += "  Distance:    " + current.distanceTo( next ) + " miles\n";
            s += "  Time:        " + "\n"; // add functionality
            s += "  Heading:     " + current.headingTo( next ) + "\n";
            s += "  Destination: " + next.toString() + "\n";
        }
        return s;
    }
}