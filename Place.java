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
    
    public double milesTo_Old( Place place )
    // 1° latitude constantly = ~69 miles, and 1° longitude varies from 0
    // miles at the poles to a maximum of ~69 miles at the equator.
    // Source: https://gis.stackexchange.com/questions/142326/
    //
    // This method works and is quicker than milesTo below, but this method is
    // less accurate. I am leaving it here in case it is needed, but prefer the
    // other one.
    {
    	double latDiff  = place.latitude  - this.latitude;
        double longDiff = place.longitude - this.longitude;
        
        double vDiffMiles = 69 * latDiff;
        double hDiffMiles = 69 * longDiff * Math.cos( Math.toRadians( latDiff ) );
        
        return Math.sqrt( vDiffMiles*vDiffMiles + hDiffMiles*hDiffMiles );
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
    	return milesTo( place ) / bus.getCruisingConsumption();
    	// miles/(miles/hour) = miles*(hours/mile) = hours
    }
    
    public double directionTo_old( Place place )
    // Direction in degrees (CCW from East, as in Trig) to another place
    {
        double latDiff  = place.latitude  - this.latitude;
        double longDiff = place.longitude - this.longitude;
        
        if( longDiff == 0 ) // Going straight up or down
            return (latDiff > 0) ? 90 : 270;
        
        double arctan = Math.toDegrees( Math.atan( latDiff/longDiff ) );
        // -90 < arctan < 90
        
        if( longDiff > 0 ) // Going right
            return (arctan >= 0) ? arctan : (360 + arctan);
        else // longDiff < 0; Going left
            return 180 + arctan;
    }
    
    public double bearingTo( Place place )
    // Direction in degrees (CW from North, as in Geography) to another place
    // Also from https://www.movable-type.co.uk/scripts/latlong.html
    {
    	double thisLatRad   = Math.toRadians(  this.getLatitude()  );
    	double placeLatRad  = Math.toRadians( place.getLatitude()  );
    	double thisLongRad  = Math.toRadians(  this.getLongitude() );
    	double placeLongRad = Math.toRadians( place.getLongitude() );
    	
    	double f = placeLongRad - thisLongRad;
    	double g = Math.sin(f) * Math.cos( placeLatRad );
    	double h = Math.cos( thisLatRad ) * Math.sin( placeLatRad );
    	double i = Math.sin( thisLatRad ) * Math.cos( placeLatRad ) * Math.cos(f);
    	
    	return Math.toDegrees( Math.atan2( g, h-i ) );
    }
    
    // Returns a direction as text.
    // TODO: Modify maybe
    public String headingTo( Place place )
    {
        // Old:
    	//  double angle = directionTo_old( place );
    	// Basically equivalent to:
    	double angle = 90 - bearingTo( place );
        
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
