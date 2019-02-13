import java.util.ArrayList;

public class Route extends ArrayList<BusStation>
{    
    public Bus bus;
	
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
            s += "  Distance:    " + current.milesTo( next ) + " miles\n";
            s += "  Time:        " + current.hoursTo( next, bus ) + " hours\n";
            s += "  Heading:     " + current.headingTo( next ) + "\n";
            s += "  Destination: " + next.toString() + "\n";
        }
        return s;
    }
	
	// Remove if not needed
	public boolean equalTo( Route r )
	{
		if( r == null || this.size() != r.size() || this.bus != r.bus )
			return false;
		for( int i = 0; i < this.size(); ++i )
			if( this.get(i) != r.get(i) )
				return false;
		return true;
	}
}
