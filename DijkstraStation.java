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
	// A non-constant variable that will eventually equal the distance along
	// along existing connections to the starting station
	
	private boolean visited;
	// A non-constant variable indicating whether this station has been visited
	// in the algorithm
	
	public ArrayList<DijkstraStation> connectedDStations;
	
	public DijkstraStation( BusStation station ) throws Exception
	{
		super( station.getLatitude(), station.getLongitude(), station.getName() );
		connectedStations  = station.connectedStations;
		connectedDStations =
			new ArrayList<DijkstraStation>( connectedStations.size() );
		dist = Double.POSITIVE_INFINITY;
		visited = false;
	}
	
	@Override
	public String toString() // This method is useful in debugging at least
	{
		return super.toString() + ", " + dist + " units from start";
	}
	
	
	// Static method cor is overloaded, and the 2 overloads return different types.
	public static DijkstraStation cor(
			BusStation bs,
			ArrayList<BusStation> bsList,
			ArrayList<DijkstraStation> dsList )
	// Returns the DijkstraStation in dsList that CORresponds to BusStation bs
	// in parallel ArrayList bsList
	{
		return dsList.get( bsList.indexOf( bs ) );
	}
	public static BusStation cor(
			DijkstraStation ds,
			ArrayList<DijkstraStation> dsList,
			ArrayList<BusStation> bsList )
	// Returns the BusStation in bsList that corresponds to DijkstraStation ds
	// in parallel ArrayList dsList
	{
		return bsList.get( dsList.indexOf( ds ) );
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
	
	
	
	public static Route dijkstraRoute(
			BusStation startStation,
			BusStation destStation,
			ArrayList<BusStation> bStations
	) throws Exception
	// The Route from the start to the destination, determined using Dijkstra's
	// Algorithm
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
			for( BusStation cs : ds.connectedStations )
				ds.connectedDStations.add( cor( cs, bStations, dStations ) );
		
		final DijkstraStation start = cor( startStation, bStations, dStations );
		final DijkstraStation dest  = cor(  destStation, bStations, dStations );
		start.dist = 0;
		
		DijkstraStation current = start; // non-constant
		
		while( true ) // Until loop is broken when dest is visited
		{
			for( DijkstraStation cs : current.connectedDStations )
			{
				if( !cs.visited )
				{
					double distThroughCurrent =
						current.dist + current.distanceTo( cs );
					if( cs.dist > distThroughCurrent )
						cs.dist = distThroughCurrent;
				}
			}
			current.visited = true;
			unvisited.remove( current );
			if( dest.visited)
				break;
			else
				current = bestOf( unvisited );
		}
		
		// Restore memory:
		unvisited = null;
		current   = null;
		
		Route route = new Route();
		route.add( destStation );
		DijkstraStation backtracker = dest;
		while( backtracker != start )
		{
			backtracker = bestOf( backtracker.connectedDStations );
			route.add( 0, cor( backtracker, dStations, bStations ) );
		}
		return route;
	}
}
