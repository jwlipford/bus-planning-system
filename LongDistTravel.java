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
            
            start.disconnect( dest );
            dest.disconnect( start );
            
            BusStation current = start;
            for( int j = 0; j < numGasStationsNeeded; ++j )
            {
                double newLat = current.getLatitude()  + latChangePerSubsection;
                double newLng = current.getLongitude() + lngChangePerSubsection;
                
                // Check whether a station already exists at the needed location.
                // If not, create one and add it to ldsdb and route. If so, just
                // add the preexisting one to route.
                
                BusStation preexisting = ldsdb.stationAtLocation( newLat, newLng );
                
                if( preexisting == null ) // Should be the case most of the time
                {
                    String newName = "Gas Station " + (numGasStationsInDatabase + 1);
                    
                    GasStation newGS = new GasStation( newLat, newLng, newName );
                    newGS.connect( current );
                    current.connect( newGS );
                    
                    ldsdb.addLDStation( newGS );
                    ++numGasStationsInDatabase;
                    
                    current = newGS;
                }
                else
                {
                    current = preexisting;
                }
                
                ++i;
                route.add( i, current );
            }
            if( !current.connectedTo( dest ) )
            {
                current.connect( dest );
                dest.connect( current );
            }
        }
        ldsdb.update();
    }
}
