import java.util.ArrayList;

public class BusPlanningSystem
{
    public static void main( String[] args )
    {
        ;
    }
    
    public static void createBusLine( BusStation... busLine )
    {
        for( int i = 0; i < (busLine.length - 1); ++i )
        {
            busLine[i].connect( busLine[ i+1 ] );
            busLine[ i+1 ].connect( busLine[i] );
        }
    }
}