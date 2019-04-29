/* BusesDatabase:
 * 
 * An interface to the file BusesText.txt, which stores a list of buses. This
 * class reads from and writes to the file, and the file should only be
 * accessed through this class. This class contains methods for creation and
 * deletion of buses, conversion of the database or part of it to arrays of
 * Strings, and other needs.
 * 
 * Especially important is the update() method. Changes to the database are not
 * automatically saved in the text file, and update() must be called to do this.
 * 
 * Unlike the LongDistanceStationsDatabase, multiple instances of this database
 * can be created without, as far as we know, causing failures in the system,
 * though programmers who use this class should choose whether doing so
 * conforms to their needs and style.
 */

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;

public class BusesDatabase
{
	private static final File DATA_FILE = new File( "BusesText.txt" );
	// A File object representing the text file that stores the data. This class
    // is an interface to this file.
	
	private ArrayList<Bus> buses;
	// A list of all the Buses in the database.
	
	public BusesDatabase() throws Exception
	// Reads from the text file to create the list of buses
	{
		BufferedReader br = new BufferedReader( new FileReader( DATA_FILE ) );
		String line = br.readLine(); // Moves line-by-line through the file
		this.buses = new ArrayList<Bus>();
		
		while( line != null && !line.isEmpty() )
		    // The file might or might not end with an empty line
		{
			String[] splitLine = line.split( ", " );
			    // Should have 5 elements corresponding to Bus's 5 fields
			
			String makeAndModel        = splitLine[0];
			double tankSize            = Double.parseDouble( splitLine[2] );
			double cruisingConsumption = Double.parseDouble( splitLine[3] );
			double cruisingSpeed       = Double.parseDouble( splitLine[4] );
			
			BusType type;
			switch( splitLine[1] )
			{
			    case "City": type = BusType.city; break;
			    case "LD":   type = BusType.longDistance; break;
			    default:     throw new Exception( "Error reading BusesText.txt" );
			}
			
			Bus bus = new Bus( makeAndModel, type, tankSize,
			                   cruisingConsumption, cruisingSpeed );
			this.buses.add( bus );
			line = br.readLine();
		}
		
		br.close();
	}
	
	public void update() throws Exception
    // Overwrites the text file with information from the buses field. This
    // method is the only way to save changes made using other methods into the
    // actual text file; without using this method, changes will be lost upon
    // closing the program.
    {
        FileWriter fw = new FileWriter( DATA_FILE );
        
        for( Bus bus : this.buses )
            fw.write( bus.getMakeAndModel() + ", " + bus.getType() + ", " +
                      bus.getTankSize() + ", " + bus.getCruisingConsumption() +
                      ", " + bus.getCruisingSpeed() + "\n" );
        fw.close();
    }
	
	public Bus[] toArray()
	// Returns a 1-dimensional array containing all the Buses in the database
    {
        // I don't know why, but {return this.buses.toArray()} does not work.
        
        Bus[] array = new Bus[ buses.size() ];
        for( int i = 0; i < buses.size(); ++i )
        {
            array[i] = buses.get(i);
        }    
        return array;
    }
	
	public String[][] longDistBuses()
	// Returns a 2D String array in which each row represents a long-distance
	// bus in the database, each of the first five columns represents one of
	// the five fields of the Bus class, and the sixth column represents the
	// Bus's maximum range. Only long-distance buses, not city buses, are
	// included.
	{
	    // Create a list of just long-distance buses:
	    
	    ArrayList<Bus> lDBuses = new ArrayList<Bus>( buses.size() );
	    for( Bus b : buses )
	        if( b.getType() == BusType.longDistance )
	            lDBuses.add(b);
	    
	    // Convert the list to String[][]:
	    
	    String[][] array = new String[ lDBuses.size() ][ 6 ];
	    for( int i = 0; i < lDBuses.size(); ++i )
	    {
	        Bus b = lDBuses.get(i);
	        array[i][0] = b.getMakeAndModel();
	        array[i][1] = "LD";
	        array[i][2] = String.valueOf( b.getTankSize() );
	        array[i][3] = String.valueOf( b.getCruisingConsumption() );
	        array[i][4] = String.valueOf( b.getCruisingSpeed() );
	        array[i][5] = String.valueOf( b.maxRange() );
	    }
	    return array;
	}
	
	public String[][] allBuses()
	// Returns a 2D String array in which each row represents a bus in the
    // database, each of the first five columns represents one of the five
    // fields of the Bus class, and the sixth column represents the Bus's
    // maximum range. Both city and long-distance buses are included.
	{
	    String[][] array = new String[ buses.size() ][6];
	    for( int i = 0; i < buses.size(); ++i )
	    {
	        Bus b = buses.get(i);
            array[i][0] = b.getMakeAndModel();
            array[i][1] = b.getType().toString();
            array[i][2] = String.valueOf( b.getTankSize() );
            array[i][3] = String.valueOf( b.getCruisingConsumption() );
            array[i][4] = String.valueOf( b.getCruisingSpeed() );
            array[i][5] = String.valueOf( b.maxRange() );
	    }
	    return array;
	}
	
	public void addBus( Bus bus ) throws Exception
	// Adds a Bus to the buses field, but does not update the text file (call
    // update() to do so)
	{
		if( bus.getMakeAndModel().contains( ", " ) )
			throw new Exception( "Bus's make and model cannot contain ','" );
		this.buses.add( bus );
	}
	
	public void addNewBus( String mm, BusType bt, double ts, double cc, double cs )
		throws Exception
	// Creates a new Bus from the arguments and adds it to the buses field,
    // but does not update the text file (call update() to do so)
	{
		if( mm.contains( "," ) )
			throw new Exception( "Bus's make and model cannot contain ','" );
		this.buses.add( new Bus( mm, bt, ts, cc, cs ) );
	}
	
	public void delete( int index )
	// Deletes the Bus at the index from the buses field, but does not update
	// the text file (call update() to do so)
	{
		this.buses.remove( index );
	}
}
