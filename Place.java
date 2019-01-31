public class Place
{
    private double latitude;  // corresponds to a  y coordinate
    private double longitude; // corresponds to an x coordinate
    
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
    
    public String directionTo( Place place )
    {
        // Not tested as of 1-31, but I think the general idea is right.
        
        double latDiff  = place.latitude  - this.latitude;
        double longDiff = place.longitude - this.longitude;
        double angle    = Math.toDegrees( Math.atan( latDiff/longDiff ) );
        
        if(      angle ==   0 )     return "Due East";
        else if( angle  <  45 )     return angle + "° North of East";
        else if( angle ==  45 )     return "North-East";
        else if( angle  <  90 )     return (90 - angle) + "° East of North";
        else if( angle ==  90 )     return "Due North";
        else if( angle  < 135 )     return (angle - 90) + "° West of North";
        else if( angle == 135 )     return "North-West";
        else if( angle  < 180 )     return (180 - angle) + "° North of West";
        else if( angle == 180 )     return "Due West";
        else if( angle  < 225 )     return (angle - 180) + "° South of West";
        else if( angle == 225 )     return "South-West";
        else if( angle  < 270 )     return (270 - angle) + "° West of South";
        else if( angle == 270 )     return "Due South";
        else if( angle  < 315 )     return (angle - 270) + "° East of South";
        else if( angle == 315 )     return "South-East";
        else                        return (360 - angle) + "° South of East";
    }
}
