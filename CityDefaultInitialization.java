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
import java.lang.reflect.Array;

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
	
	public static String[] implementTravel(int begin, int end) throws Exception
    {
		String[] plans = new String[3];
		
    	ArrayList<BusStation> stations = stationsFileToArrayList();
        
        System.out.print( "Stations 1-40 created and connected\n\n" );
        
        //Scanner scanner = new Scanner( System.in );
        
        //System.out.print( "Start Station (1-40): " );
        int s = begin - 1;
        if( s < 0 || s > 39 )
        	throw new Exception();
        
        //System.out.print( "Destination Station (1-40): " );
        int d = end - 1;
        if( d < 0 || d > 39 )
        	throw new Exception();
        
        BusStation start = stations.get(s);
        BusStation dest  = stations.get(d);
        
        ArrayList<DijkstraStation> dStations =
        		DijkstraStation.busStationsToDijkstraStations( stations, start );
        
        Route bestRoute  = DijkstraStation.dijkstraRoute( dStations, dest, false, 0, 0 );
        Route randRoute1 = DijkstraStation.dijkstraRoute( dStations, dest, true, 0, 0  );
        Route randRoute2 = DijkstraStation.dijkstraRoute( dStations, dest, true, 0, 0  );
        // Sometimes a randomly generated route is the same as one of the other
        // routes. TODO: Fix this, maybe. Possibly use Route.equalTo method.
        
        if(!bestRoute.equalTo(randRoute1) && !bestRoute.equalTo(randRoute2) && !randRoute1.equalTo(randRoute2))
        {
	        bestRoute.bus  = new Bus( "The Magic School Bus", BusType.city, 80, 7, 35 );
	        randRoute1.bus = bestRoute.bus;
	        randRoute2.bus = bestRoute.bus;
        }
        else 
        {
        	int check = 0;
        	int deviation = 1;
        	int deviationPoint1 = 1;
        	int deviationPoint2 = 2;
        	
        	while(bestRoute.equalTo(randRoute1) || bestRoute.equalTo(randRoute2) || randRoute1.equalTo(randRoute2))
        	{
        		if(bestRoute.equalTo(randRoute2) || randRoute1.equalTo(randRoute2)) 
        		{
        			randRoute2 = DijkstraStation.dijkstraRoute( dStations, dest, true, deviation, deviationPoint2 );
        		}
        		if(bestRoute.equalTo(randRoute1))
        		{
        			randRoute1 = DijkstraStation.dijkstraRoute( dStations, dest, true, deviation, deviationPoint1 );
        		}
        		if(check >= 2)
        		{
        			System.out.println("Unable to generate 3 unique routes");
        			break;
        		}

        		check++;
        	}
        	
        	bestRoute.bus  = new Bus( "The Magic School Bus", BusType.city, 80, 7, 35 );
	        randRoute1.bus = bestRoute.bus;
	        randRoute2.bus = bestRoute.bus;
        }
        
        /*System.out.println( "------  Best Route  --------------------" );
        System.out.print( bestRoute.toString() );
        System.out.println();
        System.out.println( "------  Semi-Random Route 1  -----------" );
        System.out.print( randRoute1.toString() );
        System.out.println();
        System.out.println( "------  Semi-Random Route 2  -----------" );
        System.out.print( randRoute2.toString() );*/
        
        
        plans[0] = bestRoute.toString();
        plans[1] = randRoute1.toString();
        plans[2] = randRoute2.toString();
        
        return plans;
    }
}