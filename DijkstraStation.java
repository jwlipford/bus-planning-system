/* DijkstraStation: A BusStation with additional attributes for use in
 * Dijkstra's Algorithm, a path-finding algorithm
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
	// A non-constant variable indicating whether this station has been visited
	// in the algorithm
	
	private BusStation correspondingStation;
	
	public ArrayList<DijkstraStation> connectedDStations;
	
	public DijkstraStation( BusStation station ) throws Exception
	{
		super( station.getLatitude(), station.getLongitude(), station.getName() );
		correspondingStation = station;
		connectedStations    = station.connectedStations;
		connectedDStations   = null;
		dist = Double.POSITIVE_INFINITY;
		visited = false;
	}
	
	@Override
	public String toString() // This method is useful in debugging at least
	{
		return super.toString() + ", " + dist + " units from start";
	}
	
	
	
	
	public static DijkstraStation bestOf( ArrayList<DijkstraStation> dStations )
	{
		DijkstraStation bestSoFar = dStations.get(0);
		for( int i = 1; i < dStations.size(); ++i )
		{
			DijkstraStation ith = dStations.get(i);
			if( ith.dist < bestSoFar.dist )
				bestSoFar = ith;
		}
		return bestSoFar;
	}
	
	
	public static ArrayList<DijkstraStation> busStationsToDijkstraStations(
			ArrayList<BusStation> bStations, BusStation start
	) throws Exception
	// Returns an ArrayList of DijkstraStations parallel to bStations, each
	// DijkstraStation with the same connections as the corresponding BusStation,
	// and each DijkstraStation with dist from start calculated
	{
		ArrayList<DijkstraStation> dStations = new ArrayList<DijkstraStation>();
		ArrayList<DijkstraStation> unvisited = new ArrayList<DijkstraStation>();
		for( BusStation bs : bStations )
		{
			DijkstraStation ds = new DijkstraStation( bs );
			dStations.add( ds );
			unvisited.add( ds );
		}
		// dStations will not change from now on. unvisited will gradually lose
		// members from now on as more stations are visited.
		
		
		for( DijkstraStation ds : dStations )
		{
			ds.connectedDStations =
					new ArrayList<DijkstraStation>( ds.connectedStations.size() );
			for( BusStation bs : ds.connectedStations )
				ds.connectedDStations.add( dStations.get( bStations.indexOf( bs ) ) );
		}
		
		dStations.get( bStations.indexOf( start ) ).dist = 0;
		
		DijkstraStation current; // non-constant
		
		while( !unvisited.isEmpty() )
		{
			current = bestOf( unvisited );
			// In the first iteration, current = the one with dist of 0,
			// assigned above, corresponding to start.
			for( DijkstraStation cs : current.connectedDStations )
			{
				if( !cs.visited )
				{
					double distThroughCurrent =
						current.dist + current.milesTo( cs );
					if( cs.dist > distThroughCurrent )
						cs.dist = distThroughCurrent;
				}
			}
			current.visited = true;
			unvisited.remove( current );
		}
		
		return dStations;
	}
	
	
	public static Route dijkstraRoute(
			ArrayList<DijkstraStation> dStations, BusStation destination
	) throws Exception
	// The Route from the start (specified when creating the dStations
	// ArrayList, probably using the busStationsToDijkstraStations method)
	// to the destination (specified here)
	{
		DijkstraStation start       = null;
		DijkstraStation backtracker = null;
		for( DijkstraStation ds : dStations )
		{
			if( ds.correspondingStation == destination )
				backtracker = ds;
			if( ds.dist == 0 )
				start = ds;
			if( start != null && backtracker != null )
				break;
		}
		
		Route route = new Route();
		route.add( destination );
		
		while( backtracker != start )
		{
			backtracker = bestOf( backtracker.connectedDStations );
			route.add( 0, backtracker.correspondingStation );
		}
		
		return route;
	}
}
