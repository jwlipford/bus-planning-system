public class BusPlanningSystem
{
    public static void main( String[] args )
    {
        // TEMPORARY TESTING
        Place p1 = new Place( 0, 0 ), p2 = new Place( -0.5, -0.866 );
        System.out.print( p1.headingTo(p2) );
        // Correctly outputs "30° South of West"
    }
}