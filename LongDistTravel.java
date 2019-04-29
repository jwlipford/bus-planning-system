/* LongDistTravel:
 * 
 * A class containing two static methods related to travel in Long-Distance Mode.
 * 
 * Method implementTravel creates an array of three possible Routes between two
 * stations in the LongDistStationsDatabase.
 * 
 * Method insertGasStations changes both a Route and the LongDistStationsDatabase
 * by creating GasStations in legs of the route that are too long for a chosen
 * Bus to traverse.
 */

import java.util.ArrayList;

public class LongDistTravel
{
    public static Route[] implementTravel(
        LongDistStationsDatabase ldsdb, int begin, int end ) throws Exception
    // This method generates 3 Routes between the start station at index begin
    // and the destination station at index end. This method may not succeed in
    // finding all, some, or any Routes, in which cases it still returns an
    // array of length 3 but with nulls for Routes that cannot be found. The
    // first Route (if it exists) is the shortest Route generated by Dijkstra's
    // Algorithm, and the second and third Routes (if they exist) are semi-
    // random Routes with the constraint that each leg takes the person closer
    // to the destination.
    // 
    // This method is almost exactly the same as CityDefaultInitialization
    // .implementTravel! That method reads from the wrong text file, though, so
    // we can't use it for Long Distance Mode. Instead we read from the
    // LongDistStationsDatabase.
    // 
    // This method's arguments begin and end are indices numbered from 1, not 0.
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
        while( true ) // Until broken
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
        while( true ) // Until broken
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
    // Inserts gas stations as needed in legs of the route that are too long
    // for the bus to traverse, adding those gas stations to ldsdb unless
    // equivalent gas stations already exist. Automatically created GasStations
    // are named by number; for example, if two GasStations already exist, a
    // new one will be named "Gas Station 3".
    {
        final double maxDist = bus.maxRange();
            // Maximum length of a leg without needing to insert a new GasStation
        
        for( int i = 0; i < (route.size() - 1); ++i ) // For each leg of the route
        {
            BusStation start = route.get(i);
                // The leg's start BusStation
            
            BusStation dest  = route.get( i+1 );
                // The leg's end BusStation
            
            double legDist = start.milesTo( dest );
                // The length of the leg
            
            int numGasStationsNeeded = (int)(legDist / maxDist);
                // Cast to int to round down.
                // Example: if legDist is 1.5 times the maxDist, then
                // (int)(1.5)=1 new GasStation is needed.
            
            if( numGasStationsNeeded == 0)
                // Then nothing needs to be done for this leg, so move to next leg.
                continue;
            
            int numSubsectionsOfLeg = numGasStationsNeeded + 1;
                // For example, if one new GasStation is needed, then it splits
                // the leg into 1+1=2 subsections.
            
            double latChangePerSubsection =
                (dest.getLatitude() - start.getLatitude()) / numSubsectionsOfLeg;
                // The change (negative or positive) in latitude across one subsection
            
            double lngChangePerSubsection =
                (dest.getLongitude() - start.getLongitude()) / numSubsectionsOfLeg;
                // The change (negative or positive) in longitude across one subsection
            
            int numGasStationsInDatabase = ldsdb.gasStations().length;
                // Used only to generate names for new GasStations.
            
            BusStation current = start;
                // A non-constant variable, the current BusStation or GasStation
                // after which a new GasStation will be inserted if needed
            
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
                else // Is the case if GasStations were already inserted in
                     // this leg during a previous call of this method
                {
                    current = preexisting;
                }
                
                ++i;
                route.add( i, current );
            }
            if( !current.connectedTo( dest ) )
                // Should not be connected yet, but we check to make sure.
            {
                current.connect( dest );
                dest.connect( current );
            }
        }
        ldsdb.update();
    }
}
