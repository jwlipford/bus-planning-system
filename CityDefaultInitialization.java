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

public class CityDefaultInitialization
{
    public static void main( String[] args )
    {
        BusStation[] stations = new BusStation[]
		{
			new BusStation( 10, 10, "Station 0" ),
			new BusStation( 10, 20, "Station 1" ),
			new BusStation( 10, 30, "Station 2" ),
			new BusStation( 20, 10, "Station 3" ),
			new BusStation( 20, 20, "Station 4" ),
			new BusStation( 20, 30, "Station 5" ),
			new BusStation( 30, 20, "Station 6" ),
			new BusStation( 30, 30, "Station 7" )
		};
        
        BusPlanningSystem.createBusLine( stations[0], stations[1], stations[2] );
        BusPlanningSystem.createBusLine( stations[3], stations[4], stations[5] );
        BusPlanningSystem.createBusLine( stations[6], stations[7] );
        BusPlanningSystem.createBusLine( stations[0], stations[3] );
        BusPlanningSystem.createBusLine( stations[1], stations[4], stations[6] );
        BusPlanningSystem.createBusLine( stations[2], stations[5], stations[7] );
        
        System.out.println( "Ran CityDefaultInitialization.main()" );
    }
}