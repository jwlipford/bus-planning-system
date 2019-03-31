import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;

public class LongDistStationsDatabase
{
	private static final File DATA_FILE = new File( "LongDistStationsText.txt" );
	
	private ArrayList<BusStation> lDStations;
	// ldStations can also store GasStations, since GasStation extends BusStation.

	public void createConnection( int i0, int i1 )
	// Connects the (i0)th BusStation to the (i1)th BusStation
	{
		BusStation bs0 = this.lDStations.get( i0 );
		BusStation bs1 = this.lDStations.get( i1 );
		bs0.connect( bs1 );
		bs1.connect( bs0 );
	}
	
	public LongDistStationsDatabase() throws Exception
	// Reads from DATA_FILE to lDStations
	{
		BufferedReader br = new BufferedReader( new FileReader( DATA_FILE ) );
		String line = br.readLine();
		this.lDStations = new ArrayList<BusStation>();
		
    	while( !line.isEmpty() ) // until line.Equals("")
    	{
    		String[] splitLine = line.split( ", " );
    		
    		double lat = Double.parseDouble( splitLine[0] );
			double lng = Double.parseDouble( splitLine[1] );
			String name = splitLine[3];
			
			boolean isGasStation;
    		switch( splitLine[2] )
    		{
    			case "City": isGasStation = false; break;
    			case "Gas":  isGasStation = true;  break;
    			default:     throw new Exception();
    		}

    		BusStation lDStation = isGasStation ? new GasStation( lat, lng, name )
                                                : new BusStation( lat, lng, name );
    		this.lDStations.add( lDStation );
    		line = br.readLine();
    	}
    	line = br.readLine();
    	while( line != null && !line.isEmpty() )
    	{
    		String[] splitLine = line.split( " " );
    		// In this file (as opposed to CityStationsText.txt), each of these
    		// lines has only 1 connection, making the rest of this block very simple.
    		this.createConnection( Integer.parseInt( splitLine[0] ),
    				               Integer.parseInt( splitLine[1] ) );
    		line = br.readLine();
    	}
    	br.close();
	}
	
	public BusStation[] toArray()
	{
		return (BusStation[])this.lDStations.toArray();
	}
	
	public void update() throws Exception
	// Overwrites DATA_FILE with information from lDStations
	{
		FileWriter fw = new FileWriter( DATA_FILE );
		
		// Write first part of file:
		for( BusStation bs : this.lDStations )
			fw.write( bs.getLatitude()  + ", " + bs.getLongitude() + ", " +
			          (bs.getClass() == GasStation.class ? "Gas, " : "City, ") +
			          bs.getName() + "\n" );
		fw.write( "\n" );
		
		// Write second part of file:
		for( int i = 0; i < this.lDStations.size(); ++i )
			for( int j = i + 1; j < this.lDStations.size(); ++j )
				if( this.lDStations.get(i).connectedStations.contains( this.lDStations.get(j) ) )
					fw.write( i + " " + j + "\n" );
		
		fw.close();
	}
	
	public void addLDStation( BusStation lDStation ) throws Exception
	{
		if( lDStation.getName().contains( "," ) )
			throw new Exception( "Long-distance bus station's name cannot contain ','" );
		this.lDStations.add( lDStation );
	}
	
	public void addNewLDStation(
		double lat, double lng,	boolean isGasStation, String name )
		throws Exception
	// Adds a BusStation to lDStations, but does not update DATA_FILE (call
	// update() to do so)
	{
		if( name.contains( "," ) )
			throw new Exception( "Long-distance bus station's name cannot contain ','" );
		BusStation lDStation = isGasStation ? new GasStation( lat, lng, name )
				                            : new BusStation( lat, lng, name );
		this.lDStations.add( lDStation );
	}
	
	public void delete( int index )
	// Deletes a BusStation from lDStations, but does not update DATA_FILE
	// (call update() to do so)
	{
		String name = this.lDStations.get( index ).getName();
		this.lDStations.remove( index );
		for( BusStation bs : this.lDStations )
		{
			for( int i = 0; i < bs.connectedStations.size(); ++i )
			{
				if( bs.connectedStations.get(i).getName().equals( name ) )
					bs.connectedStations.remove(i);
			}
		}
	}
	
	
	
	// TESTING ONLY:
	public static void main( String[] args ) throws Exception
	{
		// Open LongDistStationsText.txt and see how it changes during this method!
		LongDistStationsDatabase ldsd = new LongDistStationsDatabase();
		final BusStation THE_MOON = new BusStation(	Double.NaN, Double.NaN, "THE MOON" );
		ldsd.addLDStation( THE_MOON );
		ldsd.update();
		final GasStation JUPITER = new GasStation( Double.NaN, Double.NaN, "JUPITER" );
		ldsd.addLDStation( JUPITER );
		ldsd.createConnection( 0, ldsd.lDStations.size() - 1 ); // Connect JUPITER to NYC
		ldsd.update();
		ldsd.delete( ldsd.lDStations.size() - 1 ); // Delete JUPITER
		ldsd.update();
		ldsd.delete( ldsd.lDStations.size() - 1 ); // Delete THE_MOON
		ldsd.update();
	}
}
