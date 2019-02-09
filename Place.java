/* Method milesTo uses formulas found in the Distance section of
 * https://www.movable-type.co.uk/scripts/latlong.html
 */


public class Place
{
    public static final int EARTH_RADIUS = 3959; // miles
	
	private double latitude;  // degrees (0 to 90) south of North Pole
    private double longitude; // degrees (-180 to 180) east of Prime Meridian
    
    public double getLatitude (){ return latitude;  }
    public double getLongitude(){ return longitude; }
    
    public Place( double latitude, double longitude ) throws Exception
    {
        if( latitude < 0 )
        	throw new Exception( "Latitude < 0" );
        if( latitude > 90 )
        	throw new Exception( "Latitude > 90" );
        if( longitude < -180 )
        	throw new Exception( "Longitude < -180" );
        if( longitude > 180 )
        	throw new Exception( "Longitude > 180" );
    	this.latitude  = latitude;
        this.longitude = longitude;
    }
    
    public double milesTo( Place place )
    {
    	// Formulas found at the website cited above
    	double f = Math.sin( 0.5 * (place.getLatitude() - this.getLatitude()) );
    	double g = Math.sin( 0.5 * (place.getLongitude() - this.getLongitude()) );
    	double h = Math.cos( place.getLatitude() ) + Math.cos( this.getLatitude() );
    	double a = f*f + g*g + h;
    	double c = 2 * Math.atan2( Math.sqrt(a), Math.sqrt( 1-a ) );
    	return EARTH_RADIUS * c;
    }
    
    public double hoursTo( Place place, Bus bus )
    {
    	double speed;
    	if( bus == null )
    		speed = 35;
    	else
    		speed = bus.getCruisingConsumption();
    	return milesTo( place ) / speed;
    	// miles/(miles/hour) = miles*(hours/mile) = hours
    }
    
    // Direction in degrees to another place
    public double directionTo( Place place )
    {
        double latDiff  = place.latitude  - this.latitude;
        double longDiff = place.longitude - this.longitude;
        
        if( longDiff == 0 ) // Going straight up or down
            return (latDiff > 0) ? 90 : 270;
        
        double arctan = Math.toDegrees( Math.atan( latDiff/longDiff ) );
        // -90 < arctan < 90
        
        if( longDiff > 0 ) // Going right
            return (arctan > 0) ? arctan : (360 + arctan);
        else // longDiff < 0; Going left
            return 180 + arctan;
    }
    
    // Returns a direction as text. Tested and works as of 1-Feb-19
    public String headingTo( Place place )
    {
        double angle = directionTo( place );
        
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
