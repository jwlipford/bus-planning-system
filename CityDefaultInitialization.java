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

import java.util.Scanner;
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
    	while( line != null )
    	{
    		String[] splitLine = line.split( ", " );
    		stations.add( new BusStation(
    			Double.parseDouble( splitLine[0] ),
    			Double.parseDouble( splitLine[1] ),
    			splitLine[2].replaceAll( "\"", "" ) ) );
    		line = br.readLine();
    	}
    	br.close();
    	return stations;
    }
	
	public static void main( String[] args ) throws Exception
    {
    	ArrayList<BusStation> stations = stationsFileToArrayList();
		
		// Columns 0-4
		for( int c = 0; c < 5; ++c )
		{
			BusStation[] busLine = new BusStation[8];
			
			// Rows 0-7
			for( int r = 0; r < 8; ++r )
				busLine[r] = stations.get( r + 8*c );
			
			BusPlanningSystem.createBusLine( busLine );
		}
        
        // Rows 0-7
        for( int r = 0; r < 8; ++r )
        {
        	BusStation[] busLine = new BusStation[5];
        	
        	// Columns 0-4
        	for( int c = 0; c < 5; ++c )
        		busLine[c] = stations.get( r + 8*c );
        	
        	BusPlanningSystem.createBusLine( busLine );
        }
        
        System.out.print( "Stations 1-40 created and connected\n\n" );
        
        Scanner scanner = new Scanner( System.in );
        
        System.out.print( "Start Station (1-40): " );
        int s = Integer.parseInt( scanner.nextLine() ) - 1;
        if( s < 0 || s > 39 )
        {
        	scanner.close(); // IDE was showing a warning about closing s for some reason.
        	throw new Exception();
        }
        System.out.print( "Destination Station (1-40): " );
        int d = Integer.parseInt( scanner.nextLine() ) - 1;
        if( d < 0 || d > 39 )
        {
        	scanner.close();
        	throw new Exception();
        }
        scanner.close();
        System.out.println();
        
        BusStation start = stations.get(s);
        BusStation dest  = stations.get(d);
        
        ArrayList<DijkstraStation> dStations =
        		DijkstraStation.busStationsToDijkstraStations( stations, start );
        
        Route bestRoute  = DijkstraStation.dijkstraRoute( dStations, dest, false );
        Route randRoute1 = DijkstraStation.dijkstraRoute( dStations, dest, true  );
        Route randRoute2 = DijkstraStation.dijkstraRoute( dStations, dest, true  );
        // Sometimes a randomly generated route is the same as one of the other
        // routes. TODO: Fix this, maybe. Possibly use Route.equalTo method.
        
        bestRoute.bus  = new Bus( "The Magic School Bus", BusType.city, 80, 7, 35 );
        randRoute1.bus = bestRoute.bus;
        randRoute2.bus = bestRoute.bus;
        
        System.out.println( "------  Best Route  --------------------" );
        System.out.print( bestRoute.toString() );
        System.out.println();
        System.out.println( "------  Semi-Random Route 1  -----------" );
        System.out.print( randRoute1.toString() );
        System.out.println();
        System.out.println( "------  Semi-Random Route 2  -----------" );
        System.out.print( randRoute2.toString() );
    }
}