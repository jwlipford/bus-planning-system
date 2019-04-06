import java.util.ArrayList;

public class LongDistTravel
{
    public static Route[] implementTravel(
        LongDistStationsDatabase ldsdb, int begin, int end ) throws Exception
    // Copied almost exactly from CityDefaultInitialization.implementTravel!
    // That method reads from the wrong text file, though, so we can't use it
    // for Long Distance Mode. This is lazy code design, but I didn't bother to
    // redo that method.
    // In this method, begin and end are numbered starting at 1, not 0.
    {
        ArrayList<BusStation> stations = ldsdb.toArrayList();
        
        int s = begin - 1;
        if( s < 0 || s >= stations.size() )
            throw new Exception();
        
        int d = end - 1;
        if( d < 0 || d >= stations.size() )
            throw new Exception();
        
        BusStation start = stations.get(s);
        BusStation dest  = stations.get(d);
        
        ArrayList<DijkstraStation> dStations =
                DijkstraStation.busStationsToDijkstraStations( stations, start );
        
        Route bestRoute = DijkstraStation.dijkstraRoute( dStations, dest, false );
        if( bestRoute == null )
            return new Route[] { null, null, null };
        
        // Number of tries to find a new Route different from previous ones
        final int MAX_TRIES = 8;
        
        int numTries = 0;
        Route secondRoute;
        while( true ) // until broken
        {
            if( numTries >= MAX_TRIES )
                return new Route[] { bestRoute, null, null };
            secondRoute = DijkstraStation.dijkstraRoute( dStations, dest, true );
            if( secondRoute == null )
                return new Route[] { bestRoute, null, null };
            if( !bestRoute.equals( secondRoute ) )
                break; // secondRoute found!
            ++numTries;
        }
        
        numTries = 0;
        Route thirdRoute;
        while( true ) // until broken
        {
            if( numTries >= MAX_TRIES )
                return new Route[] { bestRoute, secondRoute, null };
            thirdRoute = DijkstraStation.dijkstraRoute( dStations, dest, true );
            if( thirdRoute == null )
                return new Route[] { bestRoute, secondRoute, null };
            if( !bestRoute.equals( thirdRoute ) && !secondRoute.equals( thirdRoute ) )
                break; // thirdRoute found!
            ++numTries;
        }
        
        return new Route[] { bestRoute, secondRoute, thirdRoute };
    }
    
    public static ArrayList<Place> suggestedGasStations( Route route, Bus bus )
            throws Exception
    // Returns a list of Places at which it is suggested that the user create
    // GasStations for legs of the route that are too long. The user does not
    // have to create GasStations at these places exactly; for each suggested
    // Place, the user should be allowed to create a GasStation in its general
    // vicinity.
    {
        final double maxDist = bus.maxRange();
        ArrayList<Place> suggestions = new ArrayList<Place>( route.size() );
        for( int i = 0; i < route.size() - 1; ++i )
        {
            Place start = route.get(i);
            Place dest  = route.get( i+1 );
            if( start.milesTo( dest ) >= maxDist )
            {
                double midLat = (start.getLatitude() + dest.getLatitude()) / 2;
                double midLng = (start.getLongitude() + dest.getLongitude()) / 2;
                suggestions.add( new Place( midLat, midLng ) );
            }
        }
        return suggestions;
    }
}
