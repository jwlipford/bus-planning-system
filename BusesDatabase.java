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
	
	public String[][] longDistBuses()
	{
	    ArrayList<Bus> lDBuses = new ArrayList<Bus>( buses.size() );
	    for( Bus b : buses )
	        if( b.getType() == BusType.longDistance )
	            lDBuses.add(b);
	    String[][] array = new String[ lDBuses.size() ][ 5 ];
	    for( int i = 0; i < lDBuses.size(); ++i )
	    {
	        Bus b = lDBuses.get(i);
	        array[i][0] = b.getMakeAndModel();
	        array[i][1] = "LD";
	        array[i][2] = String.valueOf( b.getTankSize() );
	        array[i][3] = String.valueOf( b.getCruisingConsumption() );
	        array[i][4] = String.valueOf( b.getCruisingSpeed() );
	    }
	    return array;
	}
	
	public Bus[] toArray()
	{
		// IDK why, but {return this.buses.toArray()} did not work.
	    
	    Bus[] array = new Bus[ buses.size() ];
	    for( int i = 0; i < buses.size(); ++i )
	    {
	        array[i] = buses.get(i);
	    }    
	    return array;
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
		bd.delete( bd.buses.size() - 1 ); // Delete ANTI_BUS
		bd.update();
		bd.delete( bd.buses.size() - 1 ); // Delete SUS
		bd.update();
	}
}
