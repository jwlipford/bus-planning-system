// This class will NOT be used in the final program. It is just used to create
// a default city to test.

// City will have 6 (min was 5) bus lines and look like this:
//     |   |   |
//   --0---1---2--
//     |   |   |
//   --3---4---5--
//         |   |
//        -6---7--
//         |   |

import java.util.Scanner;
import java.util.ArrayList;

public class CityDefaultInitialization
{
    public static void main( String[] args ) throws Exception
    // Testing
    {
        ArrayList<BusStation> stations = new ArrayList<BusStation>(8);
		
		stations.add( new BusStation( 10, 10, "Station 0" ) );
		stations.add( new BusStation( 10, 20, "Station 1" ) );
		stations.add( new BusStation( 10, 30, "Station 2" ) );
		stations.add( new BusStation( 20, 10, "Station 3" ) );
		stations.add( new BusStation( 20, 20, "Station 4" ) );
		stations.add( new BusStation( 20, 30, "Station 5" ) );
		stations.add( new BusStation( 30, 20, "Station 6" ) );
		stations.add( new BusStation( 30, 30, "Station 7" ) );
		
        BusPlanningSystem.createBusLine( stations.get(0), stations.get(1), stations.get(2) );
        BusPlanningSystem.createBusLine( stations.get(3), stations.get(4), stations.get(5) );
        BusPlanningSystem.createBusLine( stations.get(6), stations.get(7) );
        BusPlanningSystem.createBusLine( stations.get(0), stations.get(3) );
        BusPlanningSystem.createBusLine( stations.get(1), stations.get(4), stations.get(6) );
        BusPlanningSystem.createBusLine( stations.get(2), stations.get(5), stations.get(7) );
        
        System.out.print( "Stations 0-7 created and connected\n\n" );
        
        Scanner s = new Scanner( System.in );
        
        System.out.print( "Start Station (0-7): " );
        int start = Integer.parseInt( s.nextLine() );
        if( start < 0 || start > 7 )
        {
        	s.close(); // IDE was showing a warning about closing s for some reason.
        	throw new Exception();
        }
        System.out.print( "Destination Station (0-7): " );
        int dest = Integer.parseInt( s.nextLine() );
        if( dest < 0 || dest > 7 )
        {
        	s.close();
        	throw new Exception();
        }
        s.close();
        System.out.println();
        
        ArrayList<DijkstraStation> dStations =
        		DijkstraStation.busStationsToDijkstraStations( stations, stations.get( start ) );
        Route r = DijkstraStation.dijkstraRoute( dStations, stations.get( dest ) );
        System.out.print( r.toString() );
    }
}