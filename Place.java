public class Place
{
    private double latitude;
    private double longitude;
    
    public double getLatitude (){ return latitude;  }
    public double getLongitude(){ return longitude; }
    
    public Place( double latitude, double longitude )
    {
        this.latitude  = latitude;
        this.longitude = longitude;
    }
    
    public double distanceTo( Place place )
    {
        double latDiff  = place.latitude  - this.latitude;
        double longDiff = place.longitude - this.longitude;
        return Math.sqrt( latDiff*latDiff + longDiff*longDiff );
    }
}
