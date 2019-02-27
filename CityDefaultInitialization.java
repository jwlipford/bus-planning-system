/* This class will NOT be used in the final program. It is just used to create
 a default city to test.
City has 13 bus lines like this:
   --1---9--17--25--33--
     |   |   |   |   |
   --2--10--18--26--34--
     |   |   |   |   |
   --3--11--19--27--35--
     |   |   |	 |   |
    ... ... ... ... ...
     |   |   |	 |	 |
   --8--16--24--32--40--
Current constraints around Atlanta:
NW) 33.883594, -84.548232
SW) 33.651294, -84.548232
SE) 33.651294, -84.273736
NE) 33.883594, -84.273736
Square Characteristics: ~256 square miles, ~16 mile edges, max length of a bus
route is ~23 miles assuming straight line from SE to NE
 */

import java.util.ArrayList;
import java.io.*;

public class CityDefaultInitialization
{
	public static ArrayList<BusStation> stationsFileToArrayList() throws Exception
    {
    	File file = new File( "CityStationsText.txt" );
    	BufferedReader br = new BufferedReader( new FileReader( file ) );
    	ArrayList<BusStation> stations = new ArrayList<BusStation>();
    	String line = br.readLine();
    	while( !line.isEmpty() ) // until line.Equals("")
    	{
    		String[] splitLine = line.split( ", " );
    		stations.add( new BusStation(
    			Double.parseDouble( splitLine[0] ),
    			Double.parseDouble( splitLine[1] ),
    			splitLine[2].replaceAll( "\"", "" ) ) );
    		line = br.readLine();
    	}
    	line = br.readLine();
    	while( line != null )
    	{
    		String[] splitLine = line.split( " " );
    		for( int i = 0; i < (splitLine.length - 1); ++i )
    		{
    			int index0 = Integer.parseInt( splitLine[  i  ] );
    			int index1 = Integer.parseInt( splitLine[ i+1 ] );
    			stations.get( index0 ).connect( stations.get( index1 ) );
    			stations.get( index1 ).connect( stations.get( index0 ) );
    		}
    		line = br.readLine();
    	}
    	br.close();
    	return stations;
    }
	
    // Generates three routes
	public static Route[] implementTravel(int begin, int end) throws Exception
    {
		ArrayList<BusStation> stations = stationsFileToArrayList();
        
		// Just for debugging purposes
        System.out.print( "Stations 1-40 created and connected\n\n" );
        
        int s = begin - 1;
        if( s < 0 || s > 39 )
        	throw new Exception();
        
        int d = end - 1;
        if( d < 0 || d > 39 )
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