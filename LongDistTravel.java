import java.util.ArrayList;

public class LongDistTravel
{
    // Copied almost exactly from CityDefaultInitialization.implementTravel!
    // That method reads from the wrong text file, though, so we can't use it
    // for Long Distance Mode. This is lazy code design, but I didn't bother to
    // redo that method.
    
    // In this method, begin and end are numbered starting at 1, not 0.
    public static Route[] implementTravel(
        LongDistStationsDatabase ldsdb, int begin, int end ) throws Exception
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
        
        // Number of tries to find a new Route different from previous ones
        final int MAX_TRIES = 8;
        
        int numTries = 0;
        Route secondRoute;
        while( true ) // until broken
        {
            if( numTries >= MAX_TRIES )
                return new Route[] { bestRoute, null, null };
            secondRoute = DijkstraStation.dijkstraRoute( dStations, dest, true );
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
            if( !bestRoute.equals( thirdRoute ) && !secondRoute.equals( thirdRoute ) )
                break; // thirdRoute found!
            ++numTries;
        }
        
        return new Route[] { bestRoute, secondRoute, thirdRoute };
    }
}
