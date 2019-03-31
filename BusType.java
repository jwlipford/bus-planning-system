public enum BusType
{
    city, longDistance;
	
	
	@Override
	public String toString() // Used in BusesDatabase.update()
	{
		switch( this )
		{
			case city:         return "City";
			case longDistance: return "LD";
			default:           return null;
			// Compiler complained without default case
		}
	}
}