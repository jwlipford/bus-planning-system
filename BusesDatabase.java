import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;

public class BusesDatabase
{
	private static final File DATA_FILE = new File( "BusesText.txt" );
	
	private ArrayList<Bus> buses;
	
	public BusesDatabase() throws Exception
	{
		BufferedReader br = new BufferedReader( new FileReader( DATA_FILE ) );
		String line = br.readLine();
		this.buses = new ArrayList<Bus>();
		
		while( line != null && !line.isEmpty() )
		{
			String[] splitLine = line.split( ", " );
			
			String makeAndModel        = splitLine[0];
			double tankSize            = Double.parseDouble( splitLine[2] );
			double cruisingConsumption = Double.parseDouble( splitLine[3] );
			double cruisingSpeed       = Double.parseDouble( splitLine[4] );
			
			BusType type;
			switch( splitLine[1] )
			{
			    case "City": type = BusType.city; break;
			    case "LD":   type = BusType.longDistance; break;
			    default:     throw new Exception();
			}
			
			Bus bus = new Bus( makeAndModel, type, tankSize,
			                   cruisingConsumption, cruisingSpeed );
			this.buses.add( bus );
			line = br.readLine();
		}
		
		br.close();
	}
	
	public Bus[] toArray()
	{
		return (Bus[])this.buses.toArray();
	}
	
	public void update() throws Exception
	{
		FileWriter fw = new FileWriter( DATA_FILE );
		
		for( Bus bus : this.buses )
			fw.write( bus.getMakeAndModel() + ", " + bus.getType() + ", " +
		              bus.getTankSize() + ", " + bus.getCruisingConsumption() +
		              ", " + bus.getCruisingSpeed() + "\n" );
		fw.close();
	}
	
	public void addBus( Bus bus ) throws Exception
	{
		if( bus.getMakeAndModel().contains( ", " ) )
			throw new Exception( "Bus's make and model cannot contain ','" );
		this.buses.add( bus );
	}
	
	public void addNewBus( String mm, BusType bt, double ts, double cc, double cs )
		throws Exception
	{
		if( mm.contains( "," ) )
			throw new Exception( "Bus's make and model cannot contain ','" );
		this.buses.add( new Bus( mm, bt, ts, cc, cs ) );
	}
	
	public void delete( int index )
	{
		this.buses.remove( index );
	}
	
	
	
	// TESTING ONLY:
	public static void main(String[] args) throws Exception
	{
		// Open BusesDatabase.txt and see how it changes during this method!
		BusesDatabase bd = new BusesDatabase();
		final Bus SUS = new Bus( "Sauce", BusType.longDistance,
		                         Double.NaN, Double.NaN, Double.NaN );
		bd.addBus( SUS );
		bd.update();
		final Bus ANTI_BUS = new Bus( "Anti-bus", BusType.city, Double.NEGATIVE_INFINITY,
				                      Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY );
		bd.addBus( ANTI_BUS );
		bd.update();
		bd.delete( bd.buses.size() - 1 ); // Delete JUPITER
		bd.update();
		bd.delete( bd.buses.size() - 1 ); // Delete THE_MOON
		bd.update();
	}
}
