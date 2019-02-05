public class BusPlanningSystem
{
    public static void main( String[] args )
    {
        ;
    }
    
    public static void createBusLine( BusStation... stations )
    {
        for( int i = 0; i < (stations.length - 1); ++i )
        {
            stations[i].connect( stations[ i+1 ] );
            stations[ i+1 ].connect( stations[i] );
        }
    }
}