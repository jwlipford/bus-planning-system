import java.util.ArrayList;

public class Route extends ArrayList<BusStation>
{    
    public String display( Bus bus )
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
    
	@Override
    public String toString() // Mostly for debugging, not for actual display
    {
        String s = "A Route with " + this.size() + " stations:\n";
        for( BusStation bs : this )
        	s += " " + bs.getName() + "\n";
        return s;
    }
	
	public boolean equals( Route route )
	// Overrides super.equals, but only when the argument is a Route
	{
		if( this.size() != route.size() )
			return false;
		for( int i = 0; i < this.size(); ++i )
			if( this.get(i) != route.get(i) )
				return false;
		return true;
	}
}
