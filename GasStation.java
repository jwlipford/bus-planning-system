public class GasStation extends BusStation
{
	public GasStation( double latitude, double longitude, String name ) throws Exception
	{
		super( latitude, longitude, name );
	}
	
	@Override
	public String toString()
	{
		return "GAS STATION: " + super.toString();
	}
}