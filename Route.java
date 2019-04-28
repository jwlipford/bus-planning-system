/* Route:
 * 
 * A list of BusStations in order from a departure station to a destination
 * station. This class extends ArrayList.
 * 
 * New Routes are created only in method DijkstraStation.dijkstraRoute. This
 * class contains no method for calculating a new Route and also contains no
 * unique fields; rather, it focuses on outputting information about itself
 * as text. This class also contains a method that checks equality with another
 * Route; this method is used when other classes attempt to generate multiple
 * unique routes between the same locations.
 */

import java.util.ArrayList;

public class Route extends ArrayList<BusStation>
{    
	@Override
    public String toString()
    // Displays a String of the form
    //     "A Route with n stations:
    //       station1
    //       station2
    //       ..."
    // This method is not used in the system but is still useful for debugging. ___
    {
        String s = "A Route with " + this.size() + " stations:\n";
        for( BusStation bs : this )
        	s += " " + bs.getName() + "\n";
        return s;
    }
	
	public String display( Bus bus ) // For display in one of the 3 windows
	// ___
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
    
	public String totals( Bus bus )
	// Returns a two-line String containing the total distance and total time.
	{
		double totalMiles = 0,
			   totalHours = 0;
		for( int i = 1; i < this.size(); ++i )
		{
			totalMiles += this.get( i-1 ).milesTo( this.get(i) );
			totalHours += this.get( i-1 ).hoursTo( this.get(i), bus );
		}
        return "Total distance: " + totalMiles + " miles\n" +
		       "Total time    : " + totalHours + " hours\n";
	}
	
	public boolean equals( Route route )
	// Overrides super.equals, but only when the argument is a Route
	// ___
	{
		if( this.size() != route.size() )
			return false;
		for( int i = 0; i < this.size(); ++i )
			if( this.get(i) != route.get(i) )
				return false;
		return true;
	}
	
	public boolean containsGasStations()
	// Returns whether this Route contains at least one GasStation
	{
	    for( BusStation bs : this )
	        if( bs.getClass() == GasStation.class )
	            return true;
	    return false;
	}
}
