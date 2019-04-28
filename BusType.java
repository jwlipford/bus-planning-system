/* BusType:
 * 
 * An enum that can store one of two possibilities, "city" and "long-distance,"
 * for the scale of the area traveled by a Bus. The Bus class contains a field
 * for the BusType.
 * 
 * Currently, this class is not very useful because only one Bus of the "city"
 * type exists and no more can be created, but this class could become useful
 * if future iterations of the system were developed.
 */

public enum BusType
{
    city,         // The corresponding Bus travels within a single city
    longDistance; // The corresponding Bus travels between cities
	
	
	@Override
	public String toString()
	// This method, used only in BusesDatabase.update(), converts this object
	// to a short String recorded in BusesText.txt
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