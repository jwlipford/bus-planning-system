/* DijkstraStation:
 * 
 * A BusStation with two additional fields, dist and visited, used in
 * Dijkstra's Algorithm, a path-finding algorithm. This class also contains two
 * other fields, correspondingStation and connectedDStations, necessary because
 * new DijkstraStations are constructed from preexisting BusStations.
 * 
 * Several of this class's methods, though public, are not used outside this
 * method. Important methods used in other classes are
 * busStationsToDijkstraStations and dijkstraRoute (both of which are static).
 * 
 * The design of this class is based on Wikipedia's description of Dijkstra's
 * Algorithm. See https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm.
 * Retrieved 08-Feb-2019. See especially section 2, "Algorithm".
 */


import java.util.ArrayList;

public class DijkstraStation extends BusStation
{
	private double dist;
	    // A non-constant variable that will eventually equal the distance (in
	    // miles) along existing connections to a certain starting station
	
	private boolean visited;
	    // A non-constant variable indicating whether this station has been
	    // visited in the algorithm
	
	private BusStation correspondingStation;
	    // The BusStation used to generate this DijkstraStation
	
	private ArrayList<DijkstraStation> connectedDStations;
	    // A list of DijkstraStations "connected" to this station. This field
	    // parallels the connectedStations field such that each connected
	    // DijkstraStation corresponds to a connected normal BusStation.
	
	public DijkstraStation( BusStation station ) throws Exception
	// Initializes fields latitude, longitude, name, correspondingStation, and
	// connectedStations based on the station argument. Initializes fields
	// connectedDStations, dist, and visited to their initial values of null,
	// infinity, and false, all of which can change later.
	{
		super( station.getLatitude(), station.getLongitude(), station.getName() );
		correspondingStation = station;
		connectedStations    = station.connectedStations;
		connectedDStations   = null;
		dist = Double.POSITIVE_INFINITY;
		visited = false;
	}
	
	@Override
	public String toString()
	// Returns a String of the form
	//     "name at (lat, long) with dist = d from start".
	// The system does not currently use this method, but it is useful for
	// debugging.
	{
		return super.toString() + " with dist = " + dist + " from start";
	}
	
	
	
	
	public static DijkstraStation best( ArrayList<DijkstraStation> dStations )
	// Of the stations in the argument, returns the one with the lowest dist
	// value (the one closest to the start station) or null if the argument is
	// empty
	{
		if( dStations.isEmpty() )
		    return null;
	    DijkstraStation bestSoFar = dStations.get(0);
		for( int i = 1; i < dStations.size(); ++i )
		{
			DijkstraStation ith = dStations.get(i);
			if( ith.dist < bestSoFar.dist )
				bestSoFar = ith;
		}
		return bestSoFar;
	}
	
	public DijkstraStation bestConnectedStation()
	// Of the stations connected to this station, returns the one that is
	// closest to the start. If no stations are connected to this one or none
	// of the connected stations are closer than this station, returns null.
	{
		DijkstraStation ds = best( this.connectedDStations );
		if( ds != null && ds.dist < this.dist )
		    return ds;
		else
		    return null;
	}
	
	public DijkstraStation randomBetterConnectedStation()
	// Of the stations connected to this station, randomly returns one of those
	// that are closer to the start than this station. If no stations are
	// connected to this one or none of the connected stations are closer than
	// this station, returns null.
	{
		ArrayList<DijkstraStation> betterStations = new ArrayList<DijkstraStation>();
		for( DijkstraStation ds : this.connectedDStations )
			if( ds.dist < this.dist )
				betterStations.add( ds );
		if( !betterStations.isEmpty() )
		    return betterStations.get( (int)( Math.random() * betterStations.size() ) );
		else
		    return null;
	}
	
	
	
	
	public static ArrayList<DijkstraStation> busStationsToDijkstraStations(
			ArrayList<BusStation> bStations, BusStation start
	) throws Exception
	// Returns an ArrayList of DijkstraStations parallel to bStations, each
	// DijkstraStation with the same connections as the corresponding BusStation,
	// and each DijkstraStation with dist from start calculated.
	{
		// Create lists dStations (to be returned) and unvisited, both parallel
	    // to bStations (only initially parallel for unvisited, which loses
	    // members later in the program):
	    
	    ArrayList<DijkstraStation> dStations = new ArrayList<DijkstraStation>();
		ArrayList<DijkstraStation> unvisited = new ArrayList<DijkstraStation>();
		for( BusStation bs : bStations )
		{
			DijkstraStation ds = new DijkstraStation( bs );
			dStations.add( ds );
			unvisited.add( ds );
		}
		
		// For each DijkstraStation in dStations, now create its connections
		// parallel to bStations' stations' connections:
		
		for( DijkstraStation ds : dStations )
		{
			ds.connectedDStations =
					new ArrayList<DijkstraStation>( ds.connectedStations.size() );
			for( BusStation bs : ds.connectedStations )
				ds.connectedDStations.add( dStations.get( bStations.indexOf( bs ) ) );
		}
		
		// Set the starting DijkstraStations's dist to 0 (since dist is measured
		// from start):
		
		dStations.get( bStations.indexOf( start ) ).dist = 0;
		
		// Each station besides the one above currently has infinite dist. We
		// now find these stations' actual dists by visiting each station in a
		// certain order specified by Dijkstra's Algorithm. Visiting a station
		// finds the dists of its connected stations that were not already
		// visited:
		
		while( !unvisited.isEmpty() ) // Until all stations have been visited
		{
			DijkstraStation visiting = best( unvisited );
			    // The station currently being visited. (During the first
			    // interation, this is the starting station with 0 dist.)
			
			for( DijkstraStation cs : visiting.connectedDStations )
			{
				if( !cs.visited )
				{
					// If cs currently has infinite dist or if its distance
				    // through visiting would be better than its current dist,
				    // replace its dist with its distance through visiting, a
				    // lower number.
				    
				    double distThroughCurrent =
						visiting.dist + visiting.milesTo( cs );
					if( cs.dist > distThroughCurrent )
						cs.dist = distThroughCurrent;
				}
			}
			visiting.visited = true;
			unvisited.remove( visiting );
		}
		
		return dStations;
	}
	
	
	public static Route dijkstraRoute(
			ArrayList<DijkstraStation> dStations,
			BusStation destination,
			boolean random
	) throws Exception
	// Returns the Route from the start (specified when creating the dStations
	// ArrayList, probably using the busStationsToDijkstraStations method)
	// to the destination (specified here), either along the best route (as
	// determined by the traditional Dijkstra's Algorithm) or along a semi-
	// random route still with the constraint that each leg takes the person
	// closer to the destination. If no route from the start to the destination
	// exists, returns null.
	{
		DijkstraStation start       = null;
		DijkstraStation backtracker = null;
		    // backtracker backtracks from destination to start
		
		// Find start station and backtracker's initial station:
		
		for( DijkstraStation ds : dStations )
		{
			if( ds.correspondingStation == destination )
				backtracker = ds;
			if( ds.dist == 0 )
				start = ds;
			if( start != null && backtracker != null )
				break;
		}
		
		// Backtrack to start to create route:
		
		Route route = new Route();
		route.add( destination );
		
		while( backtracker != start )
		{
			if( random )
				backtracker = backtracker.randomBetterConnectedStation();
			else
				backtracker = backtracker.bestConnectedStation();
			
			if( backtracker != null )
			    route.add( 0, backtracker.correspondingStation );
			else
			    // For some reason, no suitable station exists to go to next.
			    // Therefore, no route can be generated.
			    return null;
		}
		
		return route;
	}
}