// Two methods in this class use information found at
// https://www.movable-type.co.uk/scripts/latlong.html.
// These methods' comments contain more information.

public abstract class Place
{
    public static final int EARTH_RADIUS = 3959; // miles
	
	private double latitude;  // degrees (-90 to 90) north of Equator
    private double longitude; // degrees (-180 to 180) east of Prime Meridian
    
    public double getLatitude (){ return latitude;  }
    public double getLongitude(){ return longitude; }
    
    public Place( double latitude, double longitude ) throws Exception
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
    
    public double milesTo( Place place )
    // This method uses formulas found in the Distance section of
	// https://www.movable-type.co.uk/scripts/latlong.html.
    // I have no idea how they work, but I have verified that they do.
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
    
    public double hoursTo( Place place, Bus bus )
    {
    	return this.milesTo( place ) / bus.getCruisingConsumption();
    	// miles/(miles/hour) = miles*(hours/mile) = hours
    }
    
    public double bearingTo( Place place )
    // This method, like milesTo, uses a formula found in the Bearing section
	// of https://www.movable-type.co.uk/scripts/latlong.html, though I applied
    // a modulus since the given formula sometimes produced negative numbers.
    // This method returns the direction in degrees (0° to 360°; clockwise from
    // North, as in Geography) to another place.
    {
    	double thisLatRad   = Math.toRadians(  this.getLatitude()  );
    	double placeLatRad  = Math.toRadians( place.getLatitude()  );
    	double thisLongRad  = Math.toRadians(  this.getLongitude() );
    	double placeLongRad = Math.toRadians( place.getLongitude() );
    	
    	double f = placeLongRad - thisLongRad;
    	double g = Math.sin(f) * Math.cos( placeLatRad );
    	double h = Math.cos( thisLatRad ) * Math.sin( placeLatRad );
    	double i = Math.sin( thisLatRad ) * Math.cos( placeLatRad ) * Math.cos(f);
    	
    	return Math.toDegrees( Math.atan2( g, h-i ) ) % 360;
    }
    
    public String headingTo( Place place )
    // Returns a bearing as text
    {
        // Convert to float to use fewer characters
    	float bearing  = (float)this.bearingTo( place );
    	
    	if( 359 < bearing || bearing < 1 )
    		return bearing + "° (North)";
    	else if( 89 < bearing && bearing < 91 )
    		return bearing + "° (East)";
    	else if( 179 < bearing && bearing < 181 )
    		return bearing + "° (South)";
    	else if( 269 < bearing && bearing < 271 )
    		return bearing + "° (West)";
    	else
    		return bearing + "°";
    }
}
