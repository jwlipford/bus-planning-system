/* LongDistTravel:
 * A class containing two static methods related to travel in Long-Distance Mode.
 * Method implementTravel creates a set of three possible Routes between two
 * stations in the LongDistStationsDatabase.
 * Method insertGasStations changes both a Route and the LongDistStationsDatabase
 * by creating GasStations in legs of the route that are too long for a chosen
 * Bus to traverse.
 */

import java.util.ArrayList;

public class LongDistTravel
{
    private static Route[] possiblyRearrange( Route[] routes )
    // Rearranges Routes, putting those with gas stations first.
    {
        ArrayList<Route> withGasStations = new ArrayList<Route>();
        ArrayList<Route> sansGasStations = new ArrayList<Route>();
        
        for( Route r : routes )
        {
            if( r.containsGasStations() )
                withGasStations.add( r );
            else
                sansGasStations.add( r );
        }
        
        Route[] rearranged = new Route[ routes.length ];
        
        int i = 0;
        for( int j = 0; j < withGasStations.size(); ++j )
        {
            rearranged[i] = withGasStations.get(j);
            ++i;
        }
        for( int j = 0; j < sansGasStations.size(); ++j )
        {
            rearranged[i] = sansGasStations.get(j);
            ++i;
        }
        
        return rearranged;
    }
    
    public static Route[] implementTravel(
        LongDistStationsDatabase ldsdb, int begin, int end ) throws Exception
    /* This method is almost exactly the same as CityDefaultInitialization
     * .implementTravel! That method reads from the wrong text file, though, so
     * we can't use it for Long Distance Mode. Instead we read from the
     * LongDistStationsDatabase.
     * This method's arguments begin and end are indices numbered from 1, not 0.
     * This method ___
     */
    {
        ArrayList<BusStation> stations = ldsdb.toArrayList();
        
        int s = begin - 1; // Convert to a 0-based index
        if( s < 0 || s >= stations.size() )
            throw new Exception();
        
        int d = end - 1;   // Convert to a 0-based index
        if( d < 0 || d >= stations.size() )
            throw new Exception();
        
        BusStation start = stations.get(s);
        BusStation dest  = stations.get(d);
        
        ArrayList<DijkstraStation> dStations =
                DijkstraStation.busStationsToDijkstraStations( stations, start );
        
        // Find first, best Route:
        
        Route bestRoute = DijkstraStation.dijkstraRoute( dStations, dest, false );
        if( bestRoute == null )
            return new Route[] { null, null, null };
        
        // Find second, semirandom Route, if possible:
        
        final int MAX_TRIES = 8;
            // Number of tries to find a new Route different from previous ones
        int numTries = 0;
            // Number of tries used
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
        
        // Find third, semirandom Route, if possible:
        
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
        
        BusStation departure = route.get(0);
        BusStation finalDest = route.get( route.size() - 1 );
        departure.disconnect( finalDest );
        finalDest.disconnect( departure );
        
        ldsdb.update();
    }
}
