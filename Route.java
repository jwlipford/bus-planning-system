/* Route:
 * 
 * A list of BusStations in order from a departure station to a destination
 * station. This class extends ArrayList.
 * 
 * New Routes are created only in method DijkstraStation.dijkstraRoute. This
 * class contains no construtor and no unique fields; rather, it has three
 * methods related to outputting information about itself as text and one
 * method that checks equality with another Route, which is used when other
 * classes attempt to generate multiple unique routes between the same
 * locations.
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
    // This method is not used in the system but is still useful for debugging.
    {
        String s = "A Route with " + this.size() + " stations:\n";
        for( BusStation bs : this )
        	s += " " + bs.getName() + "\n";
        return s;
    }
	
	public String display( Bus bus )
	// Returns a (usually long) String that lists information for each leg of
	// this Route based on the Bus used to travel that Route. This String is
	// displayed directly to the user once the user chooses a Route.
	// For each leg of this Route, six lines are displayed: the number of the
	// leg, the start BusStation, the leg's distance, the leg's time (by the
	// given Bus), the heading at start, and the destination BusStation.
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
	// Returns a two-line String of the form
	//     "Total distance: m miles
	//      Total time:     h hours"
	{
		double totalMiles = 0,
			   totalHours = 0;
		for( int i = 1; i < this.size(); ++i )
		{
			totalMiles += this.get( i-1 ).milesTo( this.get(i) );
			totalHours += this.get( i-1 ).hoursTo( this.get(i), bus );
		}
        return "Total distance: " + totalMiles + " miles\n" +
		       "Total time:     " + totalHours + " hours\n";
	}
	
	public boolean equals( Route route )
	// Returns whether this Route contains the same stations in the same order
	// as the route argument.
	// Overrides super.equals, but only when the argument is a Route
	{
		if( this.size() != route.size() )
			return false;
		for( int i = 0; i < this.size(); ++i )
			if( !this.get(i).equals( route.get(i) ) ) // Uses BusStation.equals
				return false;
		return true;
	}
}
