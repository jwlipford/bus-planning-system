/* GasStation:
 * 
 * This extremely small class extends and behaves exactly the same as BusStation.
 * The classes differ only in use -- the user can select BusStations but not
 * GasStations as departures and destinations. In retrospect, I believe I should
 * have implemented this difference as a boolean or enum field in BusStation, but
 * the current implementation works without any problem.
 */

public class GasStation extends BusStation
{
	public GasStation( double latitude, double longitude, String name ) throws Exception
	// Calls the BusStation constructor
	{
		super( latitude, longitude, name );
	}
}