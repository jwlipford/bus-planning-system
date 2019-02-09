/* This class will NOT be used in the final program. It is just used to create
 a default city to test.

City has 13 bus lines like this:

   --1---9--17-- ... -- 33
     |   |   |			 |
   --2--10--18-- ... -- 34
     |   |   |			 |
   --3--11--19-- ... -- 35
     |   |   |			 |
    ... ... ...  ...   ...
     |   |   |			 |
   --8--16--24-- ... -- 40

Current constraints around Atlanta:

NW) 33.883594, -84.548232
SW) 33.651294, -84.548232
SE) 33.651294, -84.273736
NE) 33.883594, -84.273736

Square Characteristics: ~256 square miles, ~16 mile edges, max length of a bus route is ~23 miles assuming straight line from
	SE to NE
 */

import java.util.Scanner;
import java.util.ArrayList;

public class CityDefaultInitialization
{
    public static void main( String[] args ) throws Exception
    // Testing
    {
        ArrayList<BusStation> stations = new ArrayList<BusStation>(40);
        
        // Columns follow pattern: 1 2 3 n..., (n = n + 1); Longitude matches
        // Rows follow pattern: 1 9 17 n..., (n = n + 8); Latitude matches
		
        // Column 1
		stations.add( new BusStation( 33.883594, -84.548232, "Akkala" ) );	// NW Station
		stations.add( new BusStation( 33.850408, -84.548232, "Eldin" ) );
		stations.add( new BusStation( 33.817223, -84.548232, "Central" ) );
		stations.add( new BusStation( 33.784037, -84.548232, "Faron" ) );
		stations.add( new BusStation( 33.750851, -84.548232, "Hebra" ) );
		stations.add( new BusStation( 33.717665, -84.548232, "Tabantha" ) );
		stations.add( new BusStation( 33.684480, -84.548232, "Ridgeland" ) );
		stations.add( new BusStation( 33.651294, -84.548232, "Lanayru" ) );	// SW Station
			
		// Column 2
		stations.add( new BusStation( 33.883594, -84.479608, "Starborn Valley" ) );	// NNW Station
		stations.add( new BusStation( 33.850408, -84.479608, "Star Haven" ) );
		stations.add( new BusStation( 33.817223, -84.479608, "Crystal Palace" ) );
		stations.add( new BusStation( 33.784037, -84.479608, "Widny Mill" ) );
		stations.add( new BusStation( 33.750851, -84.479608, "Shiver City" ) );
		stations.add( new BusStation( 33.717665, -84.479608, "Gusty Gulch" ) );
		stations.add( new BusStation( 33.684480, -84.479608, "Star Way" ) );
		stations.add( new BusStation( 33.651294, -84.479608, "Forever Forest" ) );	// SSW Station
		
		// Column 3
		stations.add( new BusStation( 33.883594, -84.410984, "Agrabah" ) );	// N Station
		stations.add( new BusStation( 33.850408, -84.410984, "Atlantica" ) );
		stations.add( new BusStation( 33.817223, -84.410984, "Deep Jungle" ) );
		stations.add( new BusStation( 33.784037, -84.410984, "Destiny Islands" ) );
		stations.add( new BusStation( 33.750851, -84.410984, "Neverland" ) );
		stations.add( new BusStation( 33.717665, -84.410984, "Olympus" ) );
		stations.add( new BusStation( 33.684480, -84.410984, "Pride Lands" ) );
		stations.add( new BusStation( 33.651294, -84.410984, "Timeless River" ) );	// S Station
		
		// Column 4
		stations.add( new BusStation( 33.883594, -84.34236, "Besaid" ) );	// NNE Station
		stations.add( new BusStation( 33.850408, -84.34236, "Kilika" ) );
		stations.add( new BusStation( 33.817223, -84.34236, "Lucca" ) );
		stations.add( new BusStation( 33.784037, -84.34236, "Bikanel" ) );
		stations.add( new BusStation( 33.750851, -84.34236, "Macalania" ) );
		stations.add( new BusStation( 33.717665, -84.34236, "Djose" ) );
		stations.add( new BusStation( 33.684480, -84.34236, "Bevelle" ) );
		stations.add( new BusStation( 33.651294, -84.34236, "Zanarkand" ) );	// NNW Station
		
		// Column 5
		stations.add( new BusStation( 33.883594, -84.273736, "Stone Hill" ) );	// NE Station
		stations.add( new BusStation( 33.850408, -84.273736, "Idol Springs" ) );
		stations.add( new BusStation( 33.817223, -84.273736, "Alpine Ridge" ) );
		stations.add( new BusStation( 33.784037, -84.273736, "Terrace Village" ) );
		stations.add( new BusStation( 33.750851, -84.273736, "Sunny Villa" ) );
		stations.add( new BusStation( 33.717665, -84.273736, "Shady Oasis" ) );
		stations.add( new BusStation( 33.684480, -84.273736, "Cloud Spires" ) );
		stations.add( new BusStation( 33.651294, -84.273736, "Crystal Islands" ) );	// SE Station
		
		// Bus Line Column 1
        BusPlanningSystem.createBusLine( stations.get(0), stations.get(1), stations.get(2), stations.get(3), 
        		stations.get(4), stations.get(5), stations.get(6), stations.get(7) );
        
        // Bus Line Column 2
        BusPlanningSystem.createBusLine( stations.get(8), stations.get(9), stations.get(10), stations.get(11), 
        		stations.get(12), stations.get(13), stations.get(14), stations.get(15) );
        
        // Bus Line Column 3
        BusPlanningSystem.createBusLine( stations.get(16), stations.get(17), stations.get(18), stations.get(19), 
        		stations.get(20), stations.get(21), stations.get(22), stations.get(23) );
        
        // Bus Line Column 4
        BusPlanningSystem.createBusLine( stations.get(24), stations.get(25), stations.get(26), stations.get(27), 
        		stations.get(28), stations.get(29), stations.get(30), stations.get(31) );
        
        // Bus Line Column 5
        BusPlanningSystem.createBusLine( stations.get(32), stations.get(33), stations.get(34), stations.get(35), 
        		stations.get(36), stations.get(37), stations.get(38), stations.get(39) );
        
        // Bus Line Row 1
        BusPlanningSystem.createBusLine( stations.get(0), stations.get(8), stations.get(16), stations.get(24), 
        		stations.get(32) );
        
        // Bus Line Row 2
        BusPlanningSystem.createBusLine( stations.get(1), stations.get(9), stations.get(17), stations.get(25), 
        		stations.get(33) );
        
        // Bus Line Row 3
        BusPlanningSystem.createBusLine( stations.get(2), stations.get(10), stations.get(18), stations.get(26), 
        		stations.get(34) );
        
        // Bus Line Row 4
        BusPlanningSystem.createBusLine( stations.get(3), stations.get(11), stations.get(19), stations.get(27), 
        		stations.get(35) );
        
        // Bus Line Row 5
        BusPlanningSystem.createBusLine( stations.get(4), stations.get(12), stations.get(20), stations.get(28), 
        		stations.get(36) );
        
        // Bus Line Row 6
        BusPlanningSystem.createBusLine( stations.get(5), stations.get(13), stations.get(21), stations.get(29), 
        		stations.get(37) );
        
        // Bus Line Row 7
        BusPlanningSystem.createBusLine( stations.get(6), stations.get(14), stations.get(22), stations.get(30), 
        		stations.get(38) );
        
        // Bus Line Row 8
        BusPlanningSystem.createBusLine( stations.get(7), stations.get(15), stations.get(23), stations.get(31), 
        		stations.get(39) );
        
        System.out.print( "Stations 1-40 created and connected\n\n" );
        
        Scanner s = new Scanner( System.in );
        
        System.out.print( "Start Station (1-40): " );
        int start = Integer.parseInt( s.nextLine() );
        if( start < 0 || start > 7 )
        {
        	s.close(); // IDE was showing a warning about closing s for some reason.
        	throw new Exception();
        }
        System.out.print( "Destination Station (1-40): " );
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