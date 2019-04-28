/* Place:
 * 
 * A location on the surface of the earth, identified by a latitude and a
 * longitude. This class is abstract, so it cannot be used directly, but
 * BusStation (and GasStation, which extends BusStation) extends this class.
 * Methods in this class find the distance to another place, find the direction
 * to another place (numerically, the "bearing," or as a String, the
 * "heading"), and find whether this station is equivalent ("equal") to another
 * Place.
 * 
 * Two methods in this class use information found at
 * https://www.movable-type.co.uk/scripts/latlong.html.
 * These methods' comments contain more information.
 */

public abstract class Place
{
    public static final int EARTH_RADIUS = 3959; // Earth's radius in miles
	
	private double latitude;  // degrees (-90 to 90) north of Equator
    private double longitude; // degrees (-180 to 180) east of Prime Meridian
    
    public double getLatitude (){ return latitude;  } // latitude accessor
    public double getLongitude(){ return longitude; } // longitude accessor
    
    public Place( double latitude, double longitude ) throws Exception
    // Checks that the latitude and longitude arguments are in the right ranges
    // and, if so, assigns them to the corresponding fields
    {
        if( latitude < -90 )
        	throw new Exception( "Latitude < -90" );
        if( latitude >  90 )
        	throw new Exception( "Latitude > 90" );
        if( longitude < -180 )
        	throw new Exception( "Longitude < -180" );
        if( longitude > 180 )
        	throw new Exception( "Longitude > 180" );
    	this.latitude  = latitude;
        this.longitude = longitude;
    }
    
    public boolean equals( Place place )
    // Returns whether this object has the same coordinates as the place
    {
        return   (this.latitude  == place.latitude
               && this.longitude == place.longitude);
    }
    
    public double milesTo( Place place )
    /* Returns the number of miles between this object and place along
     * the surface of the earth, taking into account its curvature.
     * 
     * This method uses formulas found in the Distance section of
     * https://www.movable-type.co.uk/scripts/latlong.html.
     * I have no idea how they work, but I have verified that they do.
     */
    {
    	double thisLatRad   = Math.toRadians(  this.getLatitude()  );
    	double placeLatRad  = Math.toRadians( place.getLatitude()  );
    	double thisLongRad  = Math.toRadians(  this.getLongitude() );
    	double placeLongRad = Math.toRadians( place.getLongitude() );
    	
    	double f = Math.sin( 0.5*( Math.abs( thisLatRad - placeLatRad ) ) );
    	double g = Math.cos( thisLatRad );
    	double h = Math.cos( placeLatRad );
    	double i = Math.sin( 0.5*( Math.abs( thisLongRad - placeLongRad ) ) );
    	
    	double a = f*f + g*h*i*i;
    	double c = 2 * Math.atan2( Math.sqrt(a), Math.sqrt( 1-a ) );
    	return EARTH_RADIUS * c;
    }
    
    public double bearingTo( Place place )
    /* Returns the direction (at the start of travel -- direction may change
     * during travel over long distances) in degrees (0° to 360°; clockwise
     * from North, as in Geography) from this object to another place.
     * 
     * This method, like milesTo, uses a formula found in the Bearing section
	 * of https://www.movable-type.co.uk/scripts/latlong.html, though I modified
	 * it such that it returns positive numbers rather than numbers in range
	 * [-180, 180]. Like milesTo, I have no idea how it works, but I have
	 * verified that it does.
     */
    {
        double thisLatRad   = Math.toRadians(  this.getLatitude()  );
        double placeLatRad  = Math.toRadians( place.getLatitude()  );
        double thisLongRad  = Math.toRadians(  this.getLongitude() );
        double placeLongRad = Math.toRadians( place.getLongitude() );
    	
        double f = placeLongRad - thisLongRad;
        double g = Math.sin(f) * Math.cos( placeLatRad );
        double h = Math.cos( thisLatRad ) * Math.sin( placeLatRad );
        double i = Math.sin( thisLatRad ) * Math.cos( placeLatRad ) * Math.cos(f);
    	
        double degrees = Math.toDegrees( Math.atan2( g, h-i ) );
        // Java's % operator is remainder, not modulus, and can return a negative.
        return (degrees + 360) % 360;
    }
    
    public String headingTo( Place place )
    // Returns a bearing as text with one of 8 major directions (north,
    // northeast, east, southeast, ...) in parentheses after the numeric angle
    {
        // Convert to float to display fewer characters
    	float bearing  = (float)this.bearingTo( place );
    	
    	if( 337.5 < bearing || bearing <= 22.5 )
    		return bearing + "° (North)";
    	else if(22.5 < bearing && bearing <= 67.5)
    		return bearing + "° (Northeast)";
    	else if( 67.5 < bearing && bearing <= 112.5 )
    		return bearing + "° (East)";
    	else if(112.5 < bearing && bearing <= 157.5)
    		return bearing + "° (Southeast)";
    	else if( 157.5 < bearing && bearing <= 202.5 )
    		return bearing + "° (South)";
    	else if( 202.5 < bearing && bearing <= 247.5 )
    		return bearing + "° (Southwest)";
    	else if( 247.5 < bearing && bearing <= 292.5 )
    		return bearing + "° (West)";
    	else if( 292.5 < bearing && bearing <= 337.5 )
    		return bearing + "° (West)";
    	else
    		return bearing + "°";
    }
}