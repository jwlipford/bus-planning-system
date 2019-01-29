public class BusStop extends Place
{
    private String name;
    
    public BusStop( double latitude, double longitude, String name )
    {
        super( latitude, longitude );
        this.name = name;
    }
}
