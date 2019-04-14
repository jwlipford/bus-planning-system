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
    
    public static void insertGasStations(
        Route route, Bus bus, LongDistStationsDatabase ldsdb ) throws Exception
    // Inserts gas stations as needed in legs of the route that are too long,
    // adding those gas stations to ldsdb
    {
        final double maxDist = bus.maxRange();
        
        for( int i = 0; i < (route.size() - 1); ++i )
        {
            BusStation start = route.get(i);
            BusStation dest  = route.get( i+1 );
            
            double legDist = start.milesTo( dest );
            int numGasStationsNeeded = (int)(legDist / maxDist); // rounding down
            if( numGasStationsNeeded == 0)
                continue;
            int numSubsectionsOfLeg = numGasStationsNeeded + 1;
            
            double latChangePerSubsection =
                (dest.getLatitude() - start.getLatitude()) / numSubsectionsOfLeg;
            double lngChangePerSubsection =
                (dest.getLongitude() - start.getLongitude()) / numSubsectionsOfLeg;
            
            int numGasStationsInDatabase = ldsdb.gasStations().length;
                // Used to generate names for new GasStations
            
            BusStation station = start;
            for( int j = 0; j < numGasStationsNeeded; ++j )
            {
                double newLat = station.getLatitude()  + latChangePerSubsection;
                double newLng = station.getLongitude() + lngChangePerSubsection;
                String newName = "Gas Station " + (numGasStationsInDatabase + 1);
                
                GasStation newGS = new GasStation( newLat, newLng, newName );
                newGS.connect( station );
                station.connect( newGS );
                station = newGS;
                
                ++i;
                route.add( i, newGS );
                
                ldsdb.addLDStation( newGS );
                ++numGasStationsInDatabase;
            }
            station.connect( dest );
            dest.connect( station );
        }
        ldsdb.update();
    }
}
